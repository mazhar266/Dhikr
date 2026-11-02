package fi.mazhar.dhikr.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Platform services the shared UI needs. Each entry point provides one:
 * Android (MainActivity) and desktop (Main.kt). This keeps commonMain free
 * of android.* and JDK-only APIs.
 */
interface AppPlatform {
    /** Today's Gregorian date, "EEEE, d MMMM yyyy" in the device locale. */
    fun todayGregorian(): String

    /** Hijri (Umm al-Qura) date, or null when unavailable on the platform. */
    fun todayHijri(): String?

    /** App version shown in Settings → About. */
    fun version(): String

    /** Gentle tap / strong completion feedback; no-op where unsupported. */
    fun vibrate(strong: Boolean)

    /** Backup & restore controls with platform-appropriate file pickers. */
    @Composable
    fun BackupSection(model: AppModel, onMessage: (String) -> Unit)
}

val LocalAppPlatform = staticCompositionLocalOf<AppPlatform> {
    error("AppPlatform not provided")
}

fun percentText(fraction: Float): String = "${(fraction * 100).toInt()}%"
