/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.standard

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.interfaces.navigating.IHandlePopBackArguments
import by.esas.tools.baseui.mvvm.DataBindingActivity
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

abstract class StandardActivity<VM : StandardViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>>
    : DataBindingActivity<VM, B, E, M>(), IHandlePopBackArguments {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    //region Observer setups

    override fun setupObservers() {
        super.setupObservers()
        //Settings for IShowingVM
        setupDialogsObservers()
    }

    open fun setupDialogsObservers() {
        viewModel.showDialog.observe(this, Observer { dialog ->
            onShowDialog(dialog)
            viewModel.showDialog.postValue(null)
        })
        viewModel.showBottomDialog.observe(this, Observer { dialog ->
            onShowDialog(dialog)
            viewModel.showBottomDialog.postValue(null)
        })
    }

    //endregion Observer setups

    override fun handleAction(action: Action): Boolean {
        when (action.name) {
            StandardViewModel.ACTION_CHANGE_LANGUAGE -> {
                val lang = action.parameters?.getString(StandardViewModel.PARAM_NEW_LANGUAGE)
                onChangeLanguage(lang, action.parameters)
            }
            else -> {
                return super.handleAction(action)
            }
        }
        return true
    }

    protected open fun onChangeLanguage(lang: String?, params: Bundle?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            this.setLanguage(lang)
        }
    }
}