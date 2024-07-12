plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.crypto.recruitmenttest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.crypto.recruitmenttest"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":common:domain"))
    implementation(project(":common:ui"))
    implementation(project(":common:theme"))
    implementation(project(":features:currencies:domain"))
    implementation(project(":features:currencies:data"))
    implementation(project(":features:currencies:ui"))
}
