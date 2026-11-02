package com.hikmah.dhikr

/** How a dhikr is performed, which determines the UI interaction. */
enum class DhikrKind {
    /** Numerically counted repetitions (Subhanallah, etc.). */
    REPEATED,

    /** Recite once — use "Mark as Recited" rather than a counter. */
    RECITE_ONCE,
}

enum class DhikrCategory {
    QURAN,
    SUNNAH,
    DU_A,
    CUSTOM,
}

/**
 * Verification status for the *content* of a dhikr (Arabic wording,
 * translation, and cited sources). Separated from repetition-count and
 * timing verification, which belong on the [RoutineEntry] / [SharedDailyGoal].
 *
 * Do not silently upgrade [NEEDS_VERIFICATION] entries to verified content.
 */
enum class VerificationStatus {
    VERIFIED,
    NEEDS_VERIFICATION,
}

/** A citation (Qur'an, Hadith, or other source). */
data class SourceRef(
    val type: SourceType,
    val reference: String,
    val grading: String? = null,
)

enum class SourceType {
    QURAN,
    HADITH,
    OTHER,
}

/**
 * Canonical dhikr definition. The same dhikr can be reused across multiple
 * routines; repetitions and routine association live on [RoutineEntry].
 */
data class Dhikr(
    val id: String,
    val title: String,
    val transliteration: String,
    val arabic: String,
    val translation: String,
    val category: DhikrCategory,
    val kind: DhikrKind,
    val verification: VerificationStatus,
    val sourceRefs: List<SourceRef> = emptyList(),
    val notes: String? = null,
    /** Optional verified Bengali translation; null when not yet sourced. */
    val bengali: String? = null,
    /** Arabic heading for display (surah name or the dhikr phrase itself). */
    val arabicTitle: String? = null,
)

/** Convenience greeting used by the app shell and existing tests. */
fun greeting(): String = "Salam from ${Platform().name}"
