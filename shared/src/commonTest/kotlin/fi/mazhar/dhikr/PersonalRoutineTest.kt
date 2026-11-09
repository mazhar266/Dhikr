package fi.mazhar.dhikr

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PersonalRoutineTest {
    @Test
    fun partialEntriesAndProgressSurviveBackupAndEditing() {
        val clock = FixedClock("2026-11-09")
        val repo = DhikrRepository(InMemoryDhikrStore(), clock)
        val id = assertNotNull(repo.createPersonalRoutine("My | Fajr"))
        repo.savePersonalDhikr(id, null, "سُبْحَانَ اللَّهِ", "", "", 3)
        repo.savePersonalDhikr(id, null, "", "Astaghfirullāh", "", 1)
        repo.savePersonalDhikr(id, null, "", "", "Praise | 100%\nSecond line", 2)
        repo.savePersonalDhikr(id, null, " ", "", "", 1)
        assertEquals(3, repo.entriesOf(id).size)

        val first = repo.entriesOf(id).first()
        repo.increment(first.key)
        repo.savePersonalDhikr(id, first.key.order, first.dhikr.arabic, "", "", 4)
        repo.setLastOpened(id)

        val restored = DhikrRepository(InMemoryDhikrStore(), clock)
        assertTrue(restored.restore(repo.export()))
        assertEquals(repo.state, restored.state)
        assertEquals("My | Fajr", restored.routineState(id)?.title)
        assertEquals(1, restored.entriesOf(id).first().count)
        assertEquals(4, restored.entriesOf(id).first().target)
        assertEquals("Astaghfirullāh", restored.entriesOf(id)[1].dhikr.transliteration)
        assertEquals("Praise | 100%\nSecond line", restored.entriesOf(id)[2].dhikr.translation)

        restored.removePersonalDhikr(id, first.key.order)
        assertEquals(2, restored.entriesOf(id).size)
        assertTrue(restored.state.progress.isEmpty())
    }

    @Test
    fun deletingAListRemovesOnlyItsProgressAndResumeState() {
        val store = InMemoryDhikrStore()
        val clock = FixedClock("2026-11-09")
        val repo = DhikrRepository(store, clock)
        val deleted = assertNotNull(repo.createPersonalRoutine("My Fajr"))
        val kept = assertNotNull(repo.createPersonalRoutine("My Asr"))
        for (id in listOf(deleted, kept)) {
            repo.savePersonalDhikr(id, null, "", "Astaghfirullāh", "", 3)
            repo.increment(repo.entriesOf(id).single().key)
        }
        repo.setLastOpened(deleted)

        assertFalse(repo.deletePersonalRoutine(RoutineId.FAJR))
        assertTrue(repo.deletePersonalRoutine(deleted))
        assertFalse(repo.deletePersonalRoutine(deleted))

        val reloaded = DhikrRepository(store, clock).apply { load() }
        assertNull(reloaded.routineState(deleted))
        assertNull(reloaded.state.lastOpenedRoutine)
        assertEquals(listOf(kept), reloaded.state.personalRoutines.map { it.id })
        assertEquals(1, reloaded.entriesOf(kept).single().count)
        assertEquals(listOf(kept), reloaded.state.progress.map { it.routineKey.routineId })
    }

    @Test
    fun personalEditorCannotModifyBuiltInRoutines() {
        val repo = DhikrRepository(InMemoryDhikrStore(), FixedClock("2026-11-09"))
        val before = repo.routineState(RoutineId.FAJR)
        val id = assertNotNull(repo.createPersonalRoutine("Fajr"))
        repo.renamePersonalRoutine(RoutineId.FAJR, "My Fajr")
        repo.savePersonalDhikr(RoutineId.FAJR, 1, "", "My own wording", "", 7)
        repo.removePersonalDhikr(RoutineId.FAJR, 1)
        assertEquals(before, repo.routineState(RoutineId.FAJR))
        assertEquals(1, repo.state.personalRoutines.size)
        assertEquals("Fajr", repo.routineState(id)?.title)
    }
}
