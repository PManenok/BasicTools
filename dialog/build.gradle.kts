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
val libVersion: String = rootProject.extra.get("dialogLib").toString()

android {
    namespace = "${packageName}.${project.name}"
    compileSdk = compileSdkVer
    defaultConfig {
        minSdk = minSdkVersion
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
    implementation(libs.kotlin.stdlib)

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    // Material Design
    api(libs.material)
    api(libs.constraintlayout)

    // Data binding
    kapt(libs.databinding)

    //Module
    api(project(":logger"))
    api(project(":util"))
    api(project(":checker"))
    api(project(":recycler"))
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