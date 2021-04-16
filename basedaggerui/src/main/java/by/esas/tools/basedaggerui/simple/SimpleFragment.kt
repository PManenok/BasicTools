/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.simple

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.basic.BaseActivity
import by.esas.tools.basedaggerui.mvvm.DataBindingFragment
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.logger.BaseErrorModel

abstract class SimpleFragment<VM : SimpleViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    DataBindingFragment<VM, B, E, M>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Settings for IShowingVM
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { dialog ->
            onShowDialog(dialog)
        })
        viewModel.showBottomDialog.observe(viewLifecycleOwner, Observer { dialog ->
            onShowBottomDialog(dialog)
        })
        //Settings for IChangeLangVM
        viewModel.changeLang.observe(viewLifecycleOwner, Observer { lang ->
            onChangeLanguage(lang)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    protected open fun onShowDialog(dialog: BaseDialogFragment<*, *>?) {
        logger.logInfo("try to showDialog ${dialog != null}")
        dialog?.let {
            it.show(parentFragmentManager, dialog.TAG)
            viewModel.showDialog.postValue(null)
        }
    }

    protected open fun onShowBottomDialog(dialog: BaseBottomDialogFragment<*, *>?) {
        logger.logInfo("try to showBottomDialog ${dialog != null}")
        dialog?.let {
            it.show(parentFragmentManager, dialog.TAG)
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