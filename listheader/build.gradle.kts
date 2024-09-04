plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("maven-publish")
}

val packageName: String by rootProject.extra
val compileSdkVer: Int by rootProject.extra
val minSdkVersion: Int by rootProject.extra
val repoName: String by rootProject.extra
val libVersion: String = rootProject.extra.get("listheaderLib").toString()

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
    implementation(libs.kotlin.stdlib.jdk7)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    // Constraint Layout
    api(libs.constraintlayout)
    // Material Design
    api(libs.material)
    implementation(project(":util"))
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