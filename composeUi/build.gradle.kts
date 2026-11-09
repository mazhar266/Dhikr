import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidLibrary)
}

afterEvaluate {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

android {
    namespace = "fi.mazhar.dhikr.composeui"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

// Shared Compose UI consumed by the Android app, the Windows (desktop) app,
// and the iOS app (via the "Dhikr" framework built by iosApp's Xcode project).
// commonMain must stay platform-neutral (no java.*/android.*) because the iOS
// targets compile it on macOS — it is iOS-clean today; keep it that way.
kotlin {
    androidTarget()
    jvm("desktop")

    // iOS compiles only on macOS (same guard as shared/build.gradle.kts).
    val isMac = System.getProperty("os.name")?.startsWith("Mac", ignoreCase = true) == true
    if (isMac) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
        // The "Dhikr" framework embedded by iosApp's Xcode project; Xcode's
        // embedAndSignAppleFrameworkForXcode task picks device vs simulator.
        targets.withType<KotlinNativeTarget>().configureEach {
            binaries.framework {
                baseName = "Dhikr"
                isStatic = true
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":shared"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
        }
    }
}
