package fi.mazhar.dhikr.content

import fi.mazhar.dhikr.RoutineEntry
import fi.mazhar.dhikr.RoutineId
import fi.mazhar.dhikr.TranslationLanguage

/**
 * The six bundled routines. Entry order, repetition targets, and prayer
 * associations follow the user's personal inventory (see the task brief and
 * DHIKR_CONTENT_REVIEW.md); where a verified hadith establishes a different
 * count or timing, the user's requested default is preserved and the
 * difference is documented on the [DhikrCatalog] notes.
 */
data class RoutineDefinition(
    val id: RoutineId,
    val title: String,
    val titleBn: String,
    val arabicTitle: String,
    val description: String,
    val descriptionBn: String,
    val entries: List<RoutineEntry>,
) {
    fun titleFor(language: TranslationLanguage): String =
        if (language == TranslationLanguage.BENGALI) titleBn else title

    fun descriptionFor(language: TranslationLanguage): String =
        if (language == TranslationLanguage.BENGALI) descriptionBn else description
}

object Routines {

    internal const val TAHLIL_ID = "la-ilaha-illallah-goal"

    private fun entry(routine: RoutineId, order: Int, dhikrId: String, target: Int) =
        RoutineEntry(key = fi.mazhar.dhikr.RoutineKey(routine, order), dhikrId = dhikrId, target = target)

    // Entry keys are persisted. New entries use fresh keys regardless of display position.
    private val fajrEntries: List<RoutineEntry> = listOf(
        entry(RoutineId.FAJR, 1, "allahu-akbar", 1),
        entry(RoutineId.FAJR, 2, "astaghfirullah", 2),
        entry(RoutineId.FAJR, 30, "astaghfirullah-rabbi", 1),
        entry(RoutineId.FAJR, 3, "takbir-tahlil-after-salam", 1),
        entry(RoutineId.FAJR, 4, "antas-salam", 1),
        entry(RoutineId.FAJR, 5, "ajirni-minan-nar", 7),
        entry(RoutineId.FAJR, 6, "quran-ayatul-kursi", 1),
        entry(RoutineId.FAJR, 7, "quran-hashr-59-22-24", 1),
        entry(RoutineId.FAJR, 8, "quran-kafirun", 1),
        entry(RoutineId.FAJR, 9, "quran-ikhlas", 3),
        entry(RoutineId.FAJR, 10, "quran-falaq", 3),
        entry(RoutineId.FAJR, 11, "quran-nas", 3),
        entry(RoutineId.FAJR, 12, "subhanallah", 33),
        entry(RoutineId.FAJR, 13, "alhamdulillah", 33),
        entry(RoutineId.FAJR, 14, "allahu-akbar", 34),
        entry(RoutineId.FAJR, 15, "salawat-ibrahimiyyah", 10),
        entry(RoutineId.FAJR, 16, "raditu-billahi", 3),
        entry(RoutineId.FAJR, 17, "hasbiyallahu", 7),
        entry(RoutineId.FAJR, 18, "audhu-kalimat-min-sharri", 3),
        entry(RoutineId.FAJR, 19, "audhu-kalimat-shaytan", 1),
        entry(RoutineId.FAJR, 20, "audhu-kalimat-ghadab", 1),
        entry(RoutineId.FAJR, 21, "bismillah-la-yadurru", 3),
        entry(RoutineId.FAJR, 31, "subhanallahi-adada-khalqihi", 3),
        entry(RoutineId.FAJR, 22, "sayyidul-istighfar", 1),
        entry(RoutineId.FAJR, 23, "afini-badani", 3),
        entry(RoutineId.FAJR, 24, TAHLIL_ID, 10),
        entry(RoutineId.FAJR, 25, "subhanallahi-wabihamdihi-100", 100),
        entry(RoutineId.FAJR, 26, "astaghfirullah", 100),
        entry(RoutineId.FAJR, 27, "hammi-wal-hazan", 1),
        entry(RoutineId.FAJR, 28, "ya-hayyu-ya-qayyum", 1),
        entry(RoutineId.FAJR, 29, "ilman-nafian", 1),
    )

    // Asr reuses Fajr's content and has its own progress keys.
    private val asrEntries = fajrEntries.dropLast(1).map {
        it.copy(key = fi.mazhar.dhikr.RoutineKey(RoutineId.ASR, it.key.order))
    }

    private fun shortRoutine(
        id: RoutineId,
        title: String,
        titleBn: String,
        arabicTitle: String,
        description: String,
        descriptionBn: String,
    ) = RoutineDefinition(
            id = id,
            title = title,
            titleBn = titleBn,
            arabicTitle = arabicTitle,
            description = description,
            descriptionBn = descriptionBn,
            entries = listOf(
                entry(id, 1, "quran-ayatul-kursi", 1),
                entry(id, 2, "subhanallah", 33),
                entry(id, 3, "alhamdulillah", 33),
                entry(id, 4, "allahu-akbar", 34),
                entry(id, 5, "salawat-ibrahimiyyah", 10),
            ),
        )

    private val items: List<RoutineDefinition> = listOf(
        RoutineDefinition(
            id = RoutineId.FAJR,
            title = "Fajr",
            titleBn = "ফজর",
            arabicTitle = "الْفَجْر",
            description = "Morning adhkar after the Fajr prayer",
            descriptionBn = "ফজরের নামাযের পর সকালের আযকার",
            entries = fajrEntries,
        ),
        shortRoutine(RoutineId.DHUHR, "Dhuhr", "যোহর", "الظُّهْر", "Adhkar after the Dhuhr prayer", "যোহরের নামাযের পরের আযকার"),
        RoutineDefinition(
            id = RoutineId.ASR,
            title = "Asr",
            titleBn = "আসর",
            arabicTitle = "الْعَصْر",
            description = "Afternoon adhkar after the Asr prayer",
            descriptionBn = "আসরের নামাযের পর বিকেলের আযকার",
            entries = asrEntries,
        ),
        shortRoutine(RoutineId.MAGHRIB, "Maghrib", "মাগরিব", "الْمَغْرِب", "Adhkar after the Maghrib prayer", "মাগরিবের নামাযের পরের আযকার"),
        shortRoutine(RoutineId.ISHA, "Isha", "এশা", "الْعِشَاء", "Adhkar after the Isha prayer", "এশার নামাযের পরের আযকার"),
        RoutineDefinition(
            id = RoutineId.BEFORE_SLEEP,
            title = "Before Sleep",
            titleBn = "ঘুমানোর আগে",
            arabicTitle = "قَبْلَ النَّوْم",
            description = "The sleeping adhkar",
            descriptionBn = "ঘুমানোর আযকার",
            entries = listOf(
                entry(RoutineId.BEFORE_SLEEP, 1, "quran-ayatul-kursi", 1),
                entry(RoutineId.BEFORE_SLEEP, 2, "quran-baqarah-285-286", 1),
                entry(RoutineId.BEFORE_SLEEP, 3, "quran-ikhlas", 3),
                entry(RoutineId.BEFORE_SLEEP, 4, "quran-falaq", 3),
                entry(RoutineId.BEFORE_SLEEP, 5, "quran-nas", 3),
            ),
        ),
    )

    private val byId: Map<RoutineId, RoutineDefinition> = items.associateBy { it.id }

    val all: List<RoutineDefinition> get() = items

    fun byId(id: RoutineId): RoutineDefinition? = byId[id]
}
