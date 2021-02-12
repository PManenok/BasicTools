package by.esas.tools.basedaggerui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.R
import by.esas.tools.basedaggerui.basic.BaseFragment
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.handler.ErrorData
import by.esas.tools.logger.handler.ErrorHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class DataBindingFragment<VM : BaseViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    BaseFragment<E>() {

    protected lateinit var binding: B

    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM

    abstract fun provideErrorHandler(): ErrorHandler<E, M>

    abstract fun provideLayoutId(): Int

    abstract fun provideSwitchableViews(): List<View?>

    abstract fun provideLifecycleOwner(): LifecycleOwner

    abstract fun provideVariableInd(): Int

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            context,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.errorData.observe(viewLifecycleOwner, Observer { data ->
            handleError(data)
        })
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.setTag(TAG)
        logger.logInfo("onCreateView")

        viewModel = provideViewModel()

        binding = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.logInfo("onViewCreated")
        viewModel.switchableViewsList = { provideSwitchableViews() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyView")
    }
}