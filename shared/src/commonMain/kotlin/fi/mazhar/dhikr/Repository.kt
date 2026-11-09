package fi.mazhar.dhikr

import fi.mazhar.dhikr.content.Routines

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
        val routine = fi.mazhar.dhikr.content.Routines.byId(routineId) ?: return emptyList()
        return DhikrEngine.entryState(state, routine, todayKey())
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

    fun setTarget(key: RoutineKey, target: Int) {
        commit(DhikrEngine.setTarget(state, key, target))
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
