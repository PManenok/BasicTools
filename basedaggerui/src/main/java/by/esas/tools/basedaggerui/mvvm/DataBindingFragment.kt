/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

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
import by.esas.tools.logger.handler.ShowErrorType
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