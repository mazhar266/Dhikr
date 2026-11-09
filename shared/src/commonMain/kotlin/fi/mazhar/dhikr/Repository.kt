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
        state = store.load() ?: SavedState()
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
        val settings = transform(state.settings)
        val goals = if (settings.laIlaTarget100 != state.settings.laIlaTarget100) {
            val day = todayKey()
            val target = DhikrEngine.laIlaTarget(settings)
            state.goals.map { goal ->
                if (goal.dayKey == day && goal.goalId == Routines.LA_ILA_GOAL_ID) {
                    goal.copy(
                        target = target,
                        satisfiedBy = goal.satisfiedBy?.takeIf { goal.completedCount >= target },
                    )
                } else goal
            }
        } else state.goals
        commit(state.copy(settings = settings, goals = goals))
    }

    // ------------------------------------------------------ backup & restore

    fun export(): String = SavedStateCodec.encode(state)

    /** Restores from exported text; returns false when the text is not valid state. */
    fun restore(text: String): Boolean {
        val decoded = SavedStateCodec.decode(text) ?: return false
        commit(decoded)
        return true
    }
}
