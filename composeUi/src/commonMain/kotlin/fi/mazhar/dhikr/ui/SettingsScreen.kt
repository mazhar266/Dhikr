package fi.mazhar.dhikr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.mazhar.dhikr.AppTheme
import fi.mazhar.dhikr.TranslationLanguage
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(model: AppModel, modifier: Modifier = Modifier) {
    val settings = model.state.settings
    val text = uiText(settings.translationLanguage)
    val platform = LocalAppPlatform.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var confirmReset by remember { mutableStateOf(false) }

    fun toast(message: String) {
        scope.launch { snackbar.showSnackbar(message) }
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                SettingsCard(text.appearance) {
                    Text(text.theme, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        listOf(
                            AppTheme.SYSTEM to text.system,
                            AppTheme.LIGHT to text.light,
                            AppTheme.SOFIA_PAPER to text.sofiaPaper,
                            AppTheme.DARK to text.dark,
                            AppTheme.OLED to text.oledDark,
                        ).forEach { (theme, label) ->
                            FilterChip(
                                selected = settings.theme == theme,
                                onClick = { model.updateSettings { it.copy(theme = theme) } },
                                label = { Text(label) },
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text.arabicFontSize((settings.arabicFontScale * 100).toInt()),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Slider(
                        value = settings.arabicFontScale,
                        onValueChange = { scale ->
                            model.updateSettings {
                                it.copy(arabicFontScale = (scale * 10).toInt() / 10f)
                            }
                        },
                        valueRange = 0.8f..1.6f,
                        steps = 7,
                    )
                    Text(
                        "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        style = arabicTextStyle(settings.arabicFontScale).copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }

            item {
                SettingsCard(text.reading) {
                    SwitchRow(
                        text.showPronunciation,
                        settings.showTransliteration,
                    ) { model.updateSettings { s -> s.copy(showTransliteration = !s.showTransliteration) } }
                    SwitchRow(
                        text.showTranslation,
                        settings.showTranslation,
                    ) { model.updateSettings { s -> s.copy(showTranslation = !s.showTranslation) } }
                    Text(text.language, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = settings.translationLanguage == TranslationLanguage.ENGLISH,
                            onClick = {
                                model.updateSettings { it.copy(translationLanguage = TranslationLanguage.ENGLISH) }
                            },
                            label = { Text(text.english) },
                        )
                        FilterChip(
                            selected = settings.translationLanguage == TranslationLanguage.BENGALI,
                            onClick = {
                                model.updateSettings { it.copy(translationLanguage = TranslationLanguage.BENGALI) }
                            },
                            label = { Text(text.bangla) },
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    SwitchRow(
                        text.hapticFeedback,
                        settings.haptics,
                    ) { model.updateSettings { s -> s.copy(haptics = !s.haptics) } }
                    SwitchRow(
                        text.autoAdvance,
                        settings.autoAdvance,
                    ) { model.updateSettings { s -> s.copy(autoAdvance = !s.autoAdvance) } }
                }
            }

            item {
                SettingsCard(text.dailyGoal) {
                    Text(
                        text.tahlilTargetDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(text.target, style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = !settings.laIlaTarget100,
                            onClick = { model.updateSettings { it.copy(laIlaTarget100 = false) } },
                            label = { Text("10×") },
                        )
                        FilterChip(
                            selected = settings.laIlaTarget100,
                            onClick = { model.updateSettings { it.copy(laIlaTarget100 = true) } },
                            label = { Text("100×") },
                        )
                    }
                }
            }

            item {
                SettingsCard(text.dailyReset) {
                    Text(
                        text.resetExplain,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { confirmReset = true }) {
                        Text(text.resetToday)
                    }
                }
            }

            item {
                SettingsCard(text.backup) {
                    Text(
                        text.backupBody,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    platform.BackupSection(model) { message -> toast(message) }
                }
            }

            item {
                SettingsCard(text.about) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Dhikr", style = MaterialTheme.typography.titleLarge)
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                "Version ${platform.version()}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text.aboutBody,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(text.developer, style = MaterialTheme.typography.labelLarge)
                    Text(
                        text.developerBio,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text.aboutFoot,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        SnackbarHost(hostState = snackbar)
    }

    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            title = { Text(text.resetTitle) },
            text = { Text(text.resetBody) },
            confirmButton = {
                TextButton(onClick = {
                    model.resetToday()
                    confirmReset = false
                    toast(text.progressReset)
                }) { Text(text.reset) }
            },
            dismissButton = {
                TextButton(onClick = { confirmReset = false }) { Text(text.cancel) }
            },
        )
    }
}

@Composable
private fun SettingsCard(title: String, content: @Composable () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
