// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {

    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central repository
        maven("https://jitpack.io")
    }
}

plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.devtools.ksp") version "1.8.0-1.0.8" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id ("io.realm.kotlin") version "1.11.0" apply false

}