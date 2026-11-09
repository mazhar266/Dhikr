package fi.mazhar.dhikr

import fi.mazhar.dhikr.content.DhikrCatalog
import fi.mazhar.dhikr.content.RoutineDefinition
import fi.mazhar.dhikr.content.Routines

/** UI-facing state of one routine entry for a given day. */
data class EntryUiState(
    val key: RoutineKey,
    val dhikr: Dhikr,
    /** 1-based position inside the routine. */
    val position: Int,
    val count: Int,
    val target: Int,
    val complete: Boolean,
    /** True when this entry is satisfied through a shared daily goal. */
    val viaSharedGoal: Boolean,
    /** True when the entry only exists conditionally (Asr mirror of the shared goal). */
    val conditional: Boolean,
)

/** UI-facing state of a whole routine for a given day. */
data class RoutineUiState(
    val id: RoutineId,
    val title: String,
    val arabicTitle: String,
    val description: String,
    val entries: List<EntryUiState>,
) {
    val totalEntries: Int get() = entries.size
    val completedEntries: Int get() = entries.count { it.complete }
    val percent: Float get() = if (totalEntries == 0) 0f else completedEntries.toFloat() / totalEntries
    val complete: Boolean get() = totalEntries > 0 && completedEntries == totalEntries
}

/** Whole-day summary across all routines. */
data class DaySummary(
    val dayKey: String,
    val routines: List<RoutineUiState>,
    val totalRepetitions: Int,
    val completedEntries: Int,
    val totalEntries: Int,
) {
    val overallPercent: Float get() = if (totalEntries == 0) 0f else completedEntries.toFloat() / totalEntries
    val allComplete: Boolean get() = routines.isNotEmpty() && routines.all { it.complete }
    val unfinishedEntries: List<EntryUiState> get() = routines.flatMap { r -> r.entries.filter { !it.complete } }
}

/**
 * Pure state machine over [SavedState]. All functions return new state; the
 * repository persists results. One routine's rows are keyed by
 * [RoutineKey.toString], so counts can never merge across routines — except
 * through the explicitly opted-in shared daily goal.
 */
object DhikrEngine {

    private fun routineDefinition(state: SavedState, id: RoutineId): RoutineDefinition? =
        Routines.byId(id) ?: state.personalRoutines.firstOrNull { it.id == id }?.definition()

    private fun routineIds(state: SavedState): List<RoutineId> =
        Routines.all.map { it.id } + state.personalRoutines.map { it.id }

    // ------------------------------------------------------------ entry view

    /**
     * The routine's entries as the user configured them, with shared-goal
     * wiring applied:
     *  - shared mode OFF: the Fajr "La ilaha illallah" entry becomes an
     *    independent entry; the Asr conditional mirror is removed.
     *  - shared mode ON: both entries follow the same [SharedDailyGoal].
     */
    fun effectiveEntries(routine: RoutineDefinition, settings: Settings): List<RoutineEntry> =
        routine.entries.mapNotNull { entry ->
            when {
                entry.sharedGoalId == null -> entry
                settings.sharedGoalEnabled -> entry
                entry.conditionalOnGoal -> null
                else -> entry.copy(sharedGoalId = null)
            }
        }

    fun targetFor(entry: RoutineEntry, state: SavedState): Int {
        if (entry.key.routineId.isPersonal) return entry.target
        if (entry.dhikrId == "la-ilaha-illallah-goal") {
            return laIlaTarget(state.settings)
        }
        state.targetOverrides[entry.key.toString()]?.let { return it }
        return entry.target
    }

    fun laIlaTarget(settings: Settings): Int = if (settings.laIlaTarget100) 100 else 10

    fun progressFor(state: SavedState, key: RoutineKey, dayKey: String): DhikrProgress? =
        state.progress.firstOrNull { it.dayKey == dayKey && it.routineKey == key }

    fun goalFor(state: SavedState, goalId: String, dayKey: String): SharedDailyGoal? =
        state.goals.firstOrNull { it.goalId == goalId && it.dayKey == dayKey }

    fun entryState(state: SavedState, routine: RoutineDefinition, dayKey: String): List<EntryUiState> {
        val settings = state.settings
        val personalDhikrs = state.personalRoutines.firstOrNull { it.id == routine.id }
            ?.entries?.associateBy { it.id }.orEmpty()
        return effectiveEntries(routine, settings).mapIndexed { index, entry ->
            val dhikr = DhikrCatalog.byId(entry.dhikrId)
                ?: personalDhikrs[entry.dhikrId]?.asDhikr()
                ?: error("Catalog missing dhikr '${entry.dhikrId}' (entry ${entry.key})")
            val goal = entry.sharedGoalId?.let { goalFor(state, it, dayKey) }
            val goalTarget = goal?.target ?: targetFor(entry, state)
            val cappedContribution = entry.conditionalOnGoal && goalTarget > entry.target
            val target = if (cappedContribution) entry.target else goalTarget
            val count = if (cappedContribution) {
                if (goal != null && goal.completedCount >= goal.target) target
                else goal?.contributions?.get(entry.key.toString())?.coerceAtMost(target) ?: 0
            } else {
                goal?.completedCount ?: progressFor(state, entry.key, dayKey)?.completedCount ?: 0
            }
            EntryUiState(
                key = entry.key,
                dhikr = dhikr,
                position = index + 1,
                count = count,
                target = target,
                complete = count >= target,
                viaSharedGoal = goal != null,
                conditional = entry.conditionalOnGoal,
            )
        }
    }

