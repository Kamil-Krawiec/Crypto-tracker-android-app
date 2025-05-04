pluginManagement {
    repositories { gradlePluginPortal(); google(); mavenCentral() }
    plugins {
        kotlin("android") version "1.9.10"
        kotlin("kapt")    version "1.9.10"
        id("com.android.application")            version "8.2.0"
        id("androidx.navigation.safeargs.kotlin") version "2.6.0"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Crypto tracker"
include(":app")