package fi.mazhar.dhikr

import fi.mazhar.dhikr.content.Routines
import kotlin.random.Random

/**
 * Facade tying the canonical content, the engine, and a [DhikrStore] together.
 * Every mutation is persisted immediately, so counts survive process death
 * and app restarts. Fully offline — no network is ever touched.
 */
class DhikrRepository(
    private val store: DhikrStore,
    private val clock: Clock,
) {
    var state: SavedState = SavedState()
        private set

    fun load() {
        state = store.load()?.withIndependentTahlilProgress()?.withSplitIstighfarProgress() ?: SavedState()
    }

    private fun commit(next: SavedState) {
        state = next
        store.save(state)
    }

    // -------------------------------------------------------------- queries

    fun todayKey(): String = clock.currentDayKey()

    fun routineState(routineId: RoutineId): RoutineUiState? =
        DhikrEngine.routineState(state, routineId, todayKey())

    fun daySummary(): DaySummary = DhikrEngine.daySummary(state, todayKey())

    fun streak(): Int = DhikrEngine.streak(state, todayKey())

    fun todayComplete(): Boolean = DhikrEngine.todayHasCompletedRoutine(state, todayKey())

    fun resumeCandidate(): RoutineId? = DhikrEngine.resumeCandidate(state, todayKey())

    fun entriesOf(routineId: RoutineId): List<EntryUiState> {
        return routineState(routineId)?.entries.orEmpty()
    }

    // ------------------------------------------------------------ mutations

    fun increment(key: RoutineKey) {
        commit(DhikrEngine.increment(state, key, todayKey(), clock.nowMillis()))
    }

    fun decrement(key: RoutineKey) {
        commit(DhikrEngine.decrement(state, key, todayKey()))
    }

    fun markRecited(key: RoutineKey) {
        commit(DhikrEngine.markRecited(state, key, todayKey(), clock.nowMillis()))
    }

    fun createPersonalRoutine(title: String): RoutineId? {
        val name = title.trim()
        if (name.isEmpty()) return null
        var id: RoutineId
        do {
            id = RoutineId.valueOf("PERSONAL_${Random.nextLong().toULong().toString(16)}")
        } while (state.personalRoutines.any { it.id == id })
        commit(state.copy(personalRoutines = state.personalRoutines + PersonalRoutine(id, name)))
        return id
    }

    fun renamePersonalRoutine(id: RoutineId, title: String) {
        val name = title.trim()
        if (name.isNotEmpty()) updatePersonalRoutine(id) { it.copy(title = name) }
    }

    fun deletePersonalRoutine(id: RoutineId): Boolean {
        if (!id.isPersonal || state.personalRoutines.none { it.id == id }) return false
        commit(state.copy(
            personalRoutines = state.personalRoutines.filterNot { it.id == id },
            progress = state.progress.filterNot { it.routineKey.routineId == id },
            targetOverrides = state.targetOverrides.filterKeys { !it.startsWith("${id.name}-") },
            lastOpenedRoutine = state.lastOpenedRoutine?.takeUnless { it == id },
        ))
        return true
    }

    fun savePersonalDhikr(id: RoutineId, order: Int?, arabic: String, pronunciation: String, meaning: String, target: Int) {
        if (target < 1 || listOf(arabic, pronunciation, meaning).all { it.isBlank() }) return
        updatePersonalRoutine(id) { routine ->
            if (order != null && routine.entries.none { it.order == order }) return@updatePersonalRoutine routine
            val entryOrder = order ?: ((routine.entries.maxOfOrNull { it.order } ?: 0) + 1)
            if (entryOrder <= 0) return@updatePersonalRoutine routine
            val entry = PersonalDhikr(entryOrder, arabic.trim(), pronunciation.trim(), meaning.trim(), target)
            val entries = if (order == null) routine.entries + entry
                else routine.entries.map { if (it.order == order) entry else it }
            routine.copy(entries = entries)
        }
    }

    fun removePersonalDhikr(id: RoutineId, order: Int) {
        if (!id.isPersonal || state.personalRoutines.none { it.id == id && it.entries.any { entry -> entry.order == order } }) return
        val key = RoutineKey(id, order)
        commit(state.copy(
            personalRoutines = state.personalRoutines.map { routine ->
                if (routine.id == id) routine.copy(entries = routine.entries.filterNot { it.order == order })
                else routine
            },
            progress = state.progress.filterNot { it.routineKey == key },
            targetOverrides = state.targetOverrides - key.toString(),
        ))
    }

    private fun updatePersonalRoutine(id: RoutineId, transform: (PersonalRoutine) -> PersonalRoutine) {
        if (!id.isPersonal) return
        val routines = state.personalRoutines.map { if (it.id == id) transform(it) else it }
        if (routines != state.personalRoutines) commit(state.copy(personalRoutines = routines))
    }

    fun setLastOpened(routineId: RoutineId) {
        commit(DhikrEngine.setLastOpened(state, routineId))
    }

    fun resetToday() {
        commit(DhikrEngine.resetDay(state, todayKey()))
    }

    fun updateSettings(transform: (Settings) -> Settings) {
        commit(state.copy(settings = transform(state.settings)))
    }

    // ------------------------------------------------------ backup & restore

    fun export(): String = SavedStateCodec.encode(state)

    /** Restores from exported text; returns false when the text is not valid state. */
    fun restore(text: String): Boolean {
        val decoded = SavedStateCodec.decode(text) ?: return false
        commit(decoded.withIndependentTahlilProgress().withSplitIstighfarProgress())
        return true
    }
}

