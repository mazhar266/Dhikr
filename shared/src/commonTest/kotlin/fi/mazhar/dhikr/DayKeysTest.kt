package fi.mazhar.dhikr

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DayKeysTest {
    @Test
    fun epochDay_roundTrip() {
        val keys = listOf(
            "1970-01-01", "2026-11-02", "2000-02-29", "1900-03-01",
            "2024-12-31", "2100-03-01",
        )
        for (key in keys) {
            assertEquals(key, DayKeys.fromEpochDay(DayKeys.toEpochDay(key)), key)
        }
    }

    @Test
    fun addDays_acrossMonthAndYear() {
        assertEquals("2026-12-01", DayKeys.addDays("2026-11-30", 1))
        assertEquals("2027-01-01", DayKeys.addDays("2026-12-31", 1))
        assertEquals("2025-01-01", DayKeys.addDays("2024-12-31", 1))
        assertEquals("2024-02-29", DayKeys.addDays("2024-02-28", 1))
        assertEquals("2024-03-01", DayKeys.addDays("2024-02-29", 1))
        assertEquals("2023-03-01", DayKeys.addDays("2023-02-28", 1))
        assertEquals("2026-11-01", DayKeys.addDays("2026-11-02", -1))
        assertEquals("1970-01-01", DayKeys.addDays("1969-12-31", 1))
    }

    @Test
    fun diffDays() {
        assertEquals(1L, DayKeys.diffDays("2026-11-02", "2026-11-03"))
        assertEquals(-366L, DayKeys.diffDays("2025-01-01", "2024-01-01"))
    }

    @Test
    fun validity() {
        assertTrue(DayKeys.isValid("2026-11-02"))
        assertFalse(DayKeys.isValid("2026-02-30"))
        assertFalse(DayKeys.isValid("2026-13-01"))
        assertFalse(DayKeys.isValid("2026-11"))
        assertFalse(DayKeys.isValid("not-a-day"))
    }
}
