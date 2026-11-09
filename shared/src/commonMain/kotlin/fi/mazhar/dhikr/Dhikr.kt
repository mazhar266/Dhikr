package fi.mazhar.dhikr

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
    USER_PROVIDED,
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
    /** Bengali meaning. Null only for a dhikr that has not been translated yet. */
    val bengali: String? = null,
    /** Bengali title used when the app language is Bangla. */
    val titleBn: String? = null,
    /** Bengali-script pronunciation (উচ্চারণ). Latin pronunciation stays in [transliteration]. */
    val pronunciationBn: String? = null,
    /** Arabic heading for display (surah name or the dhikr phrase itself). */
    val arabicTitle: String? = null,
)

fun Dhikr.titleFor(language: TranslationLanguage): String =
    if (language == TranslationLanguage.BENGALI) titleBn ?: title else title

fun Dhikr.translationFor(language: TranslationLanguage): String =
    if (language == TranslationLanguage.BENGALI) bengali ?: translation else translation

/** How to read the Arabic: Bangla script in Bangla, Latin transliteration in English. */
fun Dhikr.pronunciationFor(language: TranslationLanguage): String =
    if (language == TranslationLanguage.BENGALI) pronunciationBn ?: transliteration else transliteration

/** Convenience greeting used by the app shell and existing tests. */
fun greeting(): String = "Salam from ${Platform().name}"