    fun routineState(state: SavedState, routineId: RoutineId, dayKey: String): RoutineUiState? {
        val routine = routineDefinition(state, routineId) ?: return null
        val language = state.settings.translationLanguage
        return RoutineUiState(
            id = routine.id,
            title = routine.titleFor(language),
            arabicTitle = routine.arabicTitle,
            description = routine.descriptionFor(language),
            entries = entryState(state, routine, dayKey),
        )
    }

    fun daySummary(state: SavedState, dayKey: String): DaySummary {
        val routines = routineIds(state).mapNotNull { routineState(state, it, dayKey) }
        return DaySummary(
            dayKey = dayKey,
            routines = routines,
            totalRepetitions = totalRepetitions(state, dayKey),
            completedEntries = routines.sumOf { it.completedEntries },
            totalEntries = routines.sumOf { it.totalEntries },
        )
    }

    /** Sum of completed repetitions across all entries for a day (shared goal counted once). */
    fun totalRepetitions(state: SavedState, dayKey: String): Int {
        val goalTotal = state.goals
            .filter { it.dayKey == dayKey }
            .sumOf { it.completedCount }
        val plainTotal = state.progress
            .filter { it.dayKey == dayKey }
            .filter { row -> row.routineKey !in sharedGoalKeys(state) }
            .sumOf { it.completedCount }
        return goalTotal + plainTotal
    }

    private fun sharedGoalKeys(state: SavedState): Set<RoutineKey> {
        if (!state.settings.sharedGoalEnabled) return emptySet()
        val keys = HashSet<RoutineKey>()
        for (routine in Routines.all) {
            for (entry in effectiveEntries(routine, state.settings)) {
                if (entry.sharedGoalId != null) keys.add(entry.key)
            }
        }
        return keys
    }

    // -------------------------------------------------------------- mutations

    fun increment(state: SavedState, key: RoutineKey, dayKey: String, nowMillis: Long): SavedState {
        val routine = routineDefinition(state, key.routineId) ?: return state
        val entry = effectiveEntries(routine, state.settings)
            .firstOrNull { it.key == key } ?: return state

        return if (entry.sharedGoalId != null) {
            incrementGoal(state, entry, dayKey)
        } else {
            val existing = progressFor(state, key, dayKey)
            val current = existing?.completedCount ?: 0
            val target = targetFor(entry, state)
            if (current >= target) return state
            val newCount = current + 1
            val row = DhikrProgress(
                dayKey = dayKey,
                routineKey = key,
                dhikrId = entry.dhikrId,
                completedCount = newCount,
                completedAt = if (newCount >= target) nowMillis else existing?.completedAt,
            )
            state.copy(progress = state.progress.filterNot { it == existing } + row)
        }
    }

    private fun incrementGoal(state: SavedState, entry: RoutineEntry, dayKey: String): SavedState {
        val goalId = entry.sharedGoalId ?: return state
        val existing = goalFor(state, goalId, dayKey)
        val goal = existing ?: SharedDailyGoal(
            goalId = goalId,
            dayKey = dayKey,
            target = targetFor(entry, state),
            applicableRoutineIds = Routines.routinesForGoal(goalId),
        )
        if (remainingGoalContribution(entry, goal) <= 0) return state

        val keyStr = entry.key.toString()
        val updated = goal.copy(
            completedCount = goal.completedCount + 1,
            satisfiedBy = if (goal.completedCount + 1 >= goal.target) entry.key else goal.satisfiedBy,
            contributions = goal.contributions + (keyStr to (goal.contributions[keyStr] ?: 0) + 1),
        )
        return state.copy(goals = state.goals.filterNot { it == existing } + updated)
    }

    fun decrement(state: SavedState, key: RoutineKey, dayKey: String): SavedState {
        val routine = routineDefinition(state, key.routineId) ?: return state
        val entry = effectiveEntries(routine, state.settings)
            .firstOrNull { it.key == key } ?: return state

        return if (entry.sharedGoalId != null) {
            val goal = goalFor(state, entry.sharedGoalId, dayKey) ?: return state
            val keyStr = key.toString()
            val contributed = goal.contributions[keyStr] ?: 0
            if (contributed <= 0 || goal.completedCount <= 0) return state
            val newCount = goal.completedCount - 1
            val updated = goal.copy(
                completedCount = newCount,
                satisfiedBy = if (newCount >= goal.target) goal.satisfiedBy else null,
                contributions = if (contributed == 1) {
                    goal.contributions - keyStr
                } else {
                    goal.contributions + (keyStr to contributed - 1)
                },
            )
            state.copy(goals = state.goals.filterNot { it == goal } + updated)
        } else {
            val existing = progressFor(state, key, dayKey) ?: return state
            if (existing.completedCount <= 0) return state
            val newCount = existing.completedCount - 1
            val target = targetFor(entry, state)
            val row = existing.copy(
                completedCount = newCount,
                completedAt = if (newCount >= target) existing.completedAt else null,
            )
            state.copy(progress = state.progress.filterNot { it == existing } + row)
        }
    }

