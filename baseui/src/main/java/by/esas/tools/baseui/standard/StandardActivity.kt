/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.standard

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import by.esas.tools.baseui.Config
import by.esas.tools.baseui.R
import by.esas.tools.baseui.interfaces.navigating.IHandlePopBackArguments
import by.esas.tools.baseui.mvvm.DataBindingActivity
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.handler.ShowErrorType

abstract class StandardActivity<VM : StandardViewModel<M>, B : ViewDataBinding, M : BaseErrorModel>
    : DataBindingActivity<VM, B, M>(), IHandlePopBackArguments {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    //region Observer setups

    override fun setupObservers() {
        super.setupObservers()
        //Settings for IShowingVM
        setupDialogsObservers()
    }

    open fun setupDialogsObservers() {
        viewModel.showDialog.observe(this, Observer { dialog ->
            if (dialog != null) {
                onShowDialog(dialog, dialog.TAG)
                viewModel.showDialog.postValue(null)
            }
        })
        viewModel.showBottomDialog.observe(this, Observer { dialog ->
            if (dialog != null) {
                onShowDialog(dialog, dialog.TAG)
                viewModel.showBottomDialog.postValue(null)
            }
        })
    }

    override fun provideRequestKeys(): List<String> {
        return listOf(Config.ERROR_MESSAGE_DIALOG)
    }

    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return if (requestKey == Config.ERROR_MESSAGE_DIALOG) {
            FragmentResultListener { key, result ->
                logger.logInfo("onFragmentResult $requestKey, $result")
                val actionName = result.getString(BaseDialogFragment.DIALOG_ACTION_NAME)
                if (actionName.isNullOrBlank()) {
                    enableIfNeeded(result)
                } else {
                    handleAction(Action(actionName, result))
                }
            }
        } else {
            null
        }
    }
    //endregion Observer setups

    override fun handleAction(action: Action): Boolean {
        when (action.name) {
            StandardViewModel.ACTION_CHANGE_LANGUAGE -> {
                val lang = action.parameters?.getString(StandardViewModel.PARAM_NEW_LANGUAGE)
                onChangeLanguage(lang, action.parameters)
            }
            else -> {
                return super.handleAction(action)
            }
        }
        return true
    }

    protected override fun showError(msg: String, showType: String, action: Action?) {
        logger.logOrder("showError msg = $msg showType = $showType action = $action")
        hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING.name -> enableControls()
            ShowErrorType.SHOW_ERROR_DIALOG.name -> {
                val dialog = MessageDialog(false).apply {
                    setRequestKey(Config.ERROR_MESSAGE_DIALOG)
                    setEnableControlsOnDismiss(true)
                    setTitle(R.string.base_ui_error_title)
                    setMessage(msg)
                    setPositiveButton(R.string.base_ui_common_ok_btn, action?.name)
                }
                onShowDialog(dialog, dialog.TAG)
            }
            ShowErrorType.SHOW_ERROR_MESSAGE.name -> {
                showMessage(msg)
                if (action != null)
                    handleAction(action)
                enableControls()
            }
        }
    }

    protected open fun onChangeLanguage(lang: String?, params: Bundle?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            this.setLanguage(lang)
        }
    }

    protected open fun enableIfNeeded(bundle: Bundle) {
        if (bundle.getBoolean(BaseDialogFragment.ENABLING_ON_DISMISS, false)) {
            viewModel.enableControls()
        }
    }
}