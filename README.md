# Dhikr

A quiet, offline-first daily adhkar companion. Six structured routines — Fajr, Dhuhr, Asr, Maghrib, Isha, and Before Sleep — with verified Arabic text, transliteration, translation, gentle repetition counters, persistent daily progress, and an optional shared daily goal between Fajr and Asr.

No account, no internet, no ads.

## Features

- **Six routines** following a personal daily inventory (29 entries for Fajr, 28 for Asr, 5 each for Dhuhr/Maghrib/Isha and Before Sleep)
- **Distraction-free reader** with large scalable Arabic, optional transliteration/translation, sources, and a one-hand counter with haptics and undo
- **Persistent local progress** that resets at local midnight and keeps history; streaks and a 14-day overview
- **Shared daily goal** for the "La ilaha illallah" 10×/100× tasbih (Fajr ↔ Asr), or fully independent counting
- **Four themes**: Light, Sofia Paper (warm parchment), Dark (navy), and OLED (true black)
- **Backup & restore** to a plain file via the Android share sheet
- Gregorian + Hijri (Umm al-Qura) dates

## Tech

Kotlin Multiplatform (`shared/`: Android, JVM, JS, WasmJs; iOS targets compile on macOS) with a dependency-free, JVM-tested core (catalog, routines, engine, state codec) and a Jetpack Compose Android app.

```
.\gradlew :shared:jvmTest          # unit tests
.\gradlew :androidApp:assembleDebug  # debug APK
```

Qur'anic passages are generated verbatim from the [quran.com](https://quran.com) API uthmani (Tanzil-verified) dataset by `tools/gen_quran_kt.py`, with fingerprint integrity tests. See [DHIKR_CONTENT_REVIEW.md](DHIKR_CONTENT_REVIEW.md) for source-by-source verification status; entries that still need scholarly review are flagged in-app rather than presented as authenticated Sunnah.

## Developer

**Mazhar Ahmed**
BA in Islamic Studies at IOU
Dawra-e-Hadith from Qawmi Madrasa, Bangladesh
