/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding

/**
 * Bottom dialog fragment that uses data binding, with custom state callback, disabling and enabling functions, showing and hiding progress
 * */
abstract class BindingBottomDialogFragment<B : ViewDataBinding>() :
    BaseBottomDialogFragment() {
    /**
     * This property holds [ViewDataBinding] instance for this dialog fragment.
     * It is set in [onCreateView] methods.
     * */
    lateinit var binding: B

    /**
     * This [ObservableBoolean] replaces [provideProgressBar] method usage
     * Now showProgress property set progress view visibility in layout
     * */
    val showProgress: ObservableBoolean = ObservableBoolean(false)

    /**
     * Provides variable id for data binding
     * @see onCreateView
     * */
    abstract fun provideVariableId(): Int

    /**
     * This method is obsolete for all inherited classes
     * @return null
     * */
    override fun provideProgressBar(): View? = null

    /**
     * Override parents onCreateView method
     * creates binding instance and sets variable provided by [provideVariableId] method
     * with [ViewDataBinding.setVariable] method
     * @return root view of binding instance
     * */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.logInfo("onCreateView")
        binding = DataBindingUtil.inflate<B>(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableId(), this)
        return binding.root
    }

    /**
     * Method makes progress visible to user
     * @see disableControls
     * */
    override fun showProgress() {
        showProgress.set(true)
    }

    /**
     * Method makes progress invisible to user
     * @see enableControls
     * */
    override fun hideProgress() {
        showProgress.set(false)
    }
}