package by.esas.tools.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import by.esas.tools.App
import by.esas.tools.BR
import by.esas.tools.app_data.AppSharedPrefs
import by.esas.tools.basedaggerui.factory.InjectingViewModelFactory
import by.esas.tools.baseui.standard.StandardActivity
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorMessageHelper
import by.esas.tools.utils.logger.ErrorModel
import by.esas.tools.utils.logger.LoggerImpl
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import java.util.*
import javax.inject.Inject

/**
 * To use HasAndroidInjector with Activity do not forget to add
 *     AndroidInjection.inject(this)
 * in the fun onCreate(savedInstanceState: Bundle?)
 *
 * To use HasAndroidInjector with Fragment do not forget to add
 *     override fun onAttach(context: Context) {
 *         AndroidSupportInjection.inject(this)
 *         super.onAttach(context)
 *     }
 */
abstract class AppActivity<VM : AppVM, B : ViewDataBinding>
    : StandardActivity<VM, B, ErrorModel>(),
    HasAndroidInjector {

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any?>

    override fun androidInjector(): AndroidInjector<Any?>? {
        return androidInjector
    }

    override var logger: ILogger<ErrorModel> = LoggerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun provideSwitchableViews(): List<View?> {
        return emptyList()
    }

    override fun provideErrorStringHelper(): ErrorMessageHelper<ErrorModel> {
        logger.logOrder("provideErrorStringHelper")
        return object : ErrorMessageHelper<ErrorModel>() {

            override fun getErrorMessage(error: ErrorModel): String {
                return error.status
            }
        }
    }

    override fun provideVariableInd(): Int {
        return BR.viewModel
    }

    override fun provideSetter(): by.esas.tools.util_ui.configs.SettingsProvider {
        return object : by.esas.tools.util_ui.configs.SettingsProvider {
            var prefs: AppSharedPrefs = AppSharedPrefs(App.instance)

            override fun getDefaultLanguage(): String {
                return "en"
            }

            override fun getLanguage(): String {
                return prefs.getLanguage()
            }

            override fun setLanguage(lang: String) {
                prefs.setLanguage(lang)
            }

            override fun getDefaultMode(): by.esas.tools.util_ui.configs.UiModeType {
                return by.esas.tools.util_ui.configs.UiModeType.SYSTEM
            }

            override fun getMode(): by.esas.tools.util_ui.configs.UiModeType {
                return prefs.getTheme()
            }

            override fun setMode(uiMode: by.esas.tools.util_ui.configs.UiModeType) {
                prefs.setTheme(uiMode)
            }
        }
    }

    override fun getAppContext(): Context {
        return App.appContext
    }

    override fun setAppContext(context: Context) {
        App.appContext = context
    }

    /**
     * @see IHandlePopBackArguments
     * */
    override fun handlePopBackArguments(arguments: Bundle?) {
        if (arguments != null)
            intent.putExtras(arguments)
    }
}