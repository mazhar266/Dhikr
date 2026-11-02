package fi.mazhar.dhikr

import fi.mazhar.dhikr.content.DhikrCatalog
import fi.mazhar.dhikr.content.Routines
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EngineTest {

    private val day = "2026-11-02"

    private fun fajrKey(order: Int) = RoutineKey(RoutineId.FAJR, order)
    private fun asrKey(order: Int) = RoutineKey(RoutineId.ASR, order)

    // ------------------------------------------------------ increment / decrement

    @Test
    fun increment_countsUpToTarget() {
        var state = SavedState()
        repeat(33) { state = DhikrEngine.increment(state, fajrKey(12), day, 0L) }
        val entry = entryState(state, RoutineId.FAJR, 12)
        assertEquals(33, entry.count)
        assertTrue(entry.complete)
    }

    @Test
    fun increment_cannotExceedTarget() {
        var state = SavedState()
        repeat(35) { state = DhikrEngine.increment(state, fajrKey(12), day, 0L) }
        assertEquals(33, entryState(state, RoutineId.FAJR, 12).count)
    }

    @Test
    fun decrement_uncountsAndClearsCompletion() {
        var state = SavedState()
        repeat(33) { state = DhikrEngine.increment(state, fajrKey(12), day, 42L) }
        state = DhikrEngine.decrement(state, fajrKey(12), day)
        val entry = entryState(state, RoutineId.FAJR, 12)
        assertEquals(32, entry.count)
        assertFalse(entry.complete)
    }

    @Test
    fun decrement_cannotGoBelowZero() {
        var state = SavedState()
        state = DhikrEngine.decrement(state, fajrKey(12), day)
        assertEquals(0, entryState(state, RoutineId.FAJR, 12).count)
    }

    @Test
    fun markRecited_fillsReciteOnceEntry() {
        var state = DhikrEngine.markRecited(SavedState(), fajrKey(6), day, 42L)
        val entry = entryState(state, RoutineId.FAJR, 6)
        assertEquals(1, entry.count)
        assertTrue(entry.complete)
        // Idempotent
        state = DhikrEngine.markRecited(state, fajrKey(6), day, 43L)
        assertEquals(1, entryState(state, RoutineId.FAJR, 6).count)
    }

    // ------------------------------------------------------- routine completion

    @Test
    fun routine_completesOnlyWhenAllEntriesDone() {
        var state = SavedState()
        val routine = assertNotNull(Routines.byId(RoutineId.DHUHR))
        // Complete every entry except the last (10x salawat).
        for (e in routine.entries.dropLast(1)) {
            repeat(DhikrEngine.targetFor(e, state)) {
                state = DhikrEngine.increment(state, e.key, day, 0L)
            }
        }
        val before = DhikrEngine.routineState(state, RoutineId.DHUHR, day)
        assertTrue(before != null && !before.complete)
        assertEquals(4, before.completedEntries)

        val last = routine.entries.last()
        repeat(DhikrEngine.targetFor(last, state)) {
            state = DhikrEngine.increment(state, last.key, day, 0L)
        }
        assertTrue(DhikrEngine.routineState(state, RoutineId.DHUHR, day)!!.complete)
    }

    @Test
    fun routines_areIsolatedFromEachOther() {
        // Same dhikr (subhanallah) appears in Fajr and Dhuhr with different keys.
        var state = DhikrEngine.increment(SavedState(), fajrKey(12), day, 0L)
        state = DhikrEngine.increment(state, RoutineKey(RoutineId.DHUHR, 2), day, 0L)

        assertEquals(1, entryState(state, RoutineId.FAJR, 12).count)
        assertEquals(1, entryState(state, RoutineId.DHUHR, 2).count)
        assertEquals(0, entryState(state, RoutineId.ASR, 12).count)
        assertEquals(0, entryState(state, RoutineId.MAGHRIB, 2).count)
    }

    @Test
    fun targetOverrides_applyPerEntry() {
        var state = DhikrEngine.setTarget(SavedState(), fajrKey(12), 100)
        state = DhikrEngine.increment(state, fajrKey(12), day, 0L)
        val entry = entryState(state, RoutineId.FAJR, 12)
        assertEquals(100, entry.target)
        assertEquals(1, entry.count)
        // Other entries unaffected.
        assertEquals(33, entryState(state, RoutineId.FAJR, 13).target)
    }

    @Test
    fun laIlaTarget_followsSetting() {
        var state = SavedState()
        assertEquals(10, entryState(state, RoutineId.FAJR, 24).target)
        state = state.copy(settings = state.settings.copy(laIlaTarget100 = true))
        assertEquals(100, entryState(state, RoutineId.FAJR, 24).target)
    }

    // ---------------------------------------------------------------- shared goal

    @Test
    fun sharedGoal_off_fajrEntryIndependent_asrEntryRemoved() {
        val state = SavedState() // sharedGoalEnabled = false by default
        val fajr = assertNotNull(DhikrEngine.routineState(state, RoutineId.FAJR, day))
        assertEquals(29, fajr.entries.size)
        assertFalse(fajr.entries[23].viaSharedGoal)

        val asr = assertNotNull(DhikrEngine.routineState(state, RoutineId.ASR, day))
        assertEquals(27, asr.entries.size)
        assertNull(asr.entries.firstOrNull { it.dhikr.id == "la-ilaha-illallah-goal" })
    }

    @Test
    fun sharedGoal_on_bothRoutinesMirrorTheGoal() {
        var state = SavedState().copy(settings = Settings(sharedGoalEnabled = true))
        assertEquals(29, DhikrEngine.routineState(state, RoutineId.FAJR, day)!!.entries.size)
        assertEquals(28, DhikrEngine.routineState(state, RoutineId.ASR, day)!!.entries.size)

        repeat(4) { state = DhikrEngine.increment(state, fajrKey(24), day, 0L) }
        // Fajr shows 4/10, Asr mirror also shows 4/10 (shared view).
        assertEquals(4, entryState(state, RoutineId.FAJR, 24).count)
        assertEquals(4, entryState(state, RoutineId.ASR, 24).count)

        // Asr contributes toward the same goal.
        repeat(6) { state = DhikrEngine.increment(state, asrKey(24), day, 0L) }
        assertTrue(entryState(state, RoutineId.FAJR, 24).complete)
        assertTrue(entryState(state, RoutineId.ASR, 24).complete)
        assertEquals(10, DhikrEngine.goalFor(state, Routines.LA_ILA_GOAL_ID, day)!!.completedCount)
    }

    @Test
    fun sharedGoal_asrCannotUndoFajrContributions() {
        var state = SavedState().copy(settings = Settings(sharedGoalEnabled = true))
        repeat(10) { state = DhikrEngine.increment(state, fajrKey(24), day, 0L) }
        assertTrue(entryState(state, RoutineId.FAJR, 24).complete)

        // Undo from Asr: no Fajr contributions there — must not decrement anything.
        state = DhikrEngine.decrement(state, asrKey(24), day)
        assertEquals(10, DhikrEngine.goalFor(state, Routines.LA_ILA_GOAL_ID, day)!!.completedCount)
        assertTrue(entryState(state, RoutineId.FAJR, 24).complete)
        assertTrue(entryState(state, RoutineId.ASR, 24).complete)
    }

    @Test
    fun sharedGoal_asrDoesNotDuplicateCompletedGoal() {
        var state = SavedState().copy(settings = Settings(sharedGoalEnabled = true))
        repeat(10) { state = DhikrEngine.increment(state, fajrKey(24), day, 0L) }
        // Incrementing further from Asr does not overcount the goal.
        repeat(5) { state = DhikrEngine.increment(state, asrKey(24), day, 0L) }
        assertEquals(10, DhikrEngine.goalFor(state, Routines.LA_ILA_GOAL_ID, day)!!.completedCount)
    }

    @Test
    fun sharedGoal_undoOnlyOwnContributions() {
        var state = SavedState().copy(settings = Settings(sharedGoalEnabled = true))
        repeat(4) { state = DhikrEngine.increment(state, fajrKey(24), day, 0L) }
        repeat(6) { state = DhikrEngine.increment(state, asrKey(24), day, 0L) } // goal complete
        state = DhikrEngine.decrement(state, asrKey(24), day) // undo one Asr tap
        assertEquals(9, DhikrEngine.goalFor(state, Routines.LA_ILA_GOAL_ID, day)!!.completedCount)
        assertFalse(entryState(state, RoutineId.FAJR, 24).complete)
        state = DhikrEngine.decrement(state, fajrKey(24), day)
        assertEquals(8, DhikrEngine.goalFor(state, Routines.LA_ILA_GOAL_ID, day)!!.completedCount)
    }

    // ------------------------------------------------------------------ day logic

    @Test
    fun dayRollover_resetsCountsAndKeepsHistory() {
        var state = DhikrEngine.increment(SavedState(), fajrKey(12), day, 0L)
        val nextDay = DayKeys.addDays(day, 1)

        val today = DhikrEngine.routineState(state, RoutineId.FAJR, day)!!
        val tomorrow = DhikrEngine.routineState(state, RoutineId.FAJR, nextDay)!!
        assertEquals(1, today.entries[11].count)
        assertEquals(0, tomorrow.entries[11].count)

        // History retained.
        assertEquals(1, state.progress.count { it.dayKey == day })
    }

    @Test
    fun totalRepetitions_countSharedGoalOnce() {
        var state = SavedState().copy(settings = Settings(sharedGoalEnabled = true))
        repeat(3) { state = DhikrEngine.increment(state, fajrKey(24), day, 0L) }
        state = DhikrEngine.increment(state, fajrKey(12), day, 0L)
        assertEquals(4, DhikrEngine.totalRepetitions(state, day))
    }

    @Test
    fun resetDay_clearsOnlyThatDay() {
        var state = DhikrEngine.increment(SavedState(), fajrKey(12), day, 0L)
        val nextDay = DayKeys.addDays(day, 1)
        state = DhikrEngine.increment(state, fajrKey(12), nextDay, 0L)
        state = DhikrEngine.resetDay(state, day)
        assertEquals(0, DhikrEngine.totalRepetitions(state, day))
        assertEquals(1, DhikrEngine.totalRepetitions(state, nextDay))
    }

    @Test
    fun streak_countsConsecutiveCompleteDays() {
        var state = SavedState()
        // Complete Dhuhr on three consecutive days, then leave today incomplete.
        for (i in 3 downTo 1) {
            val d = DayKeys.addDays(day, -i.toLong())
            var s = SavedState()
            val routine = Routines.byId(RoutineId.DHUHR)!!
            for (e in routine.entries) {
                repeat(DhikrEngine.targetFor(e, s)) { s = DhikrEngine.increment(s, e.key, d, 0L) }
            }
            state = state.copy(
                progress = state.progress + s.progress,
                goals = state.goals + s.goals,
            )
        }
        assertEquals(3, DhikrEngine.streak(state, day))
        assertTrue(!DhikrEngine.todayHasCompletedRoutine(state, day))
    }

    @Test
    fun streak_todayComplete_included() {
        val d = day
        var s = SavedState()
        val routine = Routines.byId(RoutineId.DHUHR)!!
        for (e in routine.entries) {
            repeat(DhikrEngine.targetFor(e, s)) { s = DhikrEngine.increment(s, e.key, d, 0L) }
        }
        assertEquals(1, DhikrEngine.streak(s, d))
    }

    @Test
    fun streak_emptyState_isZero() {
        assertEquals(0, DhikrEngine.streak(SavedState(), day))
    }

    // ------------------------------------------------------------------- resume

    @Test
    fun resumeCandidate_prefersLastOpenedIncompleteRoutine() {
        var state = SavedState()
        assertEquals(RoutineId.FAJR, DhikrEngine.resumeCandidate(state, day))

        state = DhikrEngine.setLastOpened(state, RoutineId.BEFORE_SLEEP)
        assertEquals(RoutineId.BEFORE_SLEEP, DhikrEngine.resumeCandidate(state, day))

        // Last opened is complete → falls back to first incomplete.
        var s2 = SavedState()
        val sleep = Routines.byId(RoutineId.BEFORE_SLEEP)!!
        for (e in sleep.entries) {
            if (e.kindReciteOnce()) {
                s2 = DhikrEngine.markRecited(s2, e.key, day, 0L)
            } else {
                repeat(DhikrEngine.targetFor(e, s2)) { s2 = DhikrEngine.increment(s2, e.key, day, 0L) }
            }
        }
        s2 = DhikrEngine.setLastOpened(s2, RoutineId.BEFORE_SLEEP)
        assertEquals(RoutineId.FAJR, DhikrEngine.resumeCandidate(s2, day))
    }

    // ------------------------------------------------------------------ helpers

    private fun entryState(state: SavedState, routineId: RoutineId, order: Int): EntryUiState {
        val routine = assertNotNull(Routines.byId(routineId))
        return assertNotNull(
            DhikrEngine.entryState(state, routine, day).firstOrNull { it.key.order == order },
            "Entry $routineId-$order not found",
        )
    }

    private fun RoutineEntry.kindReciteOnce(): Boolean =
        DhikrCatalog.byId(dhikrId)?.kind == DhikrKind.RECITE_ONCE
}
