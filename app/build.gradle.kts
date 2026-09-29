import java.io.File
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")

}
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

val keyAlias = localProperties.getProperty("keyAlias", "")
val keyPassword = localProperties.getProperty("keyPassword", "")
val storePassword = localProperties.getProperty("storePassword", "")
val storeFilePath = localProperties.getProperty("storeFile", "")
val razorpayKey = localProperties.getProperty("razorpayKey", "")

android {
    namespace = "com.mysanjeevni.mysanjeevni"
    compileSdk {
        version = release(36)
    }
    signingConfigs {
        create("release") {
            if (storeFilePath.isNotEmpty()) {
                storeFile = file(storeFilePath)
            }
            this.storePassword = storePassword
            this.keyAlias = keyAlias
            this.keyPassword = keyPassword
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "RAZORPAY_KEY", "\"$razorpayKey\"")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            buildConfigField("String", "RAZORPAY_KEY", "\"$razorpayKey\"")
        }
    }

    defaultConfig {
        applicationId = "com.mysanjeevni.mysanjeevni"
        minSdk = 25
        targetSdk = 36
        versionCode = 13
        versionName = "1.0.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.compose.foundation)
    implementation(libs.ui)
    implementation(libs.androidx.hilt.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.7.7")

//    hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
//    Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    // room
    implementation("androidx.room:room-runtime:2.7.1")
    implementation("androidx.room:room-ktx:2.7.1")
    kapt("androidx.room:room-compiler:2.7.1")
    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:20.0.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

//    razorpay
    implementation("com.razorpay:checkout:1.6.26")

//    firebase
    implementation(platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-auth-ktx:23.2.1")
//    google auth
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    implementation("com.hbb20:ccp:2.7.0")
//    ml kit translator
    implementation("com.google.mlkit:translate:17.0.3")

    //    Pagination
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")
    implementation("androidx.paging:paging-compose:3.3.0")

//    animation
    implementation("androidx.compose.animation:animation-graphics:1.6.0")

//    work-manager
    implementation("androidx.work:work-runtime-ktx:2.10.1")
//    Material Icon
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

//    agora
    implementation("io.agora.rtc:full-sdk:4.5.1")

}

