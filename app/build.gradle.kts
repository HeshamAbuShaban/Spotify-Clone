plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //..Firebase
    id("com.google.gms.google-services")
    // for navigation safe args
    id("androidx.navigation.safeargs")
    /*// KSP Plugins
    id("com.google.devtools.ksp")*/
    //..DaggerHilt
    id("com.google.dagger.hilt.android")
    // Kapt
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "dev.training.spotify_clone"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.training.spotify_clone"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    hilt { enableAggregatingTask = true }
}

dependencies {

    /*//  KotlinOfficialWebsite
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.0-1.0.13")*/


    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Media3
    implementation("androidx.media3:media3-common:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")

    // Material Design
    implementation("com.google.android.material:material:1.9.0")

    // Architectural Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Activity KTX for viewModels()
    implementation("androidx.activity:activity-ktx:1.7.2")

    /*// TO-DO :Dagger Doesn't support KSP instead use KAPT as annotation prosper
    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")*/

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    /*
    // additionally.. optional
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt("androidx.hilt:hilt-compiler:1.1.0-alpha01")
    */

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore:24.7.1")

    // Firebase Storage KTX
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")

    // Firebase Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // ShitFrom Assess-tin
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.1")

    // ExoPlayer
    api("com.google.android.exoplayer:exoplayer-core:2.19.1")
    api("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    api("com.google.android.exoplayer:extension-mediasession:2.19.1")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")
}

kapt {
    correctErrorTypes = true
}