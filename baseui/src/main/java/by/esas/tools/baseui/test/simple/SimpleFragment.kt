/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.simple

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import by.esas.tools.baseui.test.basic.BaseActivity
import by.esas.tools.baseui.test.mvvm.DataBindingFragment
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.logger.BaseErrorModel

abstract class SimpleFragment<VM : SimpleViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    DataBindingFragment<VM, B, E, M>() {

    //region fragment lifecycle methods

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    //endregion fragment lifecycle methods

    //region Observer setups

    override fun setupObservers() {
        super.setupObservers()
        //Settings for IShowingVM
        setupDialogsObservers()
        //Settings for IChangeLangVM
        setupChangeLangObserver()
    }

    open fun setupDialogsObservers() {
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { dialog ->
            onShowDialog(dialog)
        })
        viewModel.showBottomDialog.observe(viewLifecycleOwner, Observer { dialog ->
            onShowDialog(dialog)
        })
    }

    open fun setupChangeLangObserver() {
        viewModel.changeLang.observe(viewLifecycleOwner, Observer { lang ->
            onChangeLanguage(lang)
        })
    }

    //endregion Observer setups

    override fun onShowDialog(dialog: DialogFragment, tag: String) {
        super.onShowDialog(dialog, tag)
        if (dialog is BaseBottomDialogFragment<*, *>) {
            viewModel.showBottomDialog.postValue(null)
        } else {
            viewModel.showDialog.postValue(null)
        }
    }

    protected open fun onChangeLanguage(lang: String?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            activity?.let {
                if (it is BaseActivity<*, *>)
                    it.setLanguage(lang)
            }
            logger.logInfo("changeLang reset to null")
            viewModel.changeLang.postValue(null)
        }
    }
}