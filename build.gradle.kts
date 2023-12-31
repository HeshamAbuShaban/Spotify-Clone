buildscript {
    dependencies {
        //..kotlinOfficialWebsite
        classpath(kotlin("gradle-plugin", version = "1.9.0"))

        val nav_version = "2.7.2"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")

        //..GoogleMaps Or Firebase i guess.
        classpath("com.google.gms:google-services:4.3.15")

        // Dagger Hilt
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        /*classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")*/
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "1.9.0" apply false

    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    /*// ksp
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false*/
    //..Dagger Hilt
    id("com.google.dagger.hilt.android") version "2.48" apply false
}