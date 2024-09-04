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
val libVersion: String = rootProject.extra.get("domainLib").toString()

android {
    namespace = "${packageName}.${project.name}"
    compileSdk = compileSdkVer
    defaultConfig {
        minSdk = minSdkVersion
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //App
    implementation(libs.core.ktx)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)

    // Network
    api(libs.retrofit)
    implementation(libs.okhttp3)

    //Module
    api(project(":logger"))
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