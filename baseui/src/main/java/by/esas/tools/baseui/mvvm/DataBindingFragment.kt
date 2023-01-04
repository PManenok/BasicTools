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
import by.esas.tools.baseui.basic.BaseFragment
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

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

    //region Observer setups

    open fun setupObservers() {
        setupActionObserver()
    }

    open fun setupActionObserver() {
        viewModel.action.observe(viewLifecycleOwner, Observer { action ->
            if (action != null && !action.handled) {
                logger.logOrder("action Observer $action")
                handleAction(action)
            }
        })
    }

    //endregion Observer setups

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

    protected override fun enableControls(parameters: Bundle?) {
        super.enableControls(parameters)
        viewModel.enableControls(false)
    }

    protected override fun disableControls(parameters: Bundle?) {
        viewModel.disableControls(false)
        super.disableControls(parameters)
    }

    protected fun isBindingInitialized() = ::binding.isInitialized
}