plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
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

    /* REMEMBER com.google.dagger:dagger-android-support lib contains lifecycle-viewmodel-ktx lib
        and thus we need to set version otherwise there will be duplicate error */
    // Saved state module for ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    // Dagger 2
    api( "com.google.dagger:dagger:2.41")
    api ("com.google.dagger:dagger-android:2.41")
    api ("com.google.dagger:dagger-android-support:2.41")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = packageName
            artifactId = project.name
            version = project.properties["basedaggerui_lib_version"].toString()
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
