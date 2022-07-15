package by.esas.tools.simple

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import by.esas.tools.App
import by.esas.tools.BR
import by.esas.tools.basedaggerui.factory.InjectingViewModelFactory
import by.esas.tools.baseui.standard.StandardFragment
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.util.TAGk
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

    override var logger: ILogger<*> = LoggerImpl()

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
                return "Error message"
            }

            override fun getErrorMessage(e: Throwable): String {
                return "Error message"
            }

            override fun mapError(e: Throwable): ErrorModel {
                return viewModel.provideMapper().mapErrorException(this.TAGk, e)
            }
        }
    }

    override fun provideSwitchableViews(): List<View?> {
        logger.logInfo("provideSwitchableViews")
        return emptyList()
    }
}