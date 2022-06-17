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
import by.esas.tools.basedaggerui.basic.BaseActivity
import by.esas.tools.logger.BaseErrorModel


abstract class DataBindingActivity<TViewModel : BaseViewModel<E, M>, TBinding : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    BaseActivity<E, M>() {

    protected lateinit var binding: TBinding
    protected lateinit var viewModel: TViewModel

    abstract fun provideViewModel(): TViewModel

    abstract fun provideLayoutId(): Int

    abstract fun provideVariableInd(): Int

    abstract fun provideLifecycleOwner(): LifecycleOwner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()

        binding = DataBindingUtil.setContentView(this, provideLayoutId())
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()

        setupObservers()
    }

    open fun setupObservers() {
        setupErrorObserver()
        setupRequestActionObserver()
    }

    open fun setupErrorObserver() {
        viewModel.errorAction.observe(this, Observer { data ->
            handleError(data)
        })
    }

    open fun setupRequestActionObserver() {
        viewModel.requestAction.observe(this, Observer { action ->
            if (action != null)
                handleAction(action)
        })
    }
}
