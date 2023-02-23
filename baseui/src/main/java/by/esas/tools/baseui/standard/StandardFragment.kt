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
import androidx.navigation.fragment.NavHostFragment
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

abstract class StandardFragment<VM : StandardViewModel<M>, B : ViewDataBinding, M : BaseErrorModel>
    : DataBindingFragment<VM, B, M>() {

    protected var navController: NavController? = null
    abstract val fragmentDestinationId: Int

    //region Check fields

    abstract fun provideChecks(): List<Checking>

    abstract fun provideChecker(): Checker?

    protected open fun provideCheckListener(): Checker.CheckListener? {
        return viewModel.provideCheckListener()
    }

    //endregion Check fields

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavController()

        //Settings for IExecutingVM
        viewModel.addUseCases()
    }

    //region setups

    open fun setupNavController() {
        try {
            navController = NavHostFragment.findNavController(this)
        } catch (e: IllegalStateException) {
            logger.logInfo(
                "NavController was not found. Check if there is one along this Fragment's " +
                        "view hierarchy as specified by Navigation."
            )
            e.printStackTrace()
        }
    }

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
            FragmentResultListener { _, result ->
                val actionName = result.getString(DIALOG_ACTION_NAME)
                if (!actionName.isNullOrBlank()) {
                    handleAction(Action(actionName, result))
                } else {
                    if (!result.isEmpty)
                        handleAction(Action(Action.ACTION_NOT_SET, result))
                    viewModel.enableControls()
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
                action.handled = true
            }
            PopBackAction.ACTION_POP_BACK -> {
                onPopBack(action as PopBackAction)
                action.handled = true
            }
            StandardViewModel.ACTION_CHECK_FIELDS -> {
                onCheckFields(action.parameters)
                action.handled = true
            }
            StandardViewModel.ACTION_CHANGE_LANGUAGE -> {
                val lang = action.parameters?.getString(StandardViewModel.PARAM_NEW_LANGUAGE)
                onChangeLanguage(lang, action.parameters)
                action.handled = true
            }
            StandardViewModel.ACTION_CHANGE_NIGHT_MODE -> {
                val value = action.parameters?.getString(StandardViewModel.PARAM_NEW_NIGHT_MODE)
                onChangeNightMode(value, action.parameters)
                action.handled = true
            }
            else -> {
                return super.handleAction(action)
            }
        }
        return true
    }

    override fun showErrorDialog(msg: String, action: Action?) {
        val dialog = MessageDialog(false).apply {
            setRequestKey(ERROR_MESSAGE_DIALOG)
            setTitle(R.string.base_ui_error_title)
            setMessage(msg)
            setPositiveButton(R.string.base_ui_common_ok_btn, action?.name)
            action?.parameters?.let { params -> setParams(params) }
        }
        showDialog(dialog, dialog.TAG)
    }

    protected open fun onCheckFields(params: Bundle?) {
        provideChecker()
            ?.setListener(provideCheckListener())
            ?.setMode(true)
            ?.validate(provideChecks())
    }

    protected open fun onNavigate(action: NavAction) {
        logger.logInfo("try to navigate")
        if (navController?.currentDestination?.id == fragmentDestinationId) {
            if (action.direction != null) {
                logger.logInfo("navigate to destination")
                activity?.runOnUiThread { navController?.navigate(action.direction) }
            } else if (action.directionId != -1) {
                logger.logInfo("navigate to destination ${action.directionId}")
                activity?.runOnUiThread { navController?.navigate(action.directionId, action.parameters) }
            }
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
    }

    protected open fun onChangeLanguage(lang: String?, params: Bundle?) {
        logger.logOrder("onChangeLanguage $lang $params")
        if (!lang.isNullOrBlank()) {
            activity?.let {
                if (it is BaseActivity<*>)
                    it.changeLanguage(lang)
            }
        }
    }

    protected open fun onChangeNightMode(mode: String?, params: Bundle?) {
        logger.logOrder("onChangeNightMode $mode $params")
        if (mode != null) {
            activity?.let {
                if (it is BaseActivity<*>)
                    it.changeNightMode(mode)
            }
        }
    }

    //endregion handle action (navigation, popback, checking fields and language change)
}