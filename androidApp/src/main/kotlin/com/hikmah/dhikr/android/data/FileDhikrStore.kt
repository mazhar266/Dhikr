package com.hikmah.dhikr.android.data

import android.content.Context
import com.hikmah.dhikr.DhikrRepository
import com.hikmah.dhikr.DhikrStore
import com.hikmah.dhikr.SavedState
import com.hikmah.dhikr.SavedStateCodec
import com.hikmah.dhikr.SystemClock
import com.hikmah.dhikr.android.ui.AppModel
import java.io.File

/**
 * Offline file-backed store: one small text file in the app's private
 * storage, written atomically after every mutation. No network, ever.
 */
class FileDhikrStore(private val file: File) : DhikrStore {

    override fun load(): SavedState? = try {
        if (file.exists()) SavedStateCodec.decode(file.readText(Charsets.UTF_8)) else null
    } catch (_: Exception) {
        null
    }

    override fun save(state: SavedState) {
        val tmp = File(file.parentFile, file.name + ".tmp")
        tmp.writeText(SavedStateCodec.encode(state), Charsets.UTF_8)
        if (!tmp.renameTo(file)) {
            file.writeText(SavedStateCodec.encode(state), Charsets.UTF_8)
            tmp.delete()
        }
    }
}

/** Process-wide holder so counts survive configuration changes (rotation). */
object AppHolder {

    @Volatile
    private var instance: AppModel? = null

    fun model(context: Context): AppModel {
        return instance ?: synchronized(this) {
            instance ?: create(context.applicationContext).also { instance = it }
        }
    }

    private fun create(context: Context): AppModel {
        val store = FileDhikrStore(File(context.filesDir, "dhikr_state.txt"))
        val repo = DhikrRepository(store, SystemClock())
        repo.load()
        return AppModel(repo)
    }
}
