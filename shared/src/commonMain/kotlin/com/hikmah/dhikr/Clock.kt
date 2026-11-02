package com.hikmah.dhikr

/**
 * Time source for the engine. Abstracted so daily-reset logic can be tested
 * deterministically. The engine treats "today" as the caller-supplied day key;
 * the wall clock is only used for completedAt stamps and by [DhikrRepository].
 */
interface Clock {
    /** Current wall-clock time in epoch millis. */
    fun nowMillis(): Long

    /** A stable identifier for "today" (e.g. "2026-11-02"). Deterministic per local date. */
    fun currentDayKey(): String
}

/** Fixed clock for tests. */
class FixedClock(
    private var dayKey: String,
    private var now: Long = 0L,
) : Clock {
    override fun nowMillis(): Long = now
    override fun currentDayKey(): String = dayKey

    /** Advance the fake day (for rollover / streak tests). */
    fun advanceDay() {
        dayKey = DayKeys.addDays(dayKey, 1)
    }

    fun setDay(dayKey: String, now: Long = 0L) {
        this.dayKey = dayKey
        this.now = now
    }
}

/**
 * Platform wall clock. Day keys follow the *device-local* calendar date, so
 * they roll over at local midnight and stay correct across timezone/DST
 * changes (each call re-reads the current zone).
 */
expect class SystemClock() : Clock
