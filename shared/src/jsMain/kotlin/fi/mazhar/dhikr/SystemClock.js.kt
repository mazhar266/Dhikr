package fi.mazhar.dhikr

import kotlin.js.Date

actual class SystemClock actual constructor() : Clock {
    actual override fun nowMillis(): Long = Date().getTime().toLong()

    actual override fun currentDayKey(): String {
        val d = Date()
        return pad4(d.getFullYear()) + "-" + pad2(d.getMonth() + 1) + "-" + pad2(d.getDate())
    }
}

private fun pad2(n: Int): String = n.toString().padStart(2, '0')
private fun pad4(n: Int): String = n.toString().padStart(4, '0')
