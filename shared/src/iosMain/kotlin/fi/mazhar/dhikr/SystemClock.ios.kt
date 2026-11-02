package fi.mazhar.dhikr

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual class SystemClock actual constructor() : Clock {
    actual override fun nowMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()

    actual override fun currentDayKey(): String {
        // NSDateComponents is an ObjC class, so its fields are read directly
        // (unlike the C-struct useContents pattern). month is already 1-based.
        val comps = NSCalendar.currentCalendar.components(
            NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            NSDate(),
        ) ?: return ""
        return pad(comps.year.toInt(), 4) + "-" +
            pad(comps.month.toInt(), 2) + "-" +
            pad(comps.day.toInt(), 2)
    }
}

private fun pad(n: Int, width: Int): String = n.toString().padStart(width, '0')
