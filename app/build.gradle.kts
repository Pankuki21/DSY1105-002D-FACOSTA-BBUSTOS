plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.myapplication"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.Lifecycle:lifecycle-viewmodel-compose:2.8.0-rc02")
    implementation("androidx. Lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform( dependencyProvider = Libs.androidx.compose.bom))
    implementation(libs.androidx.vi)
    implementation(libs.androidx.vi.graphics)
    implementation(libs.androidx.vi.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.androidx.navigation.runtime.android)
    //implementation(libs.androidx.navigation.compose. jvmstubs)
    implementation(libs.androidx.material3.window.size.class1.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform( dependencyProvider = libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.vi.test.junit4)
    debugImplementation(libs.androidx.vi.tooling)
    debugImplementation(libs.androidx.vi.test.manifest)
}