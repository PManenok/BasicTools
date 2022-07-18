package by.esas.tools.simple

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.databinding.ViewDataBinding
import by.esas.tools.App
import by.esas.tools.BR
import by.esas.tools.basedaggerui.factory.InjectingViewModelFactory
import by.esas.tools.baseui.standard.StandardActivity
import by.esas.tools.hideSystemUIApp
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.util.TAGk
import by.esas.tools.util.configs.SettingsProvider
import by.esas.tools.util.configs.UiModeType
import by.esas.tools.util.hideSystemUIR
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
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
abstract class AppActivity<VM : AppVM, B : ViewDataBinding> : StandardActivity<VM, B, ErrorModel>(),
    HasAndroidInjector {

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any?>

    override fun androidInjector(): AndroidInjector<Any?>? {
        return androidInjector
    }

    override var logger: ILogger<ErrorModel> = LoggerImpl()

    override fun provideErrorHandler(): ErrorHandler<ErrorModel> {
        return object : ErrorHandler<ErrorModel>() {

            override fun getErrorMessage(error: ErrorModel): String {
                return "Error message"
            }

            override fun getErrorMessage(e: Throwable): String {
                return "Error message"
            }

            override fun mapError(e: Throwable): ErrorModel {
                return viewModel.provideMapper().mapErrorException(this@AppActivity.TAGk, e)
            }
        }
    }

    override fun provideVariableInd(): Int {
        return BR.viewModel
    }

    override fun provideSetter(): SettingsProvider {
        return object : SettingsProvider {
            override fun getDefaultLanguage(): String {
                return "en"
            }

            override fun getLanguage(): String {
                return App.language
            }

            override fun setLanguage(lang: String) {
                App.language = lang
            }

            override fun getDefaultMode(): UiModeType {
                return UiModeType.SYSTEM
            }

            override fun getMode(): UiModeType {
                return App.uiMode
            }

            override fun setMode(uiMode: UiModeType) {
                App.uiMode = uiMode
            }
        }
    }

    override fun hideSystemUI() {
        logger.logInfo("hideSystemUI")
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                hideSystemUIR(this)
            } else {
                hideSystemUIApp(this)
            }
        } catch (e: NullPointerException) {
            logger.logError(e)
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

    //CHECK if should be in base class
    fun setFullScreen() {
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                //clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                } else {
                    decorView.systemUiVisibility =
                        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
                }
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    open fun handleStatusBar() {
        logger.logInfo("handleStatusBar")
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBar = insets.systemWindowInsetTop
            logger.logInfo("statusBarHeight $statusBar")
            binding.root.updatePadding(top = statusBar)
            return@setOnApplyWindowInsetsListener insets
        }
    }
}