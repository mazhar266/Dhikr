package fi.mazhar.dhikr.android

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import fi.mazhar.dhikr.ui.AppModel
import fi.mazhar.dhikr.ui.AppPlatform
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Android implementation: ICU Hijri dates, Vibrator haptics, SAF backups. */
class AndroidPlatform(private val context: Context) : AppPlatform {

    override fun todayGregorian(): String =
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())

    override fun todayHijri(): String? = try {
        val cal = android.icu.util.IslamicCalendar()
        cal.calculationType = android.icu.util.IslamicCalendar.CalculationType.ISLAMIC_UMALQURA
        val fmt = android.icu.text.SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        fmt.calendar = cal
        fmt.format(cal.time).replaceFirst(" AH", "") + " AH"
    } catch (_: Throwable) {
        null
    }

    override fun version(): String = BuildConfig.VERSION_NAME

    override fun vibrate(strong: Boolean) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        if (!vibrator.hasVibrator()) return
        if (Build.VERSION.SDK_INT >= 26) {
            if (strong) {
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 45, 70, 85), -1))
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(18, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(if (strong) 110L else 20L)
        }
    }

    @Composable
    override fun BackupSection(model: AppModel, onMessage: (String) -> Unit) {
        val exportLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.CreateDocument("text/plain"),
        ) { uri: Uri? ->
            if (uri != null) {
                try {
                    context.contentResolver.openOutputStream(uri)?.use { out ->
                        out.write(model.export().toByteArray(Charsets.UTF_8))
                    }
                    onMessage("Backup saved")
                } catch (e: Exception) {
                    onMessage("Backup failed: ${e.message}")
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
                    onMessage(if (ok) "Backup restored" else "Not a valid Dhikr backup")
                } catch (e: Exception) {
                    onMessage("Restore failed: ${e.message}")
                }
            }
        }

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
