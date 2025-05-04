// settings.gradle.kts  ── root -------------------------------------------------
pluginManagement {
    repositories { gradlePluginPortal(); google(); mavenCentral() }
    plugins {
        id("com.android.application")            version "8.9.2"          // AGP preview
        id("org.jetbrains.kotlin.android")       version "2.1.10"         // Kotlin plugin  [oai_citation:4‡Gradle Plugins](https://plugins.gradle.org/plugin/org.jetbrains.kotlin.android/1.9.10?utm_source=chatgpt.com)
        id("com.google.devtools.ksp")             version "2.1.10-1.0.30"  // KSP for Kotlin 1.9.x  [oai_citation:5‡Maven Repository](https://mvnrepository.com/artifact/com.google.devtools.ksp/com.google.devtools.ksp.gradle.plugin?repo=central&utm_source=chatgpt.com) [oai_citation:6‡Maven Repository](https://mvnrepository.com/artifact/com.google.devtools.ksp/com.google.devtools.ksp.gradle.plugin/1.9.10-1.0.13?utm_source=chatgpt.com)
        id("androidx.navigation.safeargs.kotlin") version "2.8.9"         // Safe-Args  [oai_citation:7‡Android Developers](https://developer.android.com/jetpack/androidx/releases/navigation?utm_source=chatgpt.com)
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}

rootProject.name = "Crypto tracker"
include(":app")