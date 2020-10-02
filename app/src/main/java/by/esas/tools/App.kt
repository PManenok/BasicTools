package by.esas.tools

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import by.esas.tools.basedaggerui.app.BaseApp
import by.esas.tools.basedaggerui.app.BaseApp.Companion.appContext
import by.esas.tools.inject.component.DaggerAppComponent
import by.esas.tools.util.LocaleManager
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication(), BaseApp {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    companion object {
        lateinit var instance: App
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        //Slider.init(PicassoImageLoadingService(this))
        appContext = this.applicationContext

        //The following options are available with increasingly more information:
        //NONE, FATAL, ERROR, WARN, INFO, DEBUG, VERBOSE
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization FIXME uncomment
        /*OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()*/
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { return@let LocaleManager.setLocale(base, getLocale(base).language) })
        //MultiDex.install(this)
    }

}