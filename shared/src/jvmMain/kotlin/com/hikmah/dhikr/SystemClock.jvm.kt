package com.hikmah.dhikr

import java.time.LocalDate

actual class SystemClock actual constructor() : Clock {
    override fun nowMillis(): Long = System.currentTimeMillis()

    override fun currentDayKey(): String = LocalDate.now().toString()
}
