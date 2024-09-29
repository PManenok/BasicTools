// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.safeargs) apply false
}

tasks.register("clean", Delete::class) {
    delete(getLayout().buildDirectory)
}

buildscript {
    extra.apply {
        set("compileSdkVer", 34)
        set("minSdkVersion", 21)
        set("packageName", "by.esas.tools")
        set("repoName", "basetools")

        //Lib versions
        set("loggerLib", "4.0.2") // independent (6)
        set("utilLib", "4.0.2") // independent (1)
        set("utilUiLib", "4.0.2") // independent (2)
        set("timeparserLib", "4.0.2") // independent
        set("pinviewLib", "4.0.2") // independent
        set("inputfieldviewLib", "4.0.2") // independent
        set("checkerLib", "4.0.2") // independent (3)
        set("numpadLib", "4.0.2")
        set("listheaderLib", "4.0.2")
        set("dimenUtilLib", "4.0.2") // independent (1)
        set("basedaggeruiLib", "4.0.2") // independent
        set("cardlineLib", "4.0.2")
        set("topbarviewLib", "4.0.2")
        set("customswitchLib", "4.0.2")

        set("recyclerLib", "4.0.2") // dimen_util
        set("domainLib", "4.0.2") // logger (2)
        set("errorMapperLib", "4.0.2")
        set("dialogCoreLib", "4.0.2") // logger, util, checker (2)
        set("dialogLib", "4.0.2") // dialogCoreLib(logger, util, checker (2))
        set(
            "dialogMessageLib",
            "4.0.2"
        ) // dialogLib(dialogCoreLib(logger, util, checker (2))), recycler(dimen_util)
        set("biometricLib", "4.0.2") //logger
        set("baseuiLib", "4.0.2")
        //dialog(logger, util, checker, recycler(dimen_util)), domain(logger)
        set("accesscontainerLib", "4.0.2") //logger
    }
}