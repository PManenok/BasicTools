/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.mvvm

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.R
import by.esas.tools.baseui.basic.BaseActivity
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.handler.ShowErrorType

/**
 * Base class for activity that inherits from [BaseActivity] and implements data binding.
 * This class adds observer to view model's activity variable [setupActionObserver]
 */
abstract class DataBindingActivity<TViewModel : BaseViewModel<M>, TBinding : ViewDataBinding, M : BaseErrorModel>
    : BaseActivity<M>() {

    protected lateinit var binding: TBinding
    protected lateinit var viewModel: TViewModel

    abstract fun provideViewModel(): TViewModel

    abstract fun provideVariableInd(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
        setupObservers()
    }

    override fun setupRootView() {
        viewModel = provideViewModel()

        binding = DataBindingUtil.setContentView(this, provideLayoutId())
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = this
    }

    /**
     * Method handles action. If action was not handled by activity's method - action is sent to viewModel.
     * @return Boolean (false in case if action was not handled by this method and true if it was handled)
     */
    override fun handleAction(action: Action): Boolean {
        //try to handle action by parent's handler
        if (!super.handleAction(action)) {
            logger.logInfo("viewModel handleAction $action")
            // try to handle action in viewModel if parent's handler didn't handle
            return viewModel.handleAction(action)
        }
        return true
    }

    override fun showError(msg: String, showType: String, action: Action?) {
        viewModel.hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING.name -> viewModel.enableControls()
            ShowErrorType.SHOW_ERROR_DIALOG.name -> showErrorDialog(msg, action)
            ShowErrorType.SHOW_ERROR_MESSAGE.name -> showErrorMessage(msg, action)
        }
    }

    override fun showErrorDialog(msg: String, action: Action?) {
        provideMaterialAlertDialogBuilder().setTitle(R.string.base_ui_error_title)
            .setMessage(msg)
            .setPositiveButton(R.string.base_ui_common_ok_btn) { dialogInterface, _ ->
                dialogInterface?.dismiss()
                if (action != null)
                    handleAction(action)
                viewModel.enableControls()
            }.create().show()
    }

    override fun showErrorMessage(msg: String, action: Action?) {
        showMessage(msg)
        if (action != null)
            handleAction(action)
        viewModel.enableControls()
    }

    //region setup observers

    protected open fun setupObservers() {
        setupActionObserver()
        setupControlsObservers()
    }

    protected open fun setupActionObserver() {
        viewModel.action.observe(this, Observer { action ->
            if (action != null)
                handleAction(action)
        })
    }

    protected open fun setupControlsObservers() {
        viewModel.controlsEnabled.observe(this, Observer { isEnabled ->
            logger.logOrder("controlsEnabled Observer $isEnabled")
            if (isEnabled) switchControlsOn()
            else switchControlsOff()
        })
    }

    //endregion setup observers
}
