buildscript {

    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
        maven(url = "https://dl.google.com/dl/android/maven2")
        maven(url = "https://oss.jfrog.org/libs-snapshot")
        mavenCentral()

    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.1.3")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN_PLGUIN}") // 코틀린 플러그인 적용
        classpath ("com.google.gms:google-services:${Versions.GOOGLE_GMS}")
        classpath ("io.realm:realm-gradle-plugin:${Versions.REALM_PLUGIN}")
        classpath ("com.neenbedankt.gradle.plugins:android-apt:${Versions.ANDROID_APT}")
        classpath ("io.fabric.tools:gradle:${Versions.FABRIC}")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${Versions.DAGGER_HILT}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION_SAFE}")

    }
}
allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.google.com")
        maven(url = "https://maven.fabric.io/public")
        maven(url = "https://jitpack.io")
        mavenCentral()

    }
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}