plugins {
    id(BuildPlugins.androidLibrary)
    kotlin("android")
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExt)
    id(BuildPlugins.kotlinKapt)
    kotlin("kapt")
}

android {
    compileSdkVersion(App.compileSdkVersion)

    defaultConfig {
        minSdkVersion(App.minSdkVersion)
        targetSdkVersion(App.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    dataBinding {
        isEnabled = true
    }
}


dependencies {
    implementation(Libs.appCompat)
    implementation(Libs.coreKtx)
    implementation(Libs.constraintLayout)
    implementation(BuildPlugins.kotlinStdLib)
    testImplementation(TestLibs.junit)

    implementation(Libs.epoxy)
    implementation(Libs.epoxyDataBinding)
    kapt(Libs.epoxyProcessor)
}
