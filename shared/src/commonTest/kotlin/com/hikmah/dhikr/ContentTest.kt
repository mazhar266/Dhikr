package com.hikmah.dhikr

import com.hikmah.dhikr.content.DhikrCatalog
import com.hikmah.dhikr.content.QuranChecksums
import com.hikmah.dhikr.content.Routines
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Proves that every item from the provided inventory exists in its intended
 * routine, in the correct order, with the requested repetition target.
 */
class ContentTest {

    private val fajrSequence = listOf(
        "allahu-akbar" to 1,
        "astaghfirullah" to 3,
        "takbir-tahlil-after-salam" to 1,
        "antas-salam" to 1,
        "ajirni-minan-nar" to 7,
        "quran-ayatul-kursi" to 1,
        "quran-hashr-59-22-24" to 1,
        "quran-kafirun" to 1,
        "quran-ikhlas" to 3,
        "quran-falaq" to 3,
        "quran-nas" to 3,
        "subhanallah" to 33,
        "alhamdulillah" to 33,
        "allahu-akbar" to 34,
        "salawat-ibrahimiyyah" to 10,
        "raditu-billahi" to 3,
        "hasbiyallahu" to 7,
        "audhu-kalimat-min-sharri" to 3,
        "audhu-kalimat-shaytan" to 1,
        "audhu-kalimat-ghadab" to 1,
        "bismillah-la-yadurru" to 3,
        "sayyidul-istighfar" to 1,
        "afini-badani" to 3,
        "la-ilaha-illallah-goal" to 10,
        "subhanallahi-wabihamdihi-100" to 100,
        "istighfar-100" to 100,
        "hammi-wal-hazan" to 1,
        "ya-hayyu-ya-qayyum" to 1,
        "ilman-nafian" to 1,
    )

    @Test
    fun catalog_idsAreUnique() {
        val ids = DhikrCatalog.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun catalog_entriesAreComplete() {
        for (d in DhikrCatalog.all) {
            assertTrue(d.arabic.isNotBlank(), "${d.id}: arabic blank")
            assertTrue(d.transliteration.isNotBlank(), "${d.id}: transliteration blank")
            assertTrue(d.translation.isNotBlank(), "${d.id}: translation blank")
            assertTrue(d.title.isNotBlank(), "${d.id}: title blank")
        }
    }

    @Test
    fun quranText_matchesVerifiedDatasetFingerprints() {
        // FNV-1a fingerprints generated alongside the verse text from the
        // quran.com uthmani dataset; any edit to the Arabic breaks these.
        val expected = mapOf(
            "quran-ayatul-kursi" to QuranChecksums.AYATUL_KURSI,
            "quran-baqarah-285-286" to QuranChecksums.BAQARAH_285_286,
            "quran-hashr-59-22-24" to QuranChecksums.HASHR_22_24,
            "quran-kafirun" to QuranChecksums.AL_KAFIRUN,
            "quran-ikhlas" to QuranChecksums.AL_IKHLAS,
            "quran-falaq" to QuranChecksums.AL_FALAQ,
            "quran-nas" to QuranChecksums.AN_NAS,
        )
        for ((id, checksum) in expected) {
            val arabic = DhikrCatalog.byId(id)!!.arabic
            assertEquals(checksum, fnv1a(arabic), id)
            assertTrue(arabic.length > 50, id)
        }
    }

    /** FNV-1a 64-bit over UTF-8 bytes (must match tools/gen_quran_kt.py). */
    private fun fnv1a(s: String): Long {
        var h = -3750763034362895579L // 0xCBF29CE484222325
        for (b in s.encodeToByteArray()) {
            h = h xor (b.toLong() and 0xff)
            h *= 1099511628211L
        }
        return h
    }

    @Test
    fun quranEntries_areReciteOnce() {
        for (id in listOf(
            "quran-ayatul-kursi", "quran-hashr-59-22-24", "quran-baqarah-285-286",
            "quran-kafirun", "quran-ikhlas", "quran-falaq", "quran-nas",
        )) {
            val d = DhikrCatalog.byId(id)!!
            assertEquals(DhikrKind.RECITE_ONCE, d.kind, id)
            assertEquals(DhikrCategory.QURAN, d.category, id)
        }
    }

    @Test
    fun sixRoutines_existInOrder() {
        assertEquals(
            listOf("FAJR", "DHUHR", "ASR", "MAGHRIB", "ISHA", "BEFORE_SLEEP"),
            Routines.all.map { it.id.name },
        )
    }

    @Test
    fun fajr_matchesInventoryExactly() {
        val fajr = Routines.byId(RoutineId.FAJR)!!
        assertEquals(fajrSequence.map { it.first }, fajr.entries.map { it.dhikrId })
        assertEquals(fajrSequence.map { it.second }, fajr.entries.map { it.target })
        assertEquals(1, fajr.entries.first().key.order)
        assertEquals(fajr.entries.size, fajr.entries.last().key.order)
    }

    @Test
    fun asr_matchesInventoryExactly() {
        val asr = Routines.byId(RoutineId.ASR)!!
        val expected = fajrSequence.take(23) +
            listOf("la-ilaha-illallah-goal" to 10) +
            fajrSequence.drop(24).dropLast(1)
        assertEquals(expected.map { it.first }, asr.entries.map { it.dhikrId })
        assertEquals(28, asr.entries.size)
        // Item 24 in Asr is the conditional mirror of the shared goal.
        val conditional = asr.entries[23]
        assertTrue(conditional.conditionalOnGoal)
        assertEquals(Routines.LA_ILA_GOAL_ID, conditional.sharedGoalId)
        assertEquals(Routines.LA_ILA_GOAL_ID, asr.entries[23].sharedGoalId)
    }

    @Test
    fun shortRoutines_matchInventory() {
        val expected = listOf(
            "quran-ayatul-kursi" to 1,
            "subhanallah" to 33,
            "alhamdulillah" to 33,
            "allahu-akbar" to 34,
            "salawat-ibrahimiyyah" to 10,
        )
        for (id in listOf(RoutineId.DHUHR, RoutineId.MAGHRIB, RoutineId.ISHA)) {
            val routine = Routines.byId(id)!!
            assertEquals(expected.map { it.first }, routine.entries.map { it.dhikrId }, id.name)
            assertEquals(expected.map { it.second }, routine.entries.map { it.target }, id.name)
        }
    }

    @Test
    fun beforeSleep_matchesInventory() {
        val sleep = Routines.byId(RoutineId.BEFORE_SLEEP)!!
        assertEquals(
            listOf("quran-ayatul-kursi", "quran-baqarah-285-286", "quran-ikhlas", "quran-falaq", "quran-nas"),
            sleep.entries.map { it.dhikrId },
        )
        assertEquals(listOf(1, 1, 3, 3, 3), sleep.entries.map { it.target })
    }

    @Test
    fun everyEntryReferencesExistingDhikr() {
        for (routine in Routines.all) {
            for (entry in routine.entries) {
                assertTrue(
                    DhikrCatalog.byId(entry.dhikrId) != null,
                    "${routine.id}-${entry.key.order} references unknown dhikr '${entry.dhikrId}'",
                )
            }
        }
    }

    @Test
    fun sharedGoal_isWiredToFajrAndAsr() {
        assertEquals(
            listOf(RoutineId.FAJR, RoutineId.ASR),
            Routines.routinesForGoal(Routines.LA_ILA_GOAL_ID),
        )
        // Only the Asr entry is conditional.
        val conditionalEntries = Routines.all
            .flatMap { it.entries }
            .filter { it.conditionalOnGoal }
        assertEquals(1, conditionalEntries.size)
        assertEquals(RoutineId.ASR, conditionalEntries[0].key.routineId)
    }

    @Test
    fun needsVerification_isLimitedToDocumentedItems() {
        val unverified = DhikrCatalog.all
            .filter { it.verification == VerificationStatus.NEEDS_VERIFICATION }
            .map { it.id }
        assertEquals(listOf("takbir-tahlil-after-salam"), unverified)
    }
}
