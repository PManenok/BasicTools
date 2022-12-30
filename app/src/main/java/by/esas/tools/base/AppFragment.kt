package by.esas.tools.base

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import by.esas.tools.App
import by.esas.tools.BR
import by.esas.tools.R
import by.esas.tools.basedaggerui.factory.InjectingViewModelFactory
import by.esas.tools.baseui.standard.StandardFragment
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.BaseLoggerImpl
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.util.TAGk
import by.esas.tools.utils.logger.ErrorModel
import by.esas.tools.utils.logger.LoggerImpl
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class AppFragment<VM : AppVM, B : ViewDataBinding> :
    StandardFragment<VM, B, ErrorModel>(),
    HasAndroidInjector {

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any?>

    override fun androidInjector(): AndroidInjector<Any?>? {
        return androidInjector
    }

    override var logger: ILogger<*> = BaseLoggerImpl(context = provideAppContext())

    override fun provideAppContext(): Context {
        return App.appContext
    }

    override fun provideVariableInd(): Int {
        return BR.viewModel
    }

    override fun provideErrorHandler(): ErrorHandler<ErrorModel> {
        logger.logInfo("provideErrorHandler")
        return object : ErrorHandler<ErrorModel>() {

            override fun getErrorMessage(error: ErrorModel): String {
                return error.statusEnum
            }

            override fun getErrorMessage(e: Throwable): String {
                return e.message ?: resources.getString(R.string.test_error)
            }

            override fun mapError(e: Throwable): ErrorModel {
                return viewModel.provideMapper().mapErrorException(this.TAGk, e)
            }
        }
    }

    override fun provideChecks(): List<Checking> {
        return emptyList()
    }

    override fun provideChecker(): Checker? {
        return null
    }

    override fun provideSwitchableViews(): List<View?> {
        logger.logInfo("provideSwitchableViews")
        return emptyList()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }
}