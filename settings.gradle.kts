// settings.gradle.kts  ── root -------------------------------------------------
pluginManagement {
    repositories { gradlePluginPortal(); google(); mavenCentral() }
    plugins {
        id("com.android.application")             version "8.9.0"
        id("org.jetbrains.kotlin.android")        version "1.9.10"
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
        id("com.google.devtools.ksp")             version "1.9.10-1.0.13"
        id("com.google.dagger.hilt.android")      version "2.49"
        id("androidx.navigation.safeargs.kotlin") version "2.8.9"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}

rootProject.name = "Crypto tracker"
include(":app")