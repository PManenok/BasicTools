/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.simple

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.mvvm.DataBindingActivity
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.logger.BaseErrorModel

abstract class SimpleActivity<VM : SimpleViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    DataBindingActivity<VM, B, E, M>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Settings for IShowingVM
        viewModel.showDialog.observe(this, Observer { dialog ->
            onShowDialog(dialog)
        })
        viewModel.showBottomDialog.observe(this, Observer { dialog ->
            onShowBottomDialog(dialog)
        })
        //Settings for IChangeLangVM
        viewModel.changeLang.observe(this, Observer { lang ->
            onChangeLanguage(lang)
        })
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    protected open fun onShowDialog(dialog: BaseDialogFragment<*, *>?) {
        logger.logInfo("try to showDialog ${dialog != null}")
        dialog?.let {
            it.show(supportFragmentManager, dialog.TAG)
            viewModel.showDialog.postValue(null)
        }
    }

    protected open fun onShowBottomDialog(dialog: BaseBottomDialogFragment<*, *>?) {
        logger.logInfo("try to showBottomDialog ${dialog != null}")
        dialog?.let {
            it.show(supportFragmentManager, dialog.TAG)
            viewModel.showBottomDialog.postValue(null)
        }
    }

    protected open fun onChangeLanguage(lang: String?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            this.setLanguage(lang)
            logger.logInfo("changeLang reset to null")
            viewModel.changeLang.postValue(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //Settings for IShowingVM
        viewModel.showDialog.postValue(null)
        viewModel.showDialog.removeObservers(this)
        viewModel.showBottomDialog.postValue(null)
        viewModel.showBottomDialog.removeObservers(this)
        //Settings for IChangeLangVM
        viewModel.changeLang.postValue(null)
        viewModel.changeLang.removeObservers(this)
    }
}