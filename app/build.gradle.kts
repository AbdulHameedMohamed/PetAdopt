plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
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

    dynamicFeatures.add(":features:sharing")
}

dependencies {

    api(project(":common"))
    api(project(":logging"))
    implementation(project(":features:animals"))
    implementation(project(":features:search"))
    implementation(project(":features:onboarding"))

    api(libs.jetbrains.kotlin.jvm)
    api(libs.androidx.appcompat)
    api(libs.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Utils
    implementation(libs.jakewharton.threetenabp)

    // Networking
    implementation(libs.squareup.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)
    implementation(libs.room)
    ksp(libs.room.compiler)

    // Modules
    implementation(libs.rx.java)
    implementation(libs.rx.kotlin)
    implementation(libs.rx.android)
    implementation(libs.rx.room)

    //DI
    implementation(libs.hilt)
    implementation(libs.hilt.multidex)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.kapt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    // Logging Timber
    implementation(libs.timber)

    // Navigation
    api(libs.androidx.navigation.fragment.ktx)
    api(libs.androidx.navigation.ui.ktx)
    api(libs.navigation.dynamic.feature)

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