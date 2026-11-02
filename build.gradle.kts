// Load all plugins once at the root so subprojects (which use the same
// aliases without re-resolving classes) never load the Kotlin plugin twice.
plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.androidApp) apply false
    alias(libs.plugins.androidLibrary) apply false
}
