plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("maven-publish")
}

val packageName: String by rootProject.extra
val compileSdkVer: Int by rootProject.extra
val minSdkVersion: Int by rootProject.extra
val repoName: String by rootProject.extra
val libVersion: String = rootProject.extra.get("dialogCoreLib").toString()

android {
    namespace = "${packageName}.${project.name}"
    compileSdk = compileSdkVer
    defaultConfig {
        minSdk = minSdkVersion
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
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    api(libs.appcompat)
    // Material Design
    api(libs.material)
    //Module
    api(project(":logger"))
    api(project(":util"))
    api(project(":util_ui"))
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