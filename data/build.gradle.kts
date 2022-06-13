plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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


    /** { data => domain 빌드 gradle 종속성 추가 } - jhm 2022/04/27 **/
    implementation(project(":domain"))

    /** { KTX } - jhm 2022/04/27 **/
    implementation(KTX.CORE)

    /** { AndroidX } - jhm 2022/04/27 **/
    implementation(AndroidX.APPCOMPAT)

    /** { Google Material } - jhm 2022/04/27 **/
    implementation(Google.MATERIAL)



    /**
     * @author jhm
     * @since 2022/03/25
     * @commnet Retrofit / okHttp / dagger implementation 적용
     **/

    /** { Retrofit2 & Okhttp3 } - jhm 2022/04/22 **/
    implementation(Retrofit.RETROFIT_2)
    implementation(Retrofit.RETROFIT_2_ADAPTER)
    implementation(Retrofit.RETROFIT_2_GSON)
    implementation(OkHttp.OKHTTP_3_INTERCEPTOR)
    implementation(Room.COMMON)
    implementation(KTX.ROOM)


    /** { Test } - jhm 2022/04/22 **/
    testImplementation(Test.JUNIT)
    androidTestImplementation(Test.TEST_RUNNER)
    androidTestImplementation(Test.EXT_JUNIT)
    androidTestImplementation(Test.ESPRESSO_CORE)


    /** { Koin } - jhm 2022/04/26 **/
    implementation(Koin.ANDROIDX)
    implementation(Koin.TEST)
    implementation(Koin.ANDROID)
    implementation(Koin.COMPAT)
    implementation(Koin.WORKMANAGER)
    implementation(Koin.NAVIGATION)
    implementation(Koin.COMPOSE)
    //implementation(Koin.KOIN_VIEWMODEL)

}