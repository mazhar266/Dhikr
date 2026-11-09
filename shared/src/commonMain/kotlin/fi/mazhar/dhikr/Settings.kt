package fi.mazhar.dhikr

enum class AppTheme {
    LIGHT,

    /** Warm parchment light theme. */
    SOFIA_PAPER,
    DARK,

    /** True-black dark theme for OLED screens. */
    OLED,
    SYSTEM,
}

enum class TranslationLanguage { ENGLISH, BENGALI }

/**
 * User settings. Persisted locally. Defaults reflect the "calm, simple"
 * design brief: light/system theme, show translation and pronunciation,
 * haptics on, auto-advance off, and daily reset at local midnight.
 */
data class Settings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val arabicFontScale: Float = 1.0f,
    val showTransliteration: Boolean = true,
    val showTranslation: Boolean = true,
    val translationLanguage: TranslationLanguage = TranslationLanguage.ENGLISH,
    val haptics: Boolean = true,
    val autoAdvance: Boolean = false,
    /** Legacy backup field for the former shared tahlil goal. */
    val sharedGoalEnabled: Boolean = false,
    /** 100 in Fajr only when true. Otherwise, 10 each in Fajr and Asr. */
    val laIlaTarget100: Boolean = false,
)
