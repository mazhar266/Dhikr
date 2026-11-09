package fi.mazhar.dhikr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fi.mazhar.dhikr.PersonalDhikr

@Composable
internal fun PersonalListNameDialog(
    text: UiText,
    initialName: String?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(initialName.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialName == null) text.newPersonalList else text.renamePersonalList) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text.personalListName) },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(onClick = { onSave(name) }, enabled = name.isNotBlank()) { Text(text.save) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text.cancel) } },
    )
}

@Composable
internal fun PersonalDhikrDialog(
    text: UiText,
    initial: PersonalDhikr?,
    onDismiss: () -> Unit,
    onRemove: () -> Unit,
    onSave: (arabic: String, pronunciation: String, meaning: String, target: Int) -> Unit,
) {
    var arabic by rememberSaveable { mutableStateOf(initial?.arabic.orEmpty()) }
    var pronunciation by rememberSaveable { mutableStateOf(initial?.pronunciation.orEmpty()) }
    var meaning by rememberSaveable { mutableStateOf(initial?.meaning.orEmpty()) }
    var targetText by rememberSaveable { mutableStateOf((initial?.target ?: 1).toString()) }
    var confirmRemoval by rememberSaveable { mutableStateOf(false) }
    val target = targetText.toIntOrNull()?.takeIf { it > 0 }

    if (confirmRemoval) {
        AlertDialog(
            onDismissRequest = { confirmRemoval = false },
            title = { Text(text.removePersonalDhikr) },
            text = { Text(text.removePersonalDhikrMessage) },
            confirmButton = { TextButton(onClick = onRemove) { Text(text.removePersonalDhikr) } },
            dismissButton = {
                TextButton(onClick = { confirmRemoval = false }) { Text(text.cancel) }
            },
        )
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) text.addPersonalDhikr else text.editPersonalDhikr) },
        text = {
            Column(
                Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text.personalDhikrFields)
                OutlinedTextField(arabic, { arabic = it }, Modifier.fillMaxWidth(), label = { Text(text.arabic) }, maxLines = 4)
                OutlinedTextField(pronunciation, { pronunciation = it }, Modifier.fillMaxWidth(), label = { Text(text.pronunciation) }, maxLines = 4)
                OutlinedTextField(meaning, { meaning = it }, Modifier.fillMaxWidth(), label = { Text(text.meaning) }, maxLines = 4)
                OutlinedTextField(
                    targetText, { targetText = it }, Modifier.fillMaxWidth(),
                    label = { Text(text.repetitionTarget) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = target == null,
                )
                if (initial != null) {
                    TextButton(onClick = { confirmRemoval = true }) { Text(text.removePersonalDhikr) }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { target?.let { onSave(arabic, pronunciation, meaning, it) } },
                enabled = target != null && (arabic.isNotBlank() || pronunciation.isNotBlank() || meaning.isNotBlank()),
            ) { Text(text.save) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(text.cancel) } },
    )
}
