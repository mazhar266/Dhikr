package fi.mazhar.dhikr.android.ui

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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.mazhar.dhikr.DayKeys

@Composable
fun SummaryScreen(model: AppModel, modifier: Modifier = Modifier) {
    val summary = model.summary()
    val streak = model.streak()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(Modifier.weight(1f)) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp),
                            )
                            Text(
                                " $streak",
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                        Text(
                            "day streak",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Card(Modifier.weight(1f)) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "${summary.totalRepetitions}",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Text(
                            "repetitions today",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Routines", style = MaterialTheme.typography.titleMedium)
                        Text(
                            percentText(summary.overallPercent),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    summary.routines.forEach { routine ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                routine.title,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(0.28f),
                            )
                            LinearProgressIndicator(
                                progress = { routine.percent },
                                modifier = Modifier.weight(0.52f),
                            )
                            Text(
                                " ${routine.completedEntries}/${routine.totalEntries}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(0.20f),
                            )
                        }
                    }
                }
            }
        }

        if (summary.unfinishedEntries.isNotEmpty()) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Unfinished", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        summary.routines.forEach { routine ->
                            val pending = routine.entries.filter { !it.complete }
                            if (pending.isNotEmpty()) {
                                Text(
                                    "${routine.title} — ${pending.size} remaining",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            val today = model.todayKey()
            val last14 = (13 downTo 0).map { DayKeys.addDays(today, -it.toLong()) }
            val history = model.historyComplete(last14)
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Last 14 days", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        last14.forEach { day ->
                            val complete = history[day] == true
                            Card(
                                modifier = Modifier.size(width = 18.dp, height = 28.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                    containerColor = if (complete) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    },
                                ),
                            ) {}
                        }
                    }
                    Text(
                        "A filled block is a day with at least one completed routine",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }
        }

        item {
            Text(
                "Counts are a tracking aid, not a measure of religious merit.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
