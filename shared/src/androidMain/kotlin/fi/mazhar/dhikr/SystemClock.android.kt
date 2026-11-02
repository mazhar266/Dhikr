package fi.mazhar.dhikr

import java.util.Calendar
import java.util.Locale

actual class SystemClock actual constructor() : Clock {
    actual override fun nowMillis(): Long = System.currentTimeMillis()

    /**
     * java.util.Calendar re-reads the device timezone on every call, so the
     * day key follows local midnight across timezone and DST changes.
     */
    actual override fun currentDayKey(): String {
        val cal = Calendar.getInstance()
        return "%04d-%02d-%02d".format(
            Locale.US,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
        )
    }
}
