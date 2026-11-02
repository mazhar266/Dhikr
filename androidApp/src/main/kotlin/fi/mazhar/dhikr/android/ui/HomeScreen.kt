package fi.mazhar.dhikr.android.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fi.mazhar.dhikr.RoutineUiState

@Composable
fun HomeScreen(model: AppModel, modifier: Modifier = Modifier) {
    val summary = model.summary()
    val resume = model.resumeCandidate()
    val streak = model.streak()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column {
                Text(
                    "As-salamu alaykum",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    gregorianDateText(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                hijriDateText()?.let { hijri ->
                    Text(
                        hijri,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Today's adhkar", style = MaterialTheme.typography.titleMedium)
                        Text(
                            percentText(summary.overallPercent),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { summary.overallPercent },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${summary.completedEntries} of ${summary.totalEntries} entries · " +
                            "${summary.totalRepetitions} repetitions" +
                            if (streak > 0) " · $streak-day streak" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        if (resume != null) {
            item {
                val routine = summary.routines.firstOrNull { it.id == resume }
                if (routine != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { model.openRoutine(resume) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    "Continue ${routine.title}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                                Text(
                                    "${routine.completedEntries} of ${routine.totalEntries} entries done",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }
            }
        }

        items(summary.routines, key = { it.id.name }) { routine ->
            RoutineCard(routine) { model.openRoutine(routine.id) }
        }

        item {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun RoutineCard(routine: RoutineUiState, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    routine.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    routine.arabicTitle,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.padding(vertical = 2.dp),
                )
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { routine.percent },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "${routine.completedEntries} / ${routine.totalEntries} entries · " +
                        percentText(routine.percent),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.size(12.dp))
            if (routine.complete) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Complete",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}
