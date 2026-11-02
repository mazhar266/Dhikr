# AGENTS.md

Kotlin Multiplatform app (Dhikr). Two Gradle modules:
- `shared/` — KMP library: `androidTarget()`, `jvm()`, `js(IR)`, `wasmJs()`. `ios*` targets are declared **conditionally** and compile **only on macOS** (guarded in `shared/build.gradle.kts`). Holds the dhikr catalog, routine definitions, engine (progress/shared goals/streaks), and a dependency-free state codec (`SavedStateCodec`).
- `androidApp/` — Android app consuming `shared`: Kotlin + Jetpack Compose (plugins `kotlinAndroid` + `composeCompiler` are required on this module; AGP alone ignores `src/main/kotlin`).

## Commands
- Use `gradlew` (`gradlew.bat`), **not** the system `gradle`.
  - `.\gradlew :shared:jvmTest` — run the shared unit tests (the only runnable test suite on Windows).
  - `.\gradlew :shared:compileKotlinJs :shared:compileKotlinWasmJs` — verify web targets.
  - `.\gradlew :androidApp:assembleDebug` — build the APK (output: `androidApp/build/outputs/apk/debug/androidApp-debug.apk`).
- JS/Wasm are browser-only; do NOT run Kotlin browser tests here (no Node/browser harness configured). Compile, don't test.

## Environment gotchas (non-obvious)
- **No system Java.** The build is pinned to `org.gradle.java.home=C:\Users\mazha\.jdks\temurin-21` in `gradle.properties`. If that path disappears, `.\gradlew` fails with "JAVA_HOME is not set". Bare shells (and agents) must also `export JAVA_HOME=C:/Users/mazha/.jdks/temurin-21` *before* invoking `gradlew` — the wrapper itself needs a JVM; `org.gradle.java.home` only applies after Gradle starts.
- **AGP/KGP version warning is expected and harmless.** AGP 8.7.3 is above KGP 2.0.21's tested max (8.5). Ignore the "higher than the maximum known" warning unless it turns into an error.
- **JVM target is pinned to 17** for both the shared Android/Kotlin and the app (see `shared/build.gradle.kts` and `androidApp/build.gradle.kts`). Do not let Kotlin default to 21 — it fails with "Inconsistent JVM-target compatibility".
- `android.useAndroidX=true` is required (set in `gradle.properties`).

## Conventions / gotchas
- `expect`/`actual` `Platform` class is **Beta** in Kotlin 2.0 — the `expect/actual classes are in Beta` warning is expected; leave it, don't "fix" by adding flags.
- Adding a `nodejs()` target reintroduces the `nodejs.org` dist-repo conflict with `PREFER_SETTINGS`. Keep JS/Web browser-only.
- `local.properties` (already committed locally, ignored via its own line) points `sdk.dir` at the Android SDK — needed for Android builds but not for shared-only tasks.
- **Common code must stay dependency-free and platform-neutral.** No `String.format` / `java.time` in commonMain (they don't exist on JS/Wasm; use `DayKeys`-style helpers). On wasmJs, JS interop uses `@kotlin.JsFun("...")` on `external fun`s (not `kotlin.js.ExperimentalWasmJsInterop`, which doesn't exist in 2.0.21).
- **Qur'an text is generated, never hand-edited**: run `python tools/gen_quran_kt.py` (env `QURAN_JSON_DIR` pointing at quran.com API JSON dumps) to regenerate `shared/.../content/QuranTexts.kt`; `ContentTest` asserts FNV-1a fingerprints against the verified dataset.

## Verification order
shared builds are fast and dependency-free — run `:shared:jvmTest` first for any `shared/` change, then `:androidApp:assembleDebug` to catch Android-specific breakage.
