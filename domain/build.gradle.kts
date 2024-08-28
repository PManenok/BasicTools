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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    // Kotlin
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    //App
    implementation ("androidx.core:core-ktx:1.7.0")

    // Moshi
    implementation ("com.squareup.moshi:moshi:1.8.0")
    implementation ("com.squareup.moshi:moshi-adapters:1.8.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.8.0")
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")

    // Network
    api ("com.squareup.retrofit2:retrofit:2.7.2")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")

    //Module
    api (project(":logger"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = packageName
            artifactId = project.name
            version = project.properties["domain_lib_version"].toString()
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