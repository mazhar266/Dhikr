package com.hikmah.dhikr

import kotlin.JsFun

actual class SystemClock actual constructor() : Clock {
    override fun nowMillis(): Long = jsNow().toLong()

    override fun currentDayKey(): String = jsDayKey()
}

/** Local-date day key, built in JS so the browser timezone is respected. */
@JsFun(
    "() => { const d = new Date(); const p = (n) => n < 10 ? '0' + n : '' + n;" +
        " return d.getFullYear() + '-' + p(d.getMonth() + 1) + '-' + p(d.getDate()); }",
)
private external fun jsDayKey(): String

@JsFun("() => Date.now()")
private external fun jsNow(): Double
