package fi.mazhar.dhikr

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

@OptIn(ExperimentalForeignApi::class)
actual class SystemClock actual constructor() : Clock {
    override fun nowMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

    override fun currentDayKey(): String {
        val comps = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            NSDate(),
        ) ?: return ""
        return comps.useContents {
            pad(year.toInt(), 4) + "-" + pad(month.toInt() + 1, 2) + "-" + pad(day.toInt(), 2)
        }
    }
}

private fun pad(n: Int, width: Int): String = n.toString().padStart(width, '0')
