plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.example.petadopt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.petadopt"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.petadopt.HiltTestRunner"
    }

    sourceSets {
        getByName("androidTest") {
            assets.srcDirs("src/debug/assets")
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":logging"))
    implementation(project(":common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Modules
    implementation(libs.rx.java)
    implementation(libs.rx.kotlin)
    implementation(libs.rx.android)
    implementation(libs.rx.room)

    //DI
    implementation(libs.hilt)
    implementation(libs.hilt.multidex)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.kapt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    // Logging Timber
    implementation(libs.timber)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Coroutine
    implementation(libs.room.coroutine)

    // Pagination
    implementation(libs.room.paging)

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockwebserver)
    androidTestImplementation(libs.mockwebserver)
    testImplementation(libs.mockito)
    testImplementation(libs.truth)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.core.testing)
}