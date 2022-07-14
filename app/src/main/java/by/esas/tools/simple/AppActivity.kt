package by.esas.tools.simple

import android.content.Context
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import by.esas.tools.App
import by.esas.tools.BR
import by.esas.tools.basedaggerui.factory.InjectingViewModelFactory
import by.esas.tools.baseui.standard.StandardActivity
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.util.SettingsProvider
import by.esas.tools.util.TAGk
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
                return "en"
            }

            override fun setLanguage(lang: String) {

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