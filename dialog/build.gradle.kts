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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    // Material Design
    api("com.google.android.material:material:1.5.0")
    api("androidx.constraintlayout:constraintlayout:2.1.3")

    // Data binding
    kapt("com.android.databinding:compiler:3.1.4")

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
            version = project.properties["dialog_lib_version"].toString()
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