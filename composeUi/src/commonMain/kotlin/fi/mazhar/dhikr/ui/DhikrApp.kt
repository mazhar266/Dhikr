package fi.mazhar.dhikr.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fi.mazhar.dhikr.titleFor

@Composable
fun DhikrApp(model: AppModel) {
    DhikrAppTheme(model.state.settings) {
        val text = uiText(model.state.settings.translationLanguage)
        val showBottomBar = when (model.screen) {
            is Screen.Home, is Screen.Summary, is Screen.Settings -> true
            else -> false
        }

        Scaffold(
            topBar = { topBar(model, text) },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = model.screen is Screen.Home,
                            onClick = { model.goHome() },
                            icon = { Icon(Icons.Filled.Home, contentDescription = text.home) },
                            label = { Text(text.today) },
                        )
                        NavigationBarItem(
                            selected = model.screen is Screen.Summary,
                            onClick = { model.open(Screen.Summary) },
                            icon = { Icon(Icons.Filled.BarChart, contentDescription = text.summary) },
                            label = { Text(text.summary) },
                        )
                        NavigationBarItem(
                            selected = model.screen is Screen.Settings,
                            onClick = { model.open(Screen.Settings) },
                            icon = { Icon(Icons.Filled.Settings, contentDescription = text.settings) },
                            label = { Text(text.settings) },
                        )
                    }
                }
            },
        ) { padding ->
            val contentModifier = Modifier.padding(padding)
            when (val s = model.screen) {
                is Screen.Home -> HomeScreen(model, contentModifier)
                is Screen.Summary -> SummaryScreen(model, contentModifier)
                is Screen.Settings -> SettingsScreen(model, contentModifier)
                is Screen.Routine -> RoutineScreen(model, s.id, contentModifier)
                is Screen.Reader -> ReaderScreen(model, s.id, s.order, contentModifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topBar(model: AppModel, text: UiText) {
    val language = model.state.settings.translationLanguage
    val title: String = when (val s = model.screen) {
        is Screen.Home -> "Dhikr"
        is Screen.Summary -> text.dailySummary
        is Screen.Settings -> text.settings
        is Screen.Routine -> model.routineState(s.id)?.title ?: s.id.name
        is Screen.Reader -> model.entry(s.id, s.order)?.dhikr?.titleFor(language) ?: ""
    }
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (model.screen !is Screen.Home || model.canGoBack()) {
                IconButton(onClick = { model.back() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = text.back)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}
