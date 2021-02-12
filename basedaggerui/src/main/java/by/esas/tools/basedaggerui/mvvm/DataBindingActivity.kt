package by.esas.tools.basedaggerui.mvvm

import android.os.Bundle
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.basic.BaseActivity
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.handler.ErrorData
import by.esas.tools.logger.handler.ErrorHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder


abstract class DataBindingActivity<TViewModel : BaseViewModel<E, M>, TBinding : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    BaseActivity<E>() {

    protected lateinit var binding: TBinding

    protected lateinit var viewModel: TViewModel

    abstract fun provideViewModel(): TViewModel
    abstract fun provideErrorHandler(): ErrorHandler<E, M>
    abstract fun provideLayoutId(): Int
    open fun provideProgressBar(): ProgressBar? {
        return null
    }

    abstract fun provideVariableInd(): Int

    abstract fun provideLifecycleOwner(): LifecycleOwner

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(this).setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = provideViewModel()
        viewModel.errorData.observe(this, Observer { data ->
            handleError(data)
        })

        binding = DataBindingUtil.setContentView(this, provideLayoutId())
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()
    }

    protected open fun handleError(data: ErrorData<E, M>?) {
        logger.logInfo("try to handleError ${data != null} && !${data?.handled}")
        if (data != null && !data.handled) {
            val msg = when {
                data.throwable != null -> provideErrorHandler().getErrorMessage(data.throwable!!)
                data.model != null -> provideErrorHandler().getErrorMessage(data.model!!)
                else -> "Error"
            }
            viewModel.showError(msg, data.showType, provideMaterialAlertDialogBuilder(), data.doOnDialogOK)
            data.handled = true
        }
    }
}
