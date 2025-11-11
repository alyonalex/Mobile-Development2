plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "ru.mirea.krasikova.greenguide"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.mirea.krasikova.greenguide"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(project(":domain"))
    implementation(project(":data"))

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Библиотека фрагментов
    implementation ("androidx.fragment:fragment-ktx:1.8.3")

    //Навигация между фрагментами
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.3")

}