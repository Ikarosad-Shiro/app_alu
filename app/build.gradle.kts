plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.aluasistencias"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aluasistencias"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.play.services.maps) // Google Maps
    implementation(libs.play.services.location) // FusedLocationProviderClient
    implementation(libs.appcompat.v161)
    implementation(libs.material.v190)
    implementation(libs.activity.ktx)
    implementation(libs.constraintlayout.v214)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.v115)
    androidTestImplementation(libs.espresso.core.v351)
}

