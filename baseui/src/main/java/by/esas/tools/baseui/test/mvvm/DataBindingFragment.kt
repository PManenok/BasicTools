/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import by.esas.tools.baseui.test.basic.BaseFragment
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

abstract class DataBindingFragment<VM : BaseViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    BaseFragment<E, M>() {

    companion object {
        val TAG: String = DataBindingFragment::class.java.simpleName
    }

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM


    abstract fun provideLifecycleOwner(): LifecycleOwner

    abstract fun provideVariableInd(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
        viewModel.logger.setTag(viewModel.TAG)
    }

    //region fragment lifecycle methods

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.setTag(TAG)
        logger.logOrder("onCreateView")

        binding = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logOrder("onDestroyView")
    }

    //endregion fragment lifecycle methods

    //region Observer setups

    open fun setupObservers() {
        setupActionObserver()
    }

    open fun setupActionObserver() {
        viewModel.action.observe(viewLifecycleOwner, Observer { action ->
            if (action != null && !action.handled) {
                logger.logOrder("requestAction Observer $action")
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
        //try to handle action by parent's handler
        if (!super.handleAction(action)) {
            logger.logInfo("viewModel handleAction $action")
            // try to handle action in viewModel if parent's handler didn't handle
            return viewModel.handleAction(action)
        }
        return true
    }
}