// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.safeargs) apply false

    id("maven-publish")
}

tasks.register("clean", Delete::class) {
    delete(getLayout().buildDirectory)
}

buildscript {
    extra.apply {
        set("compileSdkVer", 34)
        set("minSdkVersion", 19)
        set("packageName", "by.esas.tools")
        set("repoName", "basetools")

        //Lib versions
        set("loggerLib", "3.1.0") // independent (6)
        set("utilLib", "3.1.0") // independent (3)
        set("timeparserLib", "3.1.0") // independent
        set("pinviewLib", "3.1.0") // independent
        set("inputfieldviewLib", "3.1.0") // independent
        set("checkerLib", "3.1.0") // independent (3)
        set("numpadLib", "3.1.0")
        set("listheaderLib", "3.1.0")
        set("dimenUtilLib", "3.1.0") // independent (1)
        set("basedaggeruiLib", "3.1.0") // independent
        set("cardlineLib", "3.1.0")
        set("topbarviewLib", "3.1.0")
        set("customswitchLib", "3.1.0")

        set("recyclerLib", "3.1.0") // dimen_util
        set("domainLib", "3.1.0") // logger (2)
        set("dialogLib", "3.1.0") // logger, util, checker (2), recycler(dimen_util)
        set("biometricLib", "3.1.0") //logger
        set("baseuiLib", "3.1.0")
        //dialog(logger, util, checker, recycler(dimen_util)), domain(logger)
        set("accesscontainerLib", "3.1.0") //logger
    }
}