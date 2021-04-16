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
import androidx.lifecycle.LifecycleOwner
import by.esas.tools.baseui.R
import by.esas.tools.baseui.basic.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class DataBindingFragment<VM : BaseViewModel<E,*>, B : ViewDataBinding, E : Enum<E>> :
    BaseFragment<E>() {

    protected lateinit var binding: B

    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM

    abstract fun provideLayoutId(): Int

    abstract fun provideSwitchableViewList(): List<View?>

    abstract fun provideLifecycleOwner(): LifecycleOwner

    abstract fun provideVariableInd(): Int

    open fun provideMaterialAlertDialogBuilder(): MaterialAlertDialogBuilder{
        return MaterialAlertDialogBuilder(
            context,
            R.style.AppTheme_CustomMaterialDialog
        ).setCancelable(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger = provideLogger()
        logger.setTag(TAG)
        logger.logInfo("onCreateView")

        viewModel = provideViewModel()
        viewModel.initLogger()

        binding = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()

        viewModel.alertDialogBuilder = provideMaterialAlertDialogBuilder()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.logInfo("onViewCreated")
        if (viewModel.switchableViewsList.isNotEmpty()) viewModel.switchableViewsList.clear()
        viewModel.switchableViewsList.addAll(provideSwitchableViewList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyView")
        viewModel.dismissDialogs()
        viewModel.alertDialogBuilder = null
        viewModel.switchableViewsList.clear()
    }
}