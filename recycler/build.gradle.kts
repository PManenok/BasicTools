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
val libVersion: String = rootProject.extra.get("recyclerLib").toString()

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
    // RecyclerView
    api(libs.recyclerview)
    // Data binding
    kapt(libs.databinding)
    // Material Design
    api(libs.material)
    api(libs.constraintlayout)
    //Module
    api(project(":dimen_util"))
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