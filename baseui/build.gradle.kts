plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    id("maven-publish")
}
apply("../properties.gradle")

val packageName = project.properties["package_name"].toString()
android {
    namespace = "${packageName}.${project.name}"
    compileSdk = project.properties["compile_sdk_version"] as Int?

    defaultConfig {
        minSdk = project.properties["min_sdk_version"] as Int?
        multiDexEnabled = true
    }
    buildFeatures {
        dataBinding = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}


dependencies {
    implementation("androidx.multidex:multidex:2.0.1")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")

    // App
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.core:core-ktx:1.7.0")

    // Data binding
    kapt("com.android.databinding:compiler:3.1.4")

    // Material Design
    api("com.google.android.material:material:1.5.0")

    /* REMEMBER com.google.dagger:dagger-android-support lib contains lifecycle-viewmodel-ktx lib
        and thus we need to set version otherwise there will be duplicate error */
    // Saved state module for ViewModel
    //implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    //implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

    // Navigation
    api("androidx.navigation:navigation-fragment-ktx:2.5.0")
    api("androidx.navigation:navigation-ui-ktx:2.5.0")

    api(project(":dialog"))
    api(project(":domain"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = packageName
            artifactId = project.name
            version = project.properties["baseui_lib_version"].toString()
            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = "basetools"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}