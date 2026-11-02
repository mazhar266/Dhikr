package com.hikmah.dhikr.android.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hikmah.dhikr.AppTheme
import com.hikmah.dhikr.TranslationLanguage
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(model: AppModel, modifier: Modifier = Modifier) {
    val settings = model.state.settings
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var confirmReset by remember { mutableStateOf(false) }

    fun toast(message: String) {
        scope.launch { snackbar.showSnackbar(message) }
    }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/plain"),
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { out ->
                    out.write(model.export().toByteArray(Charsets.UTF_8))
                }
                toast("Backup saved")
            } catch (e: Exception) {
                toast("Backup failed: ${e.message}")
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val text = context.contentResolver.openInputStream(uri)?.use { input ->
                    input.readBytes().toString(Charsets.UTF_8)
                }
                val ok = text != null && model.restore(text)
                toast(if (ok) "Backup restored" else "Not a valid Dhikr backup")
            } catch (e: Exception) {
                toast("Restore failed: ${e.message}")
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                SettingsCard("Appearance") {
                    Text("Theme", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = settings.theme == AppTheme.LIGHT,
                            onClick = { model.updateSettings { it.copy(theme = AppTheme.LIGHT) } },
                            label = { Text("Light") },
                        )
                        FilterChip(
                            selected = settings.theme == AppTheme.DARK,
                            onClick = { model.updateSettings { it.copy(theme = AppTheme.DARK) } },
                            label = { Text("Dark") },
                        )
                        FilterChip(
                            selected = settings.theme == AppTheme.SYSTEM,
                            onClick = { model.updateSettings { it.copy(theme = AppTheme.SYSTEM) } },
                            label = { Text("System") },
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Arabic font size — ${(settings.arabicFontScale * 100).toInt()}%",
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
                SettingsCard("Reading") {
                    SwitchRow(
                        "Show transliteration",
                        settings.showTransliteration,
                    ) { model.updateSettings { s -> s.copy(showTransliteration = !s.showTransliteration) } }
                    SwitchRow(
                        "Show translation",
                        settings.showTranslation,
                    ) { model.updateSettings { s -> s.copy(showTranslation = !s.showTranslation) } }
                    Text("Translation language", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = settings.translationLanguage == TranslationLanguage.ENGLISH,
                            onClick = {
                                model.updateSettings { it.copy(translationLanguage = TranslationLanguage.ENGLISH) }
                            },
                            label = { Text("English") },
                        )
                        FilterChip(
                            selected = settings.translationLanguage == TranslationLanguage.BENGALI,
                            onClick = {
                                model.updateSettings { it.copy(translationLanguage = TranslationLanguage.BENGALI) }
                            },
                            label = { Text("Bengali") },
                        )
                    }
                    Text(
                        "Bengali translations are not yet included; entries fall back to English.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    SwitchRow(
                        "Haptic feedback",
                        settings.haptics,
                    ) { model.updateSettings { s -> s.copy(haptics = !s.haptics) } }
                    SwitchRow(
                        "Auto-advance after completing an entry",
                        settings.autoAdvance,
                    ) { model.updateSettings { s -> s.copy(autoAdvance = !s.autoAdvance) } }
                }
            }

            item {
                SettingsCard("La ilaha illallah daily goal") {
                    SwitchRow(
                        "Share between Fajr and Asr",
                        settings.sharedGoalEnabled,
                    ) { model.updateSettings { s -> s.copy(sharedGoalEnabled = !s.sharedGoalEnabled) } }
                    Text(
                        if (settings.sharedGoalEnabled) {
                            "One shared daily goal: completing it in either prayer satisfies both entries."
                        } else {
                            "Independent: Fajr and Asr count their own repetitions."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("Target", style = MaterialTheme.typography.bodyMedium)
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
                SettingsCard("Daily reset") {
                    Text(
                        "Progress resets automatically at local midnight. " +
                            "Previous days are kept as history.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { confirmReset = true }) {
                        Text("Reset today's progress")
                    }
                }
            }

            item {
                SettingsCard("Backup & restore") {
                    Text(
                        "Backups are plain files you choose where to keep. " +
                            "Restoring replaces current progress and settings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = {
                            exportLauncher.launch("dhikr-backup-${model.todayKey()}.txt")
                        }) { Text("Export…") }
                        TextButton(onClick = {
                            importLauncher.launch(arrayOf("text/plain", "text/*", "*/*"))
                        }) { Text("Import…") }
                    }
                }
            }

            item {
                Text(
                    "Dhikr 1.0 · Offline only · No account needed\n" +
                        "Qur'anic text: Tanzil-verified uthmani script via quran.com",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        SnackbarHost(hostState = snackbar)
    }

    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            title = { Text("Reset today's progress?") },
            text = { Text("Today's counts will be cleared. Other days are kept.") },
            confirmButton = {
                TextButton(onClick = {
                    model.resetToday()
                    confirmReset = false
                    toast("Today's progress reset")
                }) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { confirmReset = false }) { Text("Cancel") }
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
