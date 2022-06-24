/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.basic.BaseFragment
import by.esas.tools.basedaggerui.inject.factory.InjectingViewModelFactory
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class DataBindingFragment<VM : BaseViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    BaseFragment<E, M>(), HasAndroidInjector {

    @Inject
    lateinit var viewModelFactory: InjectingViewModelFactory

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any?>

    override fun androidInjector(): AndroidInjector<Any?>? {
        return androidInjector
    }

    companion object {
        val TAG: String = DataBindingFragment::class.java.simpleName
    }

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM


    abstract fun provideLifecycleOwner(): LifecycleOwner

    abstract fun provideVariableInd(): Int

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

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
        setupErrorObserver()
        setupRequestActionObserver()
    }

    open fun setupErrorObserver() {
        viewModel.errorAction.observe(viewLifecycleOwner, Observer { action ->
            if (action != null) {
                logger.logOrder("errorAction Observer $action")
                handleError(action)
            }
        })
    }

    open fun setupRequestActionObserver() {
        viewModel.requestAction.observe(viewLifecycleOwner, Observer { action ->
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