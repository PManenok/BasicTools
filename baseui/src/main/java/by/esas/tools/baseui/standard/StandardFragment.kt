/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.standard

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import by.esas.tools.baseui.basic.BaseActivity
import by.esas.tools.baseui.interfaces.navigating.IHandlePopBackArguments
import by.esas.tools.baseui.interfaces.navigating.NavAction
import by.esas.tools.baseui.interfaces.navigating.PopBackAction
import by.esas.tools.baseui.mvvm.DataBindingFragment
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

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
            showDialog(dialog)
            viewModel.showDialog.postValue(null)
        })
        viewModel.showBottomDialog.observe(viewLifecycleOwner, Observer { dialog ->
            showDialog(dialog)
            viewModel.showBottomDialog.postValue(null)
        })
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