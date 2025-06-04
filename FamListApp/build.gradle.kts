// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
true // Needed to make the build file valid

// Define versions in a central place
object Versions {
    const val KOTLIN = "1.9.0" // Example Kotlin version
    const val COMPOSE = "1.5.4" // Example Compose version
    // Add other versions here
}
