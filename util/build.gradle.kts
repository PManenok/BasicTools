plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("maven-publish")
}

//apply from: "../properties.gradle"

android {
    namespace = "by.esas.tools.util"//"${package_name}.${project.name}"

    compileSdk =34//project.properties["compile_sdk_version"].toString()

    defaultConfig {
        minSdk =19//min_sdk_version
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.4.1")
}
tasks.register < Jar > (name = "sourceJar") {
    from(android.sourceSets["main"].java.srcDirs)
    classifier = "sources"
}
/*publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "${package_name}"
            artifactId = "${project.name}"
            version = "$util_lib_version"
            //artifact("$buildDir/outputs/aar/module-name-release.aar")
            artifact(tasks["sourceJar"])
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}*/

/*afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            release(MavenPublication) {
                from components.release
                groupId = "${package_name}"
                artifactId = "${project.name}"
                version = "$util_lib_version"
            }
        }
    }
}*/
