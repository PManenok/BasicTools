/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.standard

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import by.esas.tools.baseui.Config.ERROR_MESSAGE_DIALOG
import by.esas.tools.baseui.R
import by.esas.tools.baseui.basic.BaseActivity
import by.esas.tools.baseui.interfaces.navigating.IHandlePopBackArguments
import by.esas.tools.baseui.interfaces.navigating.NavAction
import by.esas.tools.baseui.interfaces.navigating.PopBackAction
import by.esas.tools.baseui.mvvm.DataBindingFragment
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.dialog.Config.DIALOG_ACTION_NAME
import by.esas.tools.dialog.MessageDialog
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.handler.ShowErrorType

abstract class StandardFragment<VM : StandardViewModel<M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel>
    : DataBindingFragment<VM, B, M>() {

    protected var navController: NavController? = null
    abstract val fragmentDestinationId: Int

    //region Check fields

    abstract fun provideChecks(): List<Checking>

    abstract fun provideChecker(): Checker

    protected open fun provideCheckListener(): Checker.CheckListener {
        return viewModel.provideCheckListener()
    }

    //endregion Check fields

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    //region setup observers

    override fun setupObservers() {
        super.setupObservers()
        setupDialogsObservers()
    }

    open fun setupDialogsObservers() {
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { dialog ->
            logger.logInfo("try to showDialog ${dialog != null}")
            if (dialog != null) {
                showDialog(dialog, dialog.TAG)
                viewModel.showDialog.postValue(null)
            }
        })
        viewModel.showBottomDialog.observe(viewLifecycleOwner, Observer { dialog ->
            logger.logInfo("try to showBottomDialog ${dialog != null}")
            if (dialog != null) {
                showDialog(dialog, dialog.TAG)
                viewModel.showBottomDialog.postValue(null)
            }
        })
    }

    override fun provideRequestKeys(): List<String> {
        return listOf(ERROR_MESSAGE_DIALOG)
    }

    /**
     * This is default [ERROR_MESSAGE_DIALOG] fragment result listener, to change handling
     * of ERROR_MESSAGE_DIALOG override [provideFragmentResultListener] in your fragment instance
     * and add custom listener to this requestKey.
     * */
    override fun provideFragmentResultListener(requestKey: String): FragmentResultListener? {
        return if (requestKey == ERROR_MESSAGE_DIALOG) {
            FragmentResultListener { key, result ->
                val actionName = result.getString(DIALOG_ACTION_NAME)
                if (!actionName.isNullOrBlank()) {
                    handleAction(Action(actionName, result))
                } else {
                    enableControls(result)
                }
            }
        } else {
            null
        }
    }

    //endregion setup observers

    //region handle action (navigation, popback, checking fields and language change)

    override fun handleAction(action: Action): Boolean {
        when (action.name) {
            NavAction.ACTION_NAVIGATION -> {
                onNavigate(action as NavAction)
            }
            PopBackAction.ACTION_POP_BACK -> {
                onPopBack(action as PopBackAction)
            }
            StandardViewModel.ACTION_CHECK_FIELDS -> {
                onCheckFields(action.parameters)
            }
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

    protected override fun showError(msg: String, showType: String, action: Action?) {
        hideProgress()
        when (showType) {
            ShowErrorType.SHOW_NOTHING.name -> enableControls()
            ShowErrorType.SHOW_ERROR_DIALOG.name -> {
                val dialog = MessageDialog(false).apply {
                    setRequestKey(ERROR_MESSAGE_DIALOG)
                    setTitle(R.string.base_ui_error_title)
                    setMessage(msg)
                    setPositiveButton(R.string.base_ui_common_ok_btn, action?.name)
                    action?.parameters?.let { params -> setParams(params) }
                }
                showDialog(dialog, dialog.TAG)
            }
            ShowErrorType.SHOW_ERROR_MESSAGE.name -> {
                showMessage(msg)
                if (action != null)
                    handleAction(action)
                enableControls()
            }
        }
    }

    protected open fun onCheckFields(params: Bundle?) {
        provideChecker()
            .setListener(provideCheckListener())
            .setMode(true)
            .validate(provideChecks())
    }

    protected open fun onNavigate(action: NavAction) {
        logger.logInfo("try to navigate")
        try {
            if (navController?.currentDestination?.id == fragmentDestinationId) {
                logger.logInfo("navigate to destination")

                activity?.runOnUiThread {
                    navController?.navigate(action.direction)
                }
            }
        } catch (e: Throwable) {
            logger.logError(e)
        }
    }

    protected open fun onPopBack(action: PopBackAction) {
        logger.logInfo("try to popBack")
        //Check arguments from popBack action and send them to activity if it is IHandlePopBackArguments
        if (action.parameters != null && activity is IHandlePopBackArguments) {
            (activity as IHandlePopBackArguments).handlePopBackArguments(action.parameters)
        }
        //Check if popBack destination was set
        if (action.destination != 0) {
            logger.logInfo("navController popBackStack to ${action.destination} inclusive ${action.inclusive}")
            activity?.runOnUiThread { navController?.popBackStack(action.destination, action.inclusive) }
        } else {
            logger.logInfo("navController popBackStack")
            activity?.runOnUiThread { navController?.popBackStack() }
        }
        action.handled = true
    }

    protected open fun onChangeLanguage(lang: String?, params: Bundle?) {
        logger.logInfo("try to changeLang $lang")
        if (!lang.isNullOrBlank()) {
            logger.logInfo("changeLang to $lang")
            activity?.let {
                if (it is BaseActivity<*>)
                    it.setLanguage(lang)
            }
        }
    }

    //endregion handle action (navigation, popback, checking fields and language change)
}