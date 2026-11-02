package fi.mazhar.dhikr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import platform.Foundation.NSBundle
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierIslamicUmmAlQura
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSObject
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.writeToFile
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController

/**
 * iOS implementation: Foundation date formatters, UIKit haptics, and backup
 * pickers presented from the Compose host controller.
 */
class IosPlatform(private val hostController: () -> UIViewController) : AppPlatform {

    override fun todayGregorian(): String =
        NSDateFormatter()
            .apply {
                dateFormat = "EEEE, d MMMM yyyy"
                locale = NSLocale.currentLocale
            }
            .stringFromDate(NSDate())

    override fun todayHijri(): String? = try {
        val ummAlQura = NSCalendar(NSCalendarIdentifierIslamicUmmAlQura) ?: return null
        NSDateFormatter()
            .apply {
                calendar = ummAlQura
                locale = NSLocale.currentLocale
                dateFormat = "d MMMM yyyy"
            }
            .stringFromDate(NSDate()) + " AH"
    } catch (_: Throwable) {
        null
    }

    override fun version(): String =
        NSBundle.mainBundle.infoDictionary
            ?.get("CFBundleShortVersionString") as? String
            ?: "1.0"

    override fun vibrate(strong: Boolean) {
        try {
            val style = if (strong) {
                UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy
            } else {
                UIImpactFeedbackStyle.UIImpactFeedbackStyleLight
            }
            UIImpactFeedbackGenerator(style).impactOccurred()
        } catch (_: Throwable) {
            // Haptics are unavailable on some devices/simulators — ignore.
        }
    }

    @Composable
    override fun BackupSection(model: AppModel, onMessage: (String) -> Unit) {
        val ui = uiText(model.state.settings.translationLanguage)
        // The picker holds its delegate weakly; `remember` keeps it alive.
        val delegate = remember { BackupPickerDelegate() }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = {
                delegate.onPicked = { path ->
                    try {
                        val ok = (model.export() as NSString).writeToFile(
                            path,
                            atomically = true,
                            encoding = NSUTF8StringEncoding,
                        )
                        onMessage(if (ok) ui.backupSaved else ui.backupFailed(null))
                    } catch (e: Exception) {
                        onMessage(ui.backupFailed(e.message))
                    }
                }
                present(delegate, UIDocumentPickerMode.UIDocumentPickerModeExport)
            }) { Text(ui.export) }
            TextButton(onClick = {
                delegate.onPicked = { path ->
                    try {
                        val text = readTextFileOrNull(path)
                        if (text == null) {
                            onMessage(ui.restoreFailed(null))
                        } else {
                            val ok = model.restore(text)
                            onMessage(if (ok) ui.backupRestored else ui.backupInvalid)
                        }
                    } catch (e: Exception) {
                        onMessage(ui.restoreFailed(e.message))
                    }
                }
                present(delegate, UIDocumentPickerMode.UIDocumentPickerModeImport)
            }) { Text(ui.import) }
        }
    }

    @Suppress("DEPRECATION") // classic API; the iOS 14 replacement needs UTType bridging
    private fun present(delegate: BackupPickerDelegate, mode: UIDocumentPickerMode) {
        val picker = UIDocumentPickerViewController(listOf("public.plain-text"), mode)
        picker.delegate = delegate
        hostController().presentViewController(picker, true, null)
    }
}

/** Routes document-picker results back to the composable that armed it. */
private class BackupPickerDelegate : NSObject(), UIDocumentPickerDelegateProtocol {

    var onPicked: ((String) -> Unit)? = null

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAt urls: List<*>,
    ) {
        val path = (urls.firstOrNull() as? NSURL)?.path ?: return
        onPicked?.invoke(path)
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onPicked = null
    }
}
