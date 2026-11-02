package com.hikmah.dhikr

/**
 * Pure "yyyy-MM-dd" day-key arithmetic (no platform date APIs), used for
 * streak computation and rollover tests. Implements Howard Hinnant's
 * civil-date <-> days-since-epoch algorithms.
 */
object DayKeys {

    private val PATTERN = Regex("""^(\d{4})-(\d{2})-(\d{2})$""")

    fun isValid(dayKey: String): Boolean {
        val m = PATTERN.find(dayKey) ?: return false
        val (y, mo, d) = m.destructured
        if (mo.toInt() !in 1..12) return false
        return d.toInt() in 1..daysInMonth(y.toInt(), mo.toInt())
    }

    fun toEpochDay(dayKey: String): Long {
        require(isValid(dayKey)) { "Invalid day key: $dayKey" }
        val y = dayKey.substring(0, 4).toInt()
        val m = dayKey.substring(5, 7).toInt()
        val d = dayKey.substring(8, 10).toInt()
        val yy = y - if (m <= 2) 1 else 0
        val era = (if (yy >= 0) yy else yy - 399) / 400
        val yoe = yy - era * 400
        val mp = (m + 9) % 12
        val doy = (153 * mp + 2) / 5 + d - 1
        val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
        return era * 146097L + doe - 719468L
    }

    fun fromEpochDay(epochDay: Long): String {
        val z = epochDay + 719468L
        val era = (if (z >= 0) z else z - 146096) / 146097
        val doe = z - era * 146097
        val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
        val y = yoe + era * 400
        val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
        val mp = (5 * doy + 2) / 153
        val d = doy - (153 * mp + 2) / 5 + 1
        val m = if (mp < 10) mp + 3 else mp - 9
        val yy = y + if (m <= 2) 1 else 0
        return pad(yy.toInt(), 4) + "-" + pad(m.toInt(), 2) + "-" + pad(d.toInt(), 2)
    }

    private fun pad(n: Int, width: Int): String = n.toString().padStart(width, '0')

    fun addDays(dayKey: String, days: Long): String = fromEpochDay(toEpochDay(dayKey) + days)

    fun diffDays(from: String, to: String): Long = toEpochDay(to) - toEpochDay(from)

    private fun daysInMonth(year: Int, month: Int): Int = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        else -> if (isLeapYear(year)) 29 else 28
    }

    private fun isLeapYear(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
}
