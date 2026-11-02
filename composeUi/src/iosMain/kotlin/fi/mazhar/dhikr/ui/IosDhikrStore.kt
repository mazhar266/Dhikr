package fi.mazhar.dhikr.ui

import fi.mazhar.dhikr.DhikrStore
import fi.mazhar.dhikr.SavedState
import fi.mazhar.dhikr.SavedStateCodec
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.writeToFile

/**
 * Offline file-backed store mirroring the Android one: a single text file in
 * the app's Documents directory, written atomically after every mutation.
 */
class IosDhikrStore : DhikrStore {

    private val path: String =
        (NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
            .firstOrNull() as String?) + "/dhikr_state.txt"

    override fun load(): SavedState? = try {
        val data = NSFileManager.defaultManager.contentsAtPath(path)
        val text = data?.let { NSString.create(data = it, encoding = NSUTF8StringEncoding) }
        if (text != null) SavedStateCodec.decode(text) else null
    } catch (_: Exception) {
        null
    }

    override fun save(state: SavedState) {
        val encoded = SavedStateCodec.encode(state)
        (encoded as NSString).writeToFile(path, atomically = true, encoding = NSUTF8StringEncoding)
    }
}

/** Reads a small UTF-8 text file, or null when it cannot be read/decoded. */
internal fun readTextFileOrNull(path: String): String? {
    val data: NSData? = NSFileManager.defaultManager.contentsAtPath(path)
    return data?.let { NSString.create(data = it, encoding = NSUTF8StringEncoding) }
}
