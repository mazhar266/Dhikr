package fi.mazhar.dhikr

import java.time.LocalDate

actual class SystemClock actual constructor() : Clock {
    actual override fun nowMillis(): Long = System.currentTimeMillis()

    actual override fun currentDayKey(): String = LocalDate.now().toString()
}