    /** "Mark as Recited": fill a one-time recitation entry to its target. */
    fun markRecited(state: SavedState, key: RoutineKey, dayKey: String, nowMillis: Long): SavedState {
        val routine = routineDefinition(state, key.routineId) ?: return state
        val entry = effectiveEntries(routine, state.settings)
            .firstOrNull { it.key == key } ?: return state

        return if (entry.sharedGoalId != null) {
            val goalId = entry.sharedGoalId
            val existing = goalFor(state, goalId, dayKey)
            val goal = existing ?: SharedDailyGoal(
                goalId = goalId,
                dayKey = dayKey,
                target = targetFor(entry, state),
                applicableRoutineIds = Routines.routinesForGoal(goalId),
            )
            val delta = remainingGoalContribution(entry, goal)
            if (delta <= 0) return state
            val keyStr = key.toString()
            val newCount = goal.completedCount + delta
            val updated = goal.copy(
                completedCount = newCount,
                satisfiedBy = if (newCount >= goal.target) entry.key else goal.satisfiedBy,
                contributions = goal.contributions + (keyStr to (goal.contributions[keyStr] ?: 0) + delta),
            )
            state.copy(goals = state.goals.filterNot { it == existing } + updated)
        } else {
            val target = targetFor(entry, state)
            val existing = progressFor(state, key, dayKey)
            if ((existing?.completedCount ?: 0) >= target) return state
            val row = DhikrProgress(
                dayKey = dayKey,
                routineKey = key,
                dhikrId = entry.dhikrId,
                completedCount = target,
                completedAt = nowMillis,
            )
            state.copy(progress = state.progress.filterNot { it == existing } + row)
        }
    }

    private fun remainingGoalContribution(entry: RoutineEntry, goal: SharedDailyGoal): Int {
        val remaining = goal.target - goal.completedCount
        if (!entry.conditionalOnGoal) return remaining
        val contributed = goal.contributions[entry.key.toString()] ?: 0
        return minOf(remaining, entry.target - contributed)
    }

    fun setTarget(state: SavedState, key: RoutineKey, target: Int): SavedState {
        if (target < 1) return state
        return state.copy(targetOverrides = state.targetOverrides + (key.toString() to target))
    }

    fun setLastOpened(state: SavedState, routineId: RoutineId): SavedState =
        state.copy(lastOpenedRoutine = routineId)

    /** Clears a day's progress and shared goals; other days are untouched. */
    fun resetDay(state: SavedState, dayKey: String): SavedState = state.copy(
        progress = state.progress.filterNot { it.dayKey == dayKey },
        goals = state.goals.filterNot { it.dayKey == dayKey },
    )

    // ----------------------------------------------------------- daily logic

    /**
     * Consecutive-day streak of fully completing at least one routine, ending
     * today (or yesterday, when today is still in progress). Days with no
     * recorded progress break the streak.
     */
    fun streak(state: SavedState, todayKey: String): Int {
        if (!DayKeys.isValid(todayKey)) return 0
        val completeDays = daysWithCompletedRoutine(state)
        var cursor = todayKey
        if (cursor !in completeDays) cursor = DayKeys.addDays(cursor, -1)
        var streak = 0
        while (cursor in completeDays) {
            streak++
            cursor = DayKeys.addDays(cursor, -1)
        }
        return streak
    }

    /** True when at least one routine is fully complete on today's key. */
    fun todayHasCompletedRoutine(state: SavedState, todayKey: String): Boolean =
        todayKey in daysWithCompletedRoutine(state)

    private fun daysWithCompletedRoutine(state: SavedState): Set<String> {
        val dayKeys = state.progress.map { it.dayKey }.toSet() + state.goals.map { it.dayKey }
        return dayKeys.filterTo(HashSet()) { day ->
            routineIds(state).any { id ->
                routineState(state, id, day)?.complete == true
            }
        }
    }

    /**
     * The routine to highlight for "resume": the last opened routine if it is
     * still incomplete today, otherwise the first incomplete routine in the
     * fixed display order. Null when everything is done.
     */
    fun resumeCandidate(state: SavedState, dayKey: String): RoutineId? {
        val summary = daySummary(state, dayKey)
        state.lastOpenedRoutine
            ?.let { last -> summary.routines.firstOrNull { it.id == last && it.entries.isNotEmpty() && !it.complete } }
            ?.let { return it.id }
        return summary.routines.firstOrNull { it.entries.isNotEmpty() && !it.complete }?.id
    }
}
