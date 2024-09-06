plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    id("maven-publish")
}

val packageName: String by rootProject.extra
val compileSdkVer: Int by rootProject.extra
val minSdkVersion: Int by rootProject.extra
val repoName: String by rootProject.extra
val libVersion: String = rootProject.extra.get("baseuiLib").toString()

android {
    namespace = "${packageName}.${project.name}"
    compileSdk = compileSdkVer
    defaultConfig {
        minSdk = minSdkVersion
        multiDexEnabled = true
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.multidex)
    // App
    implementation(libs.appcompat)
    implementation(libs.core.ktx)

    // Material Design
    api(libs.material)

    /* REMEMBER com.google.dagger:dagger-android-support lib contains lifecycle-viewmodel-ktx lib
        and thus we need to set version otherwise there will be duplicate error */
    // Saved state module for ViewModel
    //implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    //implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

    // Navigation
    api(libs.navigation.fragment.ktx)
    api(libs.navigation.ui.ktx)

    api(project(":dialog"))
    api(project(":domain"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = packageName
            artifactId = project.name
            version = libVersion
            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = repoName
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}