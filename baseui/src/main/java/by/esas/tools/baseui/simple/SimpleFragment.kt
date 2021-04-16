/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.simple

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.baseui.basic.BaseActivity
import by.esas.tools.baseui.mvvm.DataBindingFragment
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment

abstract class SimpleFragment<VM : SimpleViewModel<E, *>, B : ViewDataBinding, E : Enum<E>> : DataBindingFragment<VM, B, E>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Settings for IObserverVM
        resetObserverManager()
        //Settings for IShowingVM
        observeShowDialog()
        observeShowBottomDialog()
        //Settings for IChangeLangVM
        observeChangeLanguage()
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    protected open fun resetObserverManager() {
        if (!viewModel.observerManager.checkLifecycleOwner(requireActivity())) {
            viewModel.observerManager.clearLifecycleOwner()
            viewModel.observerManager.setLifecycleOwner(provideLifecycleOwner())
        }
    }

    protected open fun observeShowDialog() {
        if (!viewModel.showDialog.hasObservers()) {
            viewModel.addToObservable(viewModel.showDialog, Observer { dialog ->
                onShowDialog(dialog)
            })
        }
    }

    protected open fun observeShowBottomDialog() {
        if (!viewModel.showBottomDialog.hasObservers()) {
            viewModel.addToObservable(viewModel.showBottomDialog, Observer { dialog ->
                onShowBottomDialog(dialog)
            })
        }
    }

    protected open fun observeChangeLanguage() {
        if (!viewModel.changeLang.hasObservers()) {
            viewModel.addToObservable(viewModel.changeLang, Observer { lang ->
                onChangeLanguage(lang)
            })
        }
    }

    protected open fun onShowDialog(dialog: BaseDialogFragment<*, *>?) {
        logger.logInfo("try to showDialog ${dialog != null}")
        dialog?.let {
            it.show(requireFragmentManager(), dialog.TAG)
            viewModel.showDialog.postValue(null)
        }
    }

    protected open fun onShowBottomDialog(dialog: BaseBottomDialogFragment<*, *>?) {
        logger.logInfo("try to showBottomDialog ${dialog != null}")
        dialog?.let {
            it.show(requireFragmentManager(), dialog.TAG)
            viewModel.showBottomDialog.postValue(null)
        }
    }

    protected open fun onChangeLanguage(lang: String?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            activity?.let {
                if (it is BaseActivity<*>)
                    it.setLanguage(lang)
            }
            logger.logInfo("changeLang reset to null")
            viewModel.changeLang.postValue(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Settings for IObserverVM
        viewModel.observerManager.clearObservables()
        //Settings for IShowingVM
        viewModel.showDialog.removeObservers(viewLifecycleOwner)
        viewModel.showDialog.postValue(null)
        viewModel.showBottomDialog.removeObservers(viewLifecycleOwner)
        viewModel.showBottomDialog.postValue(null)
        //Settings for IChangeLangVM
        viewModel.changeLang.removeObservers(viewLifecycleOwner)
        viewModel.changeLang.postValue(null)
    }
}