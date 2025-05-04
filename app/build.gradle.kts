// app/build.gradle.kts  ── module ---------------------------------------------
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")        // Hilt / Dagger
    id("com.google.devtools.ksp")          // Room & Moshi
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.cryptotracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cryptotracker"
        minSdk        = 24
        targetSdk     = 35
        versionCode   = 1
        versionName   = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
}

dependencies {
    /* ---------- AndroidX core & UI ---------- */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    /* ---------- Navigation ---------- */
    implementation(libs.androidx.navigation.fragment.ktx.v280)
    implementation(libs.androidx.navigation.ui.ktx.v280)

    /* ---------- Room (KSP) ---------- */
    implementation(libs.androidx.room.runtime.v250)
    implementation(libs.androidx.room.ktx.v250)
    ksp(libs.androidx.room.compiler.v250)

    /* ---------- Moshi (KSP) ---------- */
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    /* ---------- Dagger / Hilt (kapt) ---------- */
    kapt(libs.hilt.compiler)

    /* ---------- Retrofit + OkHttp ---------- */
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    /* ---------- Coroutines ---------- */
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android.v173)

    /* ---------- Lifecycle / ViewModel ---------- */
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    /* ---------- WorkManager ---------- */
    implementation(libs.androidx.work.runtime.ktx)

    /* ---------- Coil ---------- */
    implementation(libs.coil)

    /* ---------- Tests ---------- */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}