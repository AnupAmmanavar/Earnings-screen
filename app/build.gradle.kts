plugins {
    id(BuildPlugins.androidApplication)
    kotlin("android")
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExt)
    id(BuildPlugins.kotlinKapt)
    kotlin("kapt")
}

android {
    compileSdkVersion(App.compileSdkVersion)

    defaultConfig {
        applicationId = "com.kinley.earnings"
        minSdkVersion(App.minSdkVersion)
        targetSdkVersion(App.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}


dependencies {

    implementation(project(":ui"))

    implementation(BuildPlugins.kotlinStdLib)
    implementation(Libs.appCompat)
    implementation(Libs.coreKtx)
    implementation(Libs.constraintLayout)
    implementation(Libs.epoxy)
    annotationProcessor(Libs.epoxyProcessor)

    testImplementation(TestLibs.junit)

    androidTestImplementation(InstrumentationTestLibs.junit)
    androidTestImplementation(InstrumentationTestLibs.espressoCore)
}
