package by.esas.tools.baseui.basic

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import by.esas.basictools.base.interfaces.IBaseActivity
import by.esas.basictools.utils.extra.LanguageSetter
import by.esas.basictools.utils.logger.BaseLogger
import by.esas.tools.baseui.IBaseActivity
import by.hgrosh.data.sharedprefs.AppSharedPrefs
import by.hgrosh.domain.util.ILogger
import by.hgrosh.notary.app.App
import by.hgrosh.notary.utils.logger.LoggerImpl
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : AppCompatActivity(), IBaseActivity {
    abstract val TAG: String

    var logger: ILogger = LoggerImpl()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(doWithAttachBaseContext(base))
    }

    /* fun setLanguage(lang: String = "") {
         Logger.logInfo(TAG, "Changing language to $lang")
         val prefs = AppSharedPrefs(EposApplication.instance)
         val prevLang = prefs.getLanguage()
         Logger.logInfo(TAG, "Previous (current) language is $prevLang")
         val newLang: String = if (lang.isNotBlank()) lang else "en"
         if (newLang != prevLang) {
             prefs.setLanguage(lang)
             EposApplication.appContext = LocaleManager.setLocale(EposApplication.instance)
             Logger.logInfo(TAG, "Activity recreate")
             recreate()
         }
     }*/

    override fun provideTAG(): String = TAG

    override fun provideLogger(): BaseLogger {
        return logger as LoggerImpl
    }

    override fun provideSetter(): LanguageSetter {
        return object : LanguageSetter {
            val prefs: AppSharedPrefs = AppSharedPrefs(App.instance)
            override fun getDefaultLanguage(): String {
                return prefs.defaultLang
            }

            override fun getLanguage(): String {
                return prefs.getLanguage()
            }

            override fun setLanguage(lang: String) {
                prefs.setLanguage(lang)
            }
        }
    }

    override fun recreateActivity() {
        recreate()
    }

    //Add appContext in your Application class and return it here
    override fun getAppContext(): Context = applicationContext

    override fun setAppContext(context: Context) {
        //Add appContext in your Application class and reset it here
        //App.appContext = context
    }
}

