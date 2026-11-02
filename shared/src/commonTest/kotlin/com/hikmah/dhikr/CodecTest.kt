package com.hikmah.dhikr

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CodecTest {

    private val full = SavedState(
        settings = Settings(
            theme = AppTheme.DARK,
            arabicFontScale = 1.4f,
            showTransliteration = true,
            showTranslation = false,
            translationLanguage = TranslationLanguage.BENGALI,
            haptics = false,
            autoAdvance = true,
            sharedGoalEnabled = true,
            laIlaTarget100 = true,
        ),
        progress = listOf(
            DhikrProgress("2026-11-01", RoutineKey(RoutineId.FAJR, 12), "subhanallah", 33, 1_796_000_000_000),
            DhikrProgress("2026-11-01", RoutineKey(RoutineId.FAJR, 13), "alhamdulillah", 7, null),
        ),
        goals = listOf(
            SharedDailyGoal(
                goalId = "goal-la-ilaha-illallah",
                dayKey = "2026-11-01",
                target = 100,
                completedCount = 40,
                applicableRoutineIds = listOf(RoutineId.FAJR, RoutineId.ASR),
                satisfiedBy = null,
                contributions = mapOf("FAJR-24" to 40),
            ),
        ),
        targetOverrides = mapOf("ASR-24" to 100, "FAJR-1" to 3),
        lastOpenedRoutine = RoutineId.ASR,
    )

    @Test
    fun roundTrip_preservesEverything() {
        val decoded = SavedStateCodec.decode(SavedStateCodec.encode(full))
        assertEquals(full, decoded)
    }

    @Test
    fun decode_rejectsGarbage() {
        assertNull(SavedStateCodec.decode(""))
        assertNull(SavedStateCodec.decode("hello world"))
        assertNull(SavedStateCodec.decode("DHIKR_STATE_V2\nS|..."))
    }

    @Test
    fun decode_skipsUnknownAndMalformedLines() {
        val text = SavedStateCodec.encode(full)
            .lines()
            .joinToString("\n") { line ->
                if (line.startsWith("P|")) "$line\nX|future|feature" else line
            } + "\nBROKEN LINE\nP|not|enough\nT|tooshort\n"
        val decoded = SavedStateCodec.decode(text)
        assertEquals(full, decoded)
    }

    @Test
    fun decode_emptyState() {
        val decoded = SavedStateCodec.decode(SavedStateCodec.encode(SavedState()))
        assertEquals(SavedState(), decoded)
    }

    @Test
    fun encode_linesAreConstrained() {
        val text = SavedStateCodec.encode(full)
        assertTrue(text.startsWith("DHIKR_STATE_V1"))
        // No field should ever need escaping — ids, day keys, enum names only.
        assertTrue(text.none { it == '\t' })
    }
}
