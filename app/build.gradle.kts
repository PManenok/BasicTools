plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.safeargs)
}

val packageName: String by rootProject.extra
val compileSdkVer: Int by rootProject.extra
val minSdkVersion: Int by rootProject.extra

android {
    namespace = "by.esas.tools"
    compileSdk = compileSdkVer
    buildToolsVersion = "34.0.0"

    defaultConfig {
        minSdk = minSdkVersion
        applicationId = "by.esas.tools"
        targetSdk = compileSdkVer
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(libs.multidex)

    // Kotlin
    implementation(libs.kotlinx.coroutines.android)

    //App
    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.biometric)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // Network
    //implementation "com.squareup.retrofit2:retrofit:2.6.4"
    implementation(libs.okhttp3)

    // Room
    api(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    // Dagger 2
    //REMEMBER dagger is applied with api so it will be available in app module
    //implementation "com.google.dagger:dagger:$dagger2_version"
    //implementation "com.google.dagger:dagger-android:$dagger2_version"
    //implementation "com.google.dagger:dagger-android-support:$dagger2_version"
    kapt(libs.dagger.compiler)
    kapt(libs.dagger.android.processor)

    implementation(project(":baseui"))
    implementation(project(":basedaggerui"))
    implementation(project(":domain"))
    implementation(project(":dialog"))
    implementation(project(":checker"))
    implementation(project(":inputfieldview"))
    implementation(project(":pinview"))
    implementation(project(":timeparser"))
    implementation(project(":numpad"))
    implementation(project(":util"))
    implementation(project(":listheader"))
    implementation(project(":customswitch"))
    implementation(project(":topbarview"))
    implementation(project(":cardline"))
    implementation(project(":biometric_decryption"))
    implementation(project(":accesscontainer"))
}
