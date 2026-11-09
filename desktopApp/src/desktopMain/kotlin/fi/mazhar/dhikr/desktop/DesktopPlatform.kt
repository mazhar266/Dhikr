package fi.mazhar.dhikr.desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import fi.mazhar.dhikr.ui.AppModel
import fi.mazhar.dhikr.ui.AppPlatform
import fi.mazhar.dhikr.ui.uiText
import java.io.File
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.util.Date
import java.util.Locale
import javax.swing.JFileChooser
import javax.swing.SwingUtilities

/** Desktop implementation: HijrahDate, no haptics, Swing file-chooser backups. */
object DesktopPlatform : AppPlatform {

    override fun todayGregorian(): String =
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())

    override fun todayHijri(): String? = try {
        val hijri = HijrahDate.now() // Umm al-Qura chronology
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
            .withChronology(HijrahChronology.INSTANCE)
            .withLocale(Locale.ENGLISH)
        formatter.format(hijri) + " AH"
    } catch (_: Throwable) {
        null
    }

    override fun version(): String = "1.2.0"

    override fun vibrate(strong: Boolean) {
        // No haptics on desktop.
    }

    @Composable
    override fun BackupSection(model: AppModel, onMessage: (String) -> Unit) {
        val ui = uiText(model.state.settings.translationLanguage)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = {
                chooseFile(save = true) { path ->
                    if (path != null) {
                        try {
                            File(path).writeText(model.export(), Charsets.UTF_8)
                            onMessage(ui.backupSaved)
                        } catch (e: Exception) {
                            onMessage(ui.backupFailed(e.message))
                        }
                    }
                }
            }) { Text(ui.export) }
            TextButton(onClick = {
                chooseFile(save = false) { path ->
                    if (path != null) {
                        try {
                            val ok = model.restore(File(path).readText(Charsets.UTF_8))
                            onMessage(if (ok) ui.backupRestored else ui.backupInvalid)
                        } catch (e: Exception) {
                            onMessage(ui.restoreFailed(e.message))
                        }
                    }
                }
            }) { Text(ui.import) }
        }
    }

    private fun chooseFile(save: Boolean, onPath: (String?) -> Unit) {
        SwingUtilities.invokeLater {
            val chooser = JFileChooser()
            chooser.selectedFile = File("dhikr-backup.txt")
            val result = if (save) chooser.showSaveDialog(null) else chooser.showOpenDialog(null)
            onPath(
                if (result == JFileChooser.APPROVE_OPTION) {
                    chooser.selectedFile.absolutePath
                } else {
                    null
                },
            )
        }
    }
}
