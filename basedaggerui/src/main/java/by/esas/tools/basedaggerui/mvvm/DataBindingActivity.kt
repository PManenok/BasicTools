/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.mvvm

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.R
import by.esas.tools.basedaggerui.basic.BaseActivity
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.handler.ErrorData
import by.esas.tools.logger.handler.ErrorHandler
import by.esas.tools.logger.handler.ShowErrorType
import com.google.android.material.dialog.MaterialAlertDialogBuilder


abstract class DataBindingActivity<TViewModel : BaseViewModel<E, M>, TBinding : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    BaseActivity<E>() {
    companion object {
        val TAG: String = DataBindingActivity::class.java.simpleName
    }

    protected lateinit var binding: TBinding
    protected lateinit var viewModel: TViewModel

    abstract fun provideViewModel(): TViewModel

    abstract fun provideErrorHandler(): ErrorHandler<E, M>

    abstract fun provideLayoutId(): Int

    abstract fun provideVariableInd(): Int

    abstract fun provideLifecycleOwner(): LifecycleOwner

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(
            this,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
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
            showError(msg, data.showType, data.actionName)
            data.handled = true
        }
    }

    open fun showError(msg: String, showType: ShowErrorType, actionName: String? = null) {
        viewModel.hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING -> viewModel.enableControls()
            ShowErrorType.SHOW_ERROR_DIALOG -> {
                provideMaterialAlertDialogBuilder().setTitle(R.string.error_title)
                    .setMessage(msg)
                    .setPositiveButton(R.string.common_ok_btn) { dialogInterface, _ ->
                        dialogInterface?.dismiss()
                        viewModel.enableControls()
                        viewModel.handleAction(actionName)
                    }.create().show()
            }
            ShowErrorType.SHOW_ERROR_MESSAGE -> {
                logger.showMessage(msg)
                viewModel.enableControls()
                viewModel.handleAction(actionName)
            }
        }
    }
}
