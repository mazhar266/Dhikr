package com.hikmah.dhikr.android.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Gentle tap haptic; [strong] gives a heavier double-pulse for completions. */
fun vibrate(context: Context, strong: Boolean) {
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

fun gregorianDateText(): String =
    SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())

/**
 * Hijri date via the device's ICU Islamic (Umm al-Qura) calendar.
 * Available since API 24; null (with graceful fallback) on failure.
 */
fun hijriDateText(): String? = try {
    val cal = android.icu.util.IslamicCalendar()
    cal.calculationType = android.icu.util.IslamicCalendar.CalculationType.ISLAMIC_UMALQURA
    val fmt = android.icu.text.SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    fmt.calendar = cal
    fmt.format(cal.time).replaceFirst(" AH", "") + " AH"
} catch (_: Throwable) {
    null
}

fun percentText(fraction: Float): String = "${(fraction * 100).toInt()}%"
