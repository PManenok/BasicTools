/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.R
import by.esas.tools.baseui.basic.BaseFragment
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.handler.ShowErrorType

abstract class DataBindingFragment<VM : BaseViewModel<M>, B : ViewDataBinding, M : BaseErrorModel>
    : BaseFragment<M>() {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM

    abstract fun provideVariableInd(): Int

    //region fragment lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
        viewModel.logger.setTag(viewModel.TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.setTag(TAG)
        logger.logOrder("onCreateView")

        binding = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    //endregion fragment lifecycle methods

    /**
     * Method handles action. If action was not handled by fragment's method - action is sent to viewModel.
     * @return Boolean (false in case if action was not handled by this method and true if it was handled)
     */
    override fun handleAction(action: Action): Boolean {
        logger.logInfo("override handleAction $action")
        //try to handle action by parent's handler
        if (!super.handleAction(action)) {
            logger.logInfo("viewModel handleAction $action")
            // try to handle action in viewModel if parent's handler didn't handle
            return viewModel.handleAction(action)
        }
        return false
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

    //region Observer setups

    protected open fun setupObservers() {
        setupActionObserver()
        setupControlsObservers()
    }

    protected open fun setupActionObserver() {
        viewModel.action.observe(viewLifecycleOwner, Observer { action ->
            if (action != null && !action.handled) {
                logger.logOrder("action Observer $action")
                handleAction(action)
            }
        })
    }

    protected open fun setupControlsObservers() {
        viewModel.controlsEnabled.observe(viewLifecycleOwner, Observer { isEnabled ->
            logger.logOrder("controlsEnabled Observer $isEnabled")
            if (isEnabled) switchControlsOn()
            else switchControlsOff()
        })
    }

    //endregion Observer setups

    protected fun isBindingInitialized() = ::binding.isInitialized
}