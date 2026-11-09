package fi.mazhar.dhikr.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fi.mazhar.dhikr.DhikrCategory
import fi.mazhar.dhikr.EntryUiState
import fi.mazhar.dhikr.RoutineId
import fi.mazhar.dhikr.pronunciationFor
import fi.mazhar.dhikr.titleFor

@Composable
fun RoutineScreen(model: AppModel, routineId: RoutineId, modifier: Modifier = Modifier) {
    val routine = model.routineState(routineId)
    var unfinishedOnly by remember { mutableStateOf(false) }
    val settings = model.state.settings
    val text = uiText(settings.translationLanguage)
    val personal = model.state.personalRoutines.firstOrNull { it.id == routineId }
    var renaming by rememberSaveable(routineId.name) { mutableStateOf(false) }
    var editingOrder by rememberSaveable(routineId.name) { mutableStateOf<Int?>(null) }

    if (personal != null && renaming) {
        PersonalListNameDialog(text, personal.title, onDismiss = { renaming = false }) { name ->
            model.renamePersonalRoutine(routineId, name)
            renaming = false
        }
    }
    if (personal != null) {
        editingOrder?.let { order ->
            val initial = personal.entries.firstOrNull { it.order == order }
            if (order == 0 || initial != null) {
                PersonalDhikrDialog(
                    text = text,
                    initial = initial,
                    onDismiss = { editingOrder = null },
                    onRemove = {
                        model.removePersonalDhikr(routineId, order)
                        editingOrder = null
                    },
                ) { arabic, pronunciation, meaning, target ->
                    model.savePersonalDhikr(routineId, initial?.order, arabic, pronunciation, meaning, target)
                    editingOrder = null
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (routine == null) {
            Text(text.unknownRoutine, Modifier.padding(16.dp))
            return
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (personal == null) {
                Text(
                    routine.arabicTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            } else {
                TextButton(onClick = { renaming = true }) { Text(text.renamePersonalList) }
            }
            FilterChip(
                selected = unfinishedOnly,
                onClick = { unfinishedOnly = !unfinishedOnly },
                label = { Text(text.unfinishedOnly) },
            )
        }

        val entries = if (unfinishedOnly) routine.entries.filter { !it.complete } else routine.entries
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (personal != null) {
                item {
                    OutlinedButton(onClick = { editingOrder = 0 }, modifier = Modifier.fillMaxWidth()) {
                        Text(text.addPersonalDhikr)
                    }
                }
            }
            items(entries, key = { it.key.toString() }) { entry ->
                EntryRow(
                    entry = entry,
                    title = entry.dhikr.titleFor(settings.translationLanguage),
                    pronunciation = if (settings.showTransliteration) {
                        entry.dhikr.pronunciationFor(settings.translationLanguage)
                    } else {
                        null
                    },
                    text = text,
                    onEdit = if (personal != null) ({ editingOrder = entry.key.order }) else null,
                ) { model.openReader(routineId, entry.key.order) }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun EntryRow(
    entry: EntryUiState,
    title: String,
    pronunciation: String?,
    text: UiText,
    onEdit: (() -> Unit)?,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "${entry.position}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(width = 28.dp, height = 20.dp),
            )
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                val isQuran = entry.dhikr.category == DhikrCategory.QURAN
                val personal = entry.key.routineId.isPersonal
                if (!personal && pronunciation != null && !isQuran) {
                    Text(
                        pronunciation,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (!personal) {
                    Text(
                        if (isQuran) entry.dhikr.arabicTitle.orEmpty() else entry.dhikr.arabic,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDirection = TextDirection.Content,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 2.dp),
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (entry.target == 1) {
                            if (entry.complete) text.recited else text.once
                        } else {
                            "${entry.count} / ${entry.target}"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = if (entry.complete) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    )
                    if (entry.viaSharedGoal) {
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text.sharedGoal,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
            Spacer(Modifier.size(10.dp))
            if (entry.complete) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = text.done,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Icon(
                    Icons.Filled.CheckCircleOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (onEdit != null) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = text.editPersonalDhikr)
                }
            }
        }
    }
}
