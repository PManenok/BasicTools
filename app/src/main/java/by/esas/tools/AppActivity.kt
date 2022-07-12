package by.esas.tools

import androidx.databinding.ViewDataBinding
import by.esas.tools.basedaggerui.factory.InjectingViewModelFactory
import by.esas.tools.baseui.standard.StandardActivity
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.handler.ErrorHandler
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
}