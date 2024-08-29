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
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")

    implementation ("androidx.appcompat:appcompat:1.4.1")
    // Constraint Layout
    api ("androidx.constraintlayout:constraintlayout:2.1.3")

    // Material Design
    api ("com.google.android.material:material:1.5.0")

    implementation (project( ":util"))
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = packageName
            artifactId = project.name
            version = project.properties["cardline_lib_version"].toString()
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