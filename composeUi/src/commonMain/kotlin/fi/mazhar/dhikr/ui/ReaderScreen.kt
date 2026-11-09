package fi.mazhar.dhikr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.mazhar.dhikr.DhikrKind
import fi.mazhar.dhikr.EntryUiState
import fi.mazhar.dhikr.RoutineId
import fi.mazhar.dhikr.TranslationLanguage
import fi.mazhar.dhikr.VerificationStatus
import fi.mazhar.dhikr.pronunciationFor
import fi.mazhar.dhikr.translationFor
import kotlinx.coroutines.delay

@Composable
fun ReaderScreen(model: AppModel, routineId: RoutineId, order: Int, modifier: Modifier = Modifier) {
    val entries = model.entriesOf(routineId)
    val entry = entries.firstOrNull { it.key.order == order }
    val settings = model.state.settings
    val text = uiText(settings.translationLanguage)
    if (entry == null) {
        Text(text.entryNotFound, Modifier.padding(16.dp))
        return
    }
    val bangla = settings.translationLanguage == TranslationLanguage.BENGALI
    var showTargetDialog by remember { mutableStateOf(false) }

    val position = entries.indexOfFirst { it.key.order == order }
    val hasPrev = position > 0
    val hasNext = position < entries.lastIndex

    fun goTo(offset: Int) {
        val next = entries.getOrNull(position + offset) ?: return
        model.openReader(routineId, next.key.order)
    }

    // Auto-advance shortly after an entry becomes complete, when enabled.
    var wasComplete by remember(entry.key) { mutableStateOf(entry.complete) }
    LaunchedEffect(entry.count) {
        val isComplete = entry.complete
        if (!wasComplete && isComplete && settings.autoAdvance && hasNext) {
            delay(900)
            goTo(1)
        }
        wasComplete = isComplete
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))

            Text(
                entry.dhikr.arabicTitle ?: entry.dhikr.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                entry.dhikr.arabic,
                style = arabicTextStyle(settings.arabicFontScale).copy(
                    textDirection = TextDirection.Content,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                textAlign = TextAlign.Center,
            )

            if (settings.showTransliteration) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text.pronunciation,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    entry.dhikr.pronunciationFor(settings.translationLanguage),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontStyle = if (bangla) FontStyle.Normal else FontStyle.Italic,
                        fontSize = if (bangla) 18.sp else MaterialTheme.typography.bodyLarge.fontSize,
                        lineHeight = if (bangla) 30.sp else MaterialTheme.typography.bodyLarge.lineHeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            if (settings.showTranslation) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text.meaning,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    entry.dhikr.translationFor(settings.translationLanguage),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = if (bangla) 18.sp else MaterialTheme.typography.bodyLarge.fontSize,
                        lineHeight = if (bangla) 30.sp else MaterialTheme.typography.bodyLarge.lineHeight,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(16.dp))

            if (entry.dhikr.sourceRefs.isNotEmpty()) {
                Text(
                    entry.dhikr.sourceRefs.joinToString("\n") { "• " + it.reference },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (entry.dhikr.verification == VerificationStatus.NEEDS_VERIFICATION) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "⚠ ${text.needsVerification}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        Controls(
            model = model,
            entry = entry,
            hasPrev = hasPrev,
            hasNext = hasNext,
            onPrev = { goTo(-1) },
            onNext = { goTo(1) },
            onEditTarget = { showTargetDialog = true },
            text = text,
        )
    }

    if (showTargetDialog) {
        TargetDialog(
            current = entry.target,
            text = text,
            onDismiss = { showTargetDialog = false },
            onSave = { newTarget ->
                showTargetDialog = false
                model.setTarget(entry.key, newTarget)
            },
        )
    }
}

@Composable
private fun Controls(
    model: AppModel,
    entry: EntryUiState,
    hasPrev: Boolean,
    hasNext: Boolean,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onEditTarget: () -> Unit,
    text: UiText,
) {
    val platform = LocalAppPlatform.current
    val haptics = model.state.settings.haptics

    fun onTap() {
        val wasComplete = entry.complete
        model.increment(entry.key)
        if (haptics && !wasComplete && entry.count + 1 >= entry.target) {
            platform.vibrate(strong = true)
        } else if (haptics) {
            platform.vibrate(strong = false)
        }
    }

    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = onPrev, enabled = hasPrev) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Text(text.prev)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (entry.dhikr.id != "la-ilaha-illallah-goal") {
                    IconButton(onClick = onEditTarget) {
                        Icon(Icons.Filled.TextFields, contentDescription = text.changeTarget)
                    }
                }
                Text(
                    text.entryPosition(entry.position),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            OutlinedButton(onClick = onNext, enabled = hasNext) {
                Text(text.next)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = {
                    model.decrement(entry.key)
                    if (haptics) platform.vibrate(strong = false)
                },
                enabled = entry.count > 0,
            ) {
                Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = text.undo)
            }

            val isOnce = entry.dhikr.kind == DhikrKind.RECITE_ONCE && entry.target == 1
            Button(
                onClick = {
                    if (isOnce) {
                        model.markRecited(entry.key)
                    } else {
                        onTap()
                    }
                    if (haptics && isOnce) platform.vibrate(strong = true)
                },
                enabled = !entry.complete,
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                if (entry.complete) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Check, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text(text.complete, style = MaterialTheme.typography.titleLarge)
                    }
                } else if (isOnce) {
                    Text(text.markAsRecited, style = MaterialTheme.typography.titleLarge)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${entry.count} / ${entry.target}",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            if (entry.dhikr.kind == DhikrKind.RECITE_ONCE) text.recited else text.tapToCount,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TargetDialog(current: Int, text: UiText, onDismiss: () -> Unit, onSave: (Int) -> Unit) {
    var value by remember { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text.repetitionTarget) },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedButton(
                    onClick = { if (value > 1) value-- },
                ) { Icon(Icons.Filled.Remove, contentDescription = text.decrease) }
                Text(
                    "$value",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                OutlinedButton(
                    onClick = { if (value < 1000) value++ },
                ) { Icon(Icons.Filled.Add, contentDescription = text.increase) }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(value) }) { Text(text.save) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text.cancel) }
        },
    )
}
