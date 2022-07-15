import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-android")
    id ("kotlin-kapt")
    kotlin("kapt")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation ("javax.inject:javax.inject:1")

    /** { KTX } - jhm 2022/04/27 **/
    implementation(KTX.CORE)

    /** { AndroidX } - jhm 2022/04/27 **/
    implementation(AndroidX.APPCOMPAT)

    /** { Google Material } - jhm 2022/04/27 **/
    implementation(Google.MATERIAL)


    /** { Test } - jhm 2022/04/22 **/
    testImplementation(Test.JUNIT)
    androidTestImplementation(Test.TEST_RUNNER)
    androidTestImplementation(Test.EXT_JUNIT)
    androidTestImplementation(Test.ESPRESSO_CORE)

    /** { RxJava2 } - jhm 2022/04/22 **/
    implementation(RxJava2.ANDROID)
    implementation(RxJava2.JAVA)
    implementation(RxJava2.RETROFIT)
    implementation(RxJava2.BINDING)

    /** {RxJava3} - jhm 2022/04/22 **/
    implementation(RxJava3.JAVA)
    implementation(RxJava3.KOTLIN)
    implementation(RxJava3.ANDROID)
    implementation(RxJava3.BINDING)
    implementation(RxJava3.RETROFIT_ADAPTER)

    /** { Dagger } - jhm 2022/04/29 **/
    implementation(Dagger.HILT)
    implementation(Dagger.HILT_VIEW_MODEL)
    //kapt(Dagger.COMPILER)


    /** { Koin } - jhm 2022/04/26 **/
    implementation(Koin.ANDROIDX)
    implementation(Koin.TEST)
    implementation(Koin.ANDROID)
    implementation(Koin.COMPAT)
    implementation(Koin.WORKMANAGER)
    implementation(Koin.NAVIGATION)
    implementation(Koin.COMPOSE)
    //implementation(Koin.KOIN_VIEWMODEL)

    /** { Google Gson } - jhm 2022/05/03 **/
    implementation(Google.GSON)
}