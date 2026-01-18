plugins {

    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.nereidaro.parksnro"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nereidaro.parksnro"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // --- ROOM ---
    val roomVersion = "2.6.1"
    // Runtime
    implementation("androidx.room:room-runtime:$roomVersion")
    // Procesador de anotaciones para Room (KAPT)
    kapt("androidx.room:room-compiler:$roomVersion")
    // Extensión de Kotlin para Room (incluye soporte para corrutinas)
    implementation("androidx.room:room-ktx:$roomVersion")
    // ------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
kapt {
    correctErrorTypes = true
}