/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.simple

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import by.esas.tools.basedaggerui.mvvm.DataBindingActivity
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.logger.BaseErrorModel

abstract class SimpleActivity<VM : SimpleViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    DataBindingActivity<VM, B, E, M>() {

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
        //Settings for IChangeLangVM
        setupChangeLangObserver()
    }

    open fun setupDialogsObservers() {
        viewModel.showDialog.observe(this, Observer { dialog ->
            onShowDialog(dialog)
        })
        viewModel.showBottomDialog.observe(this, Observer { dialog ->
            onShowDialog(dialog)
        })
    }

    open fun setupChangeLangObserver() {
        viewModel.changeLang.observe(this, Observer { lang ->
            onChangeLanguage(lang)
        })
    }

    //endregion Observer setups


    //region show dialog functionality

    protected open fun onShowDialog(dialog: DialogFragment, tag: String) {
        logger.logInfo("try to showDialog $tag")
        val prevWithSameTag: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (prevWithSameTag != null && prevWithSameTag is BaseDialogFragment<*, *>
            && prevWithSameTag.getDialog() != null && prevWithSameTag.getDialog()?.isShowing == true
            && !prevWithSameTag.isRemoving
        ) {
            //there is currently showed dialog fragment with same TAG
        } else {
            //there is no currently showed dialog fragment with same TAG
            dialog.show(supportFragmentManager, tag)
        }
        if (dialog is BaseBottomDialogFragment<*, *>) {
            viewModel.showBottomDialog.postValue(null)
        } else {
            viewModel.showDialog.postValue(null)
        }
    }

    protected open fun onShowDialog(dialog: BaseDialogFragment<*, *>?) {
        logger.logInfo("try to showDialog ${dialog != null}")
        if (dialog != null) {
            onShowDialog(dialog, dialog.TAG)
        }
    }

    protected open fun onShowDialog(dialog: BaseBottomDialogFragment<*, *>?) {
        logger.logInfo("try to showDialog ${dialog != null}")
        if (dialog != null) {
            onShowDialog(dialog, dialog.TAG)
        }
    }

    //endregion show dialog functionality

    protected open fun onChangeLanguage(lang: String?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            this.setLanguage(lang)
            logger.logInfo("changeLang reset to null")
            viewModel.changeLang.postValue(null)
        }
    }
}