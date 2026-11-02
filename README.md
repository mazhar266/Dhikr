# Dhikr

A quiet, offline-first daily adhkar companion. Six structured routines — Fajr, Dhuhr, Asr, Maghrib, Isha, and Before Sleep — with verified Arabic text, transliteration, translation, gentle repetition counters, persistent daily progress, and an optional shared daily goal between Fajr and Asr.

No account, no internet, no ads.

## Features

- **Six routines** following a personal daily inventory (29 entries for Fajr, 28 for Asr, 5 each for Dhuhr/Maghrib/Isha and Before Sleep)
- **Distraction-free reader** with large scalable Arabic, pronunciation (Latin or বাংলা), translation in English or Bangla, sources, and a one-hand counter with haptics and undo
- **Bangla**: Settings → Language switches the app, the meaning, and the pronunciation
- **Persistent local progress** that resets at local midnight and keeps history; streaks and a 14-day overview
- **Shared daily goal** for the "La ilaha illallah" 10×/100× tasbih (Fajr ↔ Asr), or fully independent counting
- **Four themes**: Light, Sofia Paper (warm parchment), Dark (navy), and OLED (true black)
- **Backup & restore** to a plain file via the Android share sheet
- Gregorian + Hijri (Umm al-Qura) dates

## Tech

Kotlin Multiplatform. A dependency-free, JVM-tested core (`shared/`: catalog, routines, engine, state codec) and a shared Compose Multiplatform UI (`composeUi/`) power two apps: the Android app (`androidApp/`) and a Windows desktop app (`desktopApp/`). Web (JS/WasmJS) targets of the core compile too.

```
.\gradlew :shared:jvmTest                    # unit tests
.\gradlew :androidApp:assembleRelease        # signed release APK
.\gradlew :desktopApp:createDistributable    # Windows app (Dhikr.exe + bundled runtime)
```

Qur'anic passages are generated verbatim from the [quran.com](https://quran.com) API uthmani (Tanzil-verified) dataset by `tools/gen_quran_kt.py`, with fingerprint integrity tests. See [DHIKR_CONTENT_REVIEW.md](DHIKR_CONTENT_REVIEW.md) for source-by-source verification status; entries that still need scholarly review are flagged in-app rather than presented as authenticated Sunnah.

## Developer

**Mazhar Ahmed**
BA in Islamic Studies at IOU
Dawra-e-Hadith from Qawmi Madrasa, Bangladesh
