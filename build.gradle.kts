// build.gradle.kts  ── project root -------------------------------------------
plugins {
    id("com.android.application")             version "8.9.2"  apply false
    id("org.jetbrains.kotlin.android")        version "1.9.10" apply false
    id("com.google.devtools.ksp")             version "1.9.10-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.0"     apply false
}