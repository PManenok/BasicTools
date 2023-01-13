/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.standard

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import by.esas.tools.baseui.Config.ERROR_MESSAGE_DIALOG
import by.esas.tools.baseui.R
import by.esas.tools.baseui.interfaces.navigating.IHandlePopBackArguments
import by.esas.tools.baseui.mvvm.DataBindingActivity
import by.esas.tools.dialog.Config.DIALOG_ACTION_NAME
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.ILogger
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
        return listOf(ERROR_MESSAGE_DIALOG)
    }

    /**
     * This is default [ERROR_MESSAGE_DIALOG] fragment result listener, to change handling
     * of ERROR_MESSAGE_DIALOG override [provideFragmentResultListener] in your activity instance
     * and add custom listener to this requestKey.
     * */
    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return if (requestKey == ERROR_MESSAGE_DIALOG) {
            FragmentResultListener { key, result ->
                val actionName = result.getString(DIALOG_ACTION_NAME)
                if (!actionName.isNullOrBlank()) {
                    handleAction(Action(actionName, result))
                } else {
                    enableControls(result)
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
            StandardViewModel.ACTION_CHANGE_NIGHT_MODE -> {
                val value = action.parameters?.getString(StandardViewModel.PARAM_NEW_NIGHT_MODE)
                onChangeNightMode(value, action.parameters)
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
                    setRequestKey(ERROR_MESSAGE_DIALOG)
                    setTitle(R.string.base_ui_error_title)
                    setMessage(msg)
                    setPositiveButton(R.string.base_ui_common_ok_btn, action?.name)
                    action?.parameters?.let { params -> setParams(params) }
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
        logger.logOrder("onChangeLanguage $lang $params")
        if (!lang.isNullOrBlank()) {
            this.changeLanguage(lang)
        }
    }

    protected open fun onChangeNightMode(mode: String?, params: Bundle?) {
        logger.logOrder("onChangeNightMode $mode $params")
        if (mode != null) {
            this.changeNightMode(mode)
        }
    }
}