plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.safeargs)
}
apply("../properties.gradle")

val packageName = project.properties["package_name"].toString()
android {
    namespace = "by.esas.tools"
    compileSdk = project.properties["compile_sdk_version"] as Int?
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = project.properties["min_sdk_version"] as Int?
        applicationId = "by.esas.tools"
        targetSdk = project.properties["compile_sdk_version"] as Int?
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
        }
    }
}

dependencies {
    implementation("androidx.multidex:multidex:2.0.1")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    //App
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.biometric:biometric:1.1.0")

    // Data binding
    kapt("com.android.databinding:compiler:3.1.4")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.8.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")

    // Network
    //implementation "com.squareup.retrofit2:retrofit:2.6.4"
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Room
    api("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")

    // Dagger 2
    //REMEMBER dagger is applied with api so it will be available in app module
    //implementation "com.google.dagger:dagger:$dagger2_version"
    //implementation "com.google.dagger:dagger-android:$dagger2_version"
    //implementation "com.google.dagger:dagger-android-support:$dagger2_version"
    kapt("com.google.dagger:dagger-compiler:2.41")
    kapt("com.google.dagger:dagger-android-processor:2.41")

    implementation(
        project(":baseui"))
                implementation (project(":basedaggerui"))
                implementation (project(":domain"))
                implementation (project(":dialog"))
                implementation (project(":checker"))
                implementation (project(":inputfieldview"))
                implementation (project(":pinview"))
                implementation (project(":timeparser"))
                implementation (project(":numpad"))
                implementation (project(":util"))
                implementation (project(":listheader"))
                implementation (project(":customswitch"))
                implementation (project(":topbarview"))
                implementation (project(":cardline"))
                implementation (project(":biometric_decryption"))
                implementation (project(":accesscontainer"))
}
