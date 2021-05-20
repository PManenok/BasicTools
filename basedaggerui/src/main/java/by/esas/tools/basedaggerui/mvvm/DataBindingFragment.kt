/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.R
import by.esas.tools.basedaggerui.basic.BaseFragment
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.util.SwitchManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class DataBindingFragment<VM : BaseViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    BaseFragment<E, M>() {
    companion object{
        val TAG: String = DataBindingFragment::class.java.simpleName
    }

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    abstract fun provideViewModel(): VM

    abstract fun provideLayoutId(): Int

    abstract fun provideLifecycleOwner(): LifecycleOwner

    abstract fun provideVariableInd(): Int

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.errorData.observe(viewLifecycleOwner, Observer { data ->
            handleError(data)
        })
    }

    //region fragment lifecycle methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logger.setTag(TAG)
        logger.logInfo("onCreateView")

        viewModel = provideViewModel()

        binding = DataBindingUtil.inflate(inflater, provideLayoutId(), container, false)
        binding.setVariable(provideVariableInd(), viewModel)
        binding.lifecycleOwner = provideLifecycleOwner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.logInfo("onViewCreated")
        viewModel.switchableViewsList = { provideSwitchableViews() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logger.logInfo("onDestroyView")
    }
    //endregion
}