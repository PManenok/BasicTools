/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.standard

import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import by.esas.tools.baseui.test.interfaces.navigating.IHandlePopBackArguments
import by.esas.tools.baseui.test.interfaces.navigating.NavAction
import by.esas.tools.baseui.test.interfaces.navigating.PopBackAction
import by.esas.tools.baseui.test.mvvm.DataBindingFragment
import by.esas.tools.checker.Checking
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

abstract class StandardFragment<VM : StandardViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    DataBindingFragment<VM, B, E, M>() {
    protected var navController: NavController? = null
    abstract val fragmentDestinationId: Int

    abstract fun provideChecks():List<Checking>

    override fun handleAction(action: Action): Boolean {
        when (action.name) {
            NavAction.ACTION_NAVIGATION -> {
                onNavigate(action as NavAction)
            }
            PopBackAction.ACTION_POP_BACK -> {
                onPopBack(action as PopBackAction)
            }
            else -> {
                return super.handleAction(action)
            }
        }
        return true
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
            (activity as IHandlePopBackArguments).handleArguments(action.parameters)
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
}