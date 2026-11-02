package com.hikmah.dhikr

actual class Platform actual constructor() {
    actual val name: String = "JVM ${System.getProperty("java.version")}"
}
