# Dhikr

A quiet, offline-first daily adhkar companion. Six structured routines — Fajr, Dhuhr, Asr, Maghrib, Isha, and Before Sleep — with verified Arabic text, transliteration, translation, gentle repetition counters, persistent daily progress, and a configurable tahlil target.

No account, no internet, no ads.

## Features

- **Six routines** following a personal daily inventory (29 entries for Fajr, 28 for Asr, 5 each for Dhuhr/Maghrib/Isha and Before Sleep)
- **Distraction-free reader** with large scalable Arabic, pronunciation (Latin or বাংলা), translation in English or Bangla, sources, and a one-hand counter with haptics and undo
- **Bangla**: Settings → Language switches the app, the meaning, and the pronunciation
- **Persistent local progress** that resets at local midnight and keeps history; streaks and a 14-day overview
- **Tahlil target** of 10× each in Fajr and Asr, or 100× in Fajr only
- **Four themes**: Light, Sofia Paper (warm parchment), Dark (navy), and OLED (true black)
- **Backup & restore** to a plain file via the Android share sheet
- Gregorian + Hijri (Umm al-Qura) dates

## Tech

Kotlin Multiplatform. A dependency-free, JVM-tested core (`shared/`: catalog, routines, engine, state codec) and a shared Compose Multiplatform UI (`composeUi/`) power three apps: the Android app (`androidApp/`), a Windows desktop app (`desktopApp/`), and an iOS app (`iosApp/` — an Xcode project over the `Dhikr` framework built from `composeUi`). Web (JS/WasmJS) targets of the core compile too.

```
.\gradlew :shared:jvmTest                    # unit tests
.\gradlew :androidApp:assembleRelease        # signed release APK
.\gradlew :desktopApp:createDistributable    # Windows app (Dhikr.exe + bundled runtime)
```

### iOS

Requires a Mac with Xcode 15+ (the `ios*` Gradle targets only register on macOS):

1. `./gradlew :composeUi:linkDebugFrameworkIosSimulatorArm64` — first full compile of the iOS graph (the Xcode build re-runs this via `embedAndSignAppleFrameworkForXcode` automatically).
2. Open `iosApp/iosApp.xcodeproj`, select the `iosApp` target → *Signing & Capabilities*, and pick your team (a free Apple ID works for personal installs; free provisioning expires after 7 days, TestFlight/App Store needs the paid Apple Developer Program).
3. Choose an iPhone simulator or device and press Cmd+R.

Qur'anic passages are generated verbatim from the [quran.com](https://quran.com) API uthmani (Tanzil-verified) dataset by `tools/gen_quran_kt.py`, with fingerprint integrity tests. See [DHIKR_CONTENT_REVIEW.md](DHIKR_CONTENT_REVIEW.md) for source-by-source verification status; entries that still need scholarly review are flagged in-app rather than presented as authenticated Sunnah.

## Developer

**Mazhar Ahmed**
BA in Islamic Studies at IOU
Dawra-e-Hadith from Qawmi Madrasa, Bangladesh