/** Move each prayer's recorded share of the old goal to its own progress row. */
private fun SavedState.withIndependentTahlilProgress(): SavedState {
    val legacyGoals = goals.filter { it.goalId == "goal-la-ilaha-illallah" }
    if (legacyGoals.isEmpty() && !settings.sharedGoalEnabled) return this

    val fajr = RoutineKey(RoutineId.FAJR, 24)
    val asr = RoutineKey(RoutineId.ASR, 24)
    val rows = progress.associateByTo(mutableMapOf()) { it.dayKey to it.routineKey }
    for (goal in legacyGoals) {
        val total = goal.completedCount.coerceAtLeast(0)
        var fajrCount = (goal.contributions[fajr.toString()] ?: 0).coerceIn(0, total)
        var asrCount = (goal.contributions[asr.toString()] ?: 0).coerceIn(0, total - fajrCount)
        val unattributed = total - fajrCount - asrCount
        if (goal.satisfiedBy == asr) asrCount += unattributed else fajrCount += unattributed
        for ((key, count) in listOf(fajr to fajrCount, asr to asrCount)) {
            if (count == 0) continue
            val rowKey = goal.dayKey to key
            val previous = rows[rowKey]
            val combined = ((previous?.completedCount ?: 0).toLong() + count).coerceAtMost(Int.MAX_VALUE.toLong())
            rows[rowKey] = DhikrProgress(goal.dayKey, key, Routines.TAHLIL_ID, combined.toInt(), previous?.completedAt)
        }
    }
    return copy(
        settings = settings.copy(sharedGoalEnabled = false),
        progress = rows.values.toList(),
        goals = goals - legacyGoals.toSet(),
    )
}

/** Credit the third repetition already recorded by older versions to the new slot. */
private fun SavedState.withSplitIstighfarProgress(): SavedState {
    val splitRows = progress.filter {
        val key = it.routineKey
        (key.routineId == RoutineId.FAJR || key.routineId == RoutineId.ASR) &&
            key.order == 2 && it.dhikrId == "astaghfirullah" && it.completedCount >= 3
    }
    if (splitRows.isEmpty()) return this

    val rows = progress.associateByTo(linkedMapOf()) { it.dayKey to it.routineKey }
    for (row in splitRows) {
        val thirdKey = RoutineKey(row.routineKey.routineId, 30)
        rows[row.dayKey to row.routineKey] = row.copy(completedCount = 2)
        rows.getOrPut(row.dayKey to thirdKey) {
            row.copy(routineKey = thirdKey, dhikrId = "astaghfirullah-rabbi", completedCount = 1)
        }
    }
    return copy(progress = rows.values.toList())
}
