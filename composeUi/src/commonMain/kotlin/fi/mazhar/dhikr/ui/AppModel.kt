package fi.mazhar.dhikr.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fi.mazhar.dhikr.DhikrEngine
import fi.mazhar.dhikr.DhikrRepository
import fi.mazhar.dhikr.EntryUiState
import fi.mazhar.dhikr.RoutineId
import fi.mazhar.dhikr.RoutineKey
import fi.mazhar.dhikr.SavedState

sealed class Screen {
    data object Home : Screen()
    data object Summary : Screen()
    data object Settings : Screen()
    data class Routine(val id: RoutineId) : Screen()
    data class Reader(val id: RoutineId, val order: Int) : Screen()
}

/**
 * Observable bridge between the shared [DhikrRepository] and Compose. Every
 * mutation refreshes [state], which all screens read.
 */
class AppModel(val repo: DhikrRepository) {

    var state by mutableStateOf(repo.state)
        private set

    var screen by mutableStateOf<Screen>(Screen.Home)
        private set

    private val backStack = ArrayDeque<Screen>()

    // ----------------------------------------------------------- navigation

    fun open(next: Screen) {
        backStack.addLast(screen)
        screen = next
    }

    fun openRoutine(id: RoutineId) {
        repo.setLastOpened(id)
        state = repo.state
        open(Screen.Routine(id))
    }

    /** Prev/next inside the reader replaces the current reader screen. */
    fun openReader(id: RoutineId, order: Int) {
        if (screen is Screen.Reader) {
            screen = Screen.Reader(id, order)
        } else {
            open(Screen.Reader(id, order))
        }
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        screen = previous
        return true
    }

    fun canGoBack(): Boolean = backStack.isNotEmpty()

    fun goHome() {
        backStack.clear()
        screen = Screen.Home
    }

    // ------------------------------------------------------------- queries

    fun todayKey(): String = repo.todayKey()

    fun summary() = DhikrEngine.daySummary(readState(), todayKey())

    fun routineState(id: RoutineId) = DhikrEngine.routineState(readState(), id, todayKey())

    fun entriesOf(id: RoutineId): List<EntryUiState> = routineState(id)?.entries ?: emptyList()

    fun streak(): Int = DhikrEngine.streak(readState(), todayKey())

    fun todayComplete(): Boolean = DhikrEngine.todayHasCompletedRoutine(readState(), todayKey())

    fun resumeCandidate(): RoutineId? = DhikrEngine.resumeCandidate(readState(), todayKey())

    fun entry(id: RoutineId, order: Int): EntryUiState? =
        entriesOf(id).firstOrNull { it.key.order == order }

    fun historyComplete(days: List<String>): Map<String, Boolean> {
        val s = readState()
        return days.associateWith { DhikrEngine.todayHasCompletedRoutine(s, it) }
    }

    /** Reads [state] so Compose records the reactive dependency. */
    private fun readState(): SavedState {
        val s = state
        return s
    }

    // ------------------------------------------------------------ mutations

    fun increment(key: RoutineKey) {
        repo.increment(key)
        state = repo.state
    }

    fun decrement(key: RoutineKey) {
        repo.decrement(key)
        state = repo.state
    }

    fun markRecited(key: RoutineKey) {
        repo.markRecited(key)
        state = repo.state
    }

    fun updateSettings(transform: (fi.mazhar.dhikr.Settings) -> fi.mazhar.dhikr.Settings) {
        repo.updateSettings(transform)
        state = repo.state
    }

    fun createPersonalRoutine(title: String) {
        repo.createPersonalRoutine(title)?.let { openRoutine(it) }
    }

    fun renamePersonalRoutine(id: RoutineId, title: String) {
        repo.renamePersonalRoutine(id, title)
        state = repo.state
    }

    fun savePersonalDhikr(id: RoutineId, order: Int?, arabic: String, pronunciation: String, meaning: String, target: Int) {
        repo.savePersonalDhikr(id, order, arabic, pronunciation, meaning, target)
        state = repo.state
    }

    fun removePersonalDhikr(id: RoutineId, order: Int) {
        repo.removePersonalDhikr(id, order)
        state = repo.state
    }

    fun resetToday() {
        repo.resetToday()
        state = repo.state
    }

    fun export(): String = repo.export()

    fun restore(text: String): Boolean {
        val ok = repo.restore(text)
        state = repo.state
        return ok
    }
}
