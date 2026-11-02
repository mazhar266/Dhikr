package com.hikmah.dhikr

enum class AppTheme { LIGHT, DARK, SYSTEM }

enum class TranslationLanguage { ENGLISH, BENGALI }

/**
 * User settings. Persisted locally. Defaults reflect the "calm, simple"
 * design brief: light/system theme, show translation, hide transliteration by
 * default, haptics on, auto-advance off, daily reset at local midnight, and
 * the 10/100 "La ilaha illallah" shared goal mode defaulting to INDEPENDENT
 * so Fajr and Asr progress separately unless the user opts in to sharing.
 */
data class Settings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val arabicFontScale: Float = 1.0f,
    val showTransliteration: Boolean = false,
    val showTranslation: Boolean = true,
    val translationLanguage: TranslationLanguage = TranslationLanguage.ENGLISH,
    val haptics: Boolean = true,
    val autoAdvance: Boolean = false,
    /** Whether the "La ilaha illallah" daily goal is shared between Fajr & Asr. */
    val sharedGoalEnabled: Boolean = false,
    /** 10 or 100 for the optional La ilaha illallah goal. */
    val laIlaTarget100: Boolean = false,
)
