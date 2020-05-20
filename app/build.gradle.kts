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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {

    implementation(project(":ui"))

    implementation(BuildPlugins.kotlinStdLib)
    implementation(Libs.appCompat)
    implementation(Libs.coreKtx)
    implementation(Libs.constraintLayout)

    testImplementation(TestLibs.junit)

    implementation(Libs.viewModelKtx)
    implementation(Libs.lifecycleKtx)
    implementation(Libs.activityKtx)

    implementation(Libs.coroutinesCore)
    implementation(Libs.coroutinesAndroid)

    implementation(Libs.epoxy)
    kapt(Libs.epoxyProcessor)



    androidTestImplementation(InstrumentationTestLibs.junit)
    androidTestImplementation(InstrumentationTestLibs.espressoCore)
}
