/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.mvvm

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.basic.BaseActivity
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

/**
 * Base class for activity that inherits from [BaseActivity] and implements data binding.
 * This class adds observer to view model's activity variable [setupActionObserver]
 */
abstract class DataBindingActivity<TViewModel : BaseViewModel<E, M>, TBinding : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>>
    : BaseActivity<E, M>() {

    protected lateinit var binding: TBinding
    protected lateinit var viewModel: TViewModel

    abstract fun provideViewModel(): TViewModel

    abstract fun provideLayoutId(): Int

    abstract fun provideVariableInd(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()

        binding = DataBindingUtil.setContentView(this, provideLayoutId())
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = this

        setupObservers()
    }

    open fun setupObservers() {
        setupActionObserver()
    }

    open fun setupActionObserver() {
        viewModel.action.observe(this, Observer { action ->
            if (action != null)
                handleAction(action)
        })
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
}
