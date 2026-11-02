# AGENTS.md

Kotlin Multiplatform app (Dhikr). Four Gradle modules (root `build.gradle.kts` loads all plugins with `apply false` — adding a module that re-applies Kotlin plugins without that breaks the build):
- `shared/` — dependency-free KMP library: `androidTarget()`, `jvm()`, `js(IR)`, `wasmJs()`. `ios*` targets are declared **conditionally** and compile **only on macOS** (guarded in `shared/build.gradle.kts`). Holds the dhikr catalog, routine definitions, engine (progress/shared goals/streaks), and a dependency-free state codec (`SavedStateCodec`).
- `composeUi/` — Compose Multiplatform UI library (`androidTarget()` + `jvm("desktop")`) consumed by both apps. Platform services (dates, haptics, backup pickers, version) are injected via the `AppPlatform` interface / `LocalAppPlatform`; java APIs are fine here since there are no JS/Wasm targets.
- `androidApp/` — Android app (Kotlin + Compose, plugins `kotlinAndroid` + `composeCompiler` required; AGP alone ignores `src/main/kotlin`). Provides `AndroidPlatform` and the system `BackHandler`.
- `desktopApp/` — Compose Desktop app for Windows (see below).

## Commands
- Use `gradlew` (`gradlew.bat`), **not** the system `gradle`.
  - `.\gradlew :shared:jvmTest` — run the shared unit tests (the only runnable test suite on Windows).
  - `.\gradlew :shared:compileKotlinJs :shared:compileKotlinWasmJs` — verify web targets.
  - `.\gradlew :androidApp:assembleDebug` — debug APK (`androidApp/build/outputs/apk/debug/androidApp-debug.apk`).
  - `.\gradlew :androidApp:assembleRelease` — signed, R8-minified release APK (`androidApp/build/outputs/apk/release/androidApp-release.apk`). Signing reads the gitignored `keystore.properties` + `keystore/dhikr-release.jks`; without them it falls back to debug signing.
  - `.\gradlew :desktopApp:createDistributable` — self-contained Windows app image (`desktopApp/build/compose/binaries/main/app/Dhikr/`, contains `Dhikr.exe` + bundled JRE).
- JS/Wasm are browser-only; do NOT run Kotlin browser tests here (no Node/browser harness configured). Compile, don't test.

## Environment gotchas (non-obvious)
- **No system Java.** The build is pinned to `org.gradle.java.home=C:\Users\mazha\.jdks\temurin-21` in `gradle.properties`. If that path disappears, `.\gradlew` fails with "JAVA_HOME is not set". Bare shells (and agents) must also `export JAVA_HOME=C:/Users/mazha/.jdks/temurin-21` *before* invoking `gradlew` — the wrapper itself needs a JVM; `org.gradle.java.home` only applies after Gradle starts.
- **AGP/KGP version warning is expected and harmless.** AGP 8.7.3 is above KGP 2.0.21's tested max (8.5). Ignore the "higher than the maximum known" warning unless it turns into an error.
- **JVM target is pinned to 17** in `shared`, `composeUi`, `desktopApp`, and `androidApp` (each build file pins `KotlinJvmCompile`). Do not let Kotlin default to 21 — it fails with "Inconsistent JVM-target compatibility".
- `android.useAndroidX=true` is required (set in `gradle.properties`).

## Conventions / gotchas
- `expect`/`actual` `Platform` class is **Beta** in Kotlin 2.0 — the warning is expected; leave it.
- Adding a `nodejs()` target reintroduces the `nodejs.org` dist-repo conflict with `PREFER_SETTINGS`. Keep JS/Web browser-only.
- `local.properties` (ignored) points `sdk.dir` at the Android SDK — needed for Android builds but not for shared-only tasks.
- **`shared/` must stay dependency-free and platform-neutral.** No `String.format` / `java.time` in its commonMain (they don't exist on JS/Wasm; use `DayKeys`-style helpers). On wasmJs, JS interop uses `@kotlin.JsFun("...")` on `external fun`s. `composeUi/` has no JS targets, so it may use `java.io`/`java.text` in commonMain.
- **Expect classes consumed by other KMP modules**: `expect class SystemClock() : Clock` must declare its interface members (`override fun ...`) in the header, and actuals mark them `actual override` — otherwise `:shared:compileCommonMainKotlinMetadata` fails once another multiplatform module (composeUi) consumes shared.
- **Qur'an text is generated, never hand-edited**: run `python tools/gen_quran_kt.py` (env `QURAN_JSON_DIR` pointing at quran.com API JSON dumps) to regenerate `shared/.../content/QuranTexts.kt`; `ContentTest` asserts FNV-1a fingerprints against the verified dataset.
- **App icons are generated from `icon.png`** (repo root, the master artwork): run `python tools/gen_icons.py` (needs Pillow) to rewrite the Android mipmaps/adaptive icon and `desktopApp/icons/dhikr.ico` + window icon. Don't hand-edit the outputs.
- Release signing never gets committed: `keystore.properties` and `keystore/` are gitignored; back the keystore up — losing it means the app can never be updated under the same signing key.

## Verification order
shared builds are fast and dependency-free — run `:shared:jvmTest` first for any `shared/` change, then `:androidApp:assembleDebug` (Android breakage), then `:desktopApp:assemble` (desktop breakage), then `:shared:compileKotlinJs :shared:compileKotlinWasmJs`.
