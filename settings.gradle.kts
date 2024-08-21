pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.google.com")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.google.com")
    }
}

rootProject.name = "BaseTools"
include(":app")
include(":recycler")
include(":util")
include(":checker")
include(":dialog")
include(":logger")
include(":biometric_decryption")
include(":pinview")
include(":timeparser")
include(":accesscontainer")
include(":inputfieldview")
include(":baseui")
include(":basedaggerui")
include(":domain")
include(":listheader")
include(":dimen_util")
include(":numpad")
include(":cardline")
include(":topbarview")
include(":customswitch")

