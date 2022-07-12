/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.standard

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import by.esas.tools.baseui.interfaces.IExecutingVM
import by.esas.tools.baseui.interfaces.IShowingVM
import by.esas.tools.baseui.interfaces.navigating.INavigateVM
import by.esas.tools.baseui.interfaces.navigating.NavAction
import by.esas.tools.baseui.interfaces.navigating.PopBackAction
import by.esas.tools.baseui.mvvm.BaseViewModel
import by.esas.tools.checker.Checker
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.domain.usecase.UseCase
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

abstract class StandardViewModel<M : BaseErrorModel>
    : BaseViewModel<M>(), INavigateVM, IExecutingVM, IShowingVM {

    companion object {
        const val ACTION_CHECK_FIELDS: String = "ACTION_CHECK_FIELDS"
        const val ACTION_CHANGE_LANGUAGE: String = "ACTION_CHANGE_LANGUAGE"
        const val PARAM_NEW_LANGUAGE: String = "PARAM_NEW_LANGUAGE"
    }

    override var useCases: MutableList<UseCase<*, *>> = mutableListOf<UseCase<*, *>>()

    override val showDialog: MutableLiveData<BaseDialogFragment?> = MutableLiveData<BaseDialogFragment?>(null)
    override val showBottomDialog: MutableLiveData<BaseBottomDialogFragment<*>?> =
        MutableLiveData<BaseBottomDialogFragment<*>?>(null)

    abstract fun provideCheckListener(): Checker.CheckListener

    override fun onCleared() {
        super.onCleared()
        unsubscribeUseCases()
    }

    open fun requestLanguageChange(newLanguage: String) {
        val params = Bundle()
        params.putString(PARAM_NEW_LANGUAGE, newLanguage)
        val action = Action(ACTION_CHANGE_LANGUAGE, params)
        requestAction(action)
    }

    open fun requestFieldsCheck() {
        requestAction(Action(ACTION_CHECK_FIELDS))
    }

    //region navigation

    override fun navigate(direction: NavDirections) {
        action.postValue(NavAction(direction = direction))
    }

    override fun navigate(direction: NavDirections, parameters: Bundle?) {
        action.postValue(NavAction(direction = direction, parameters = parameters))
    }

    override fun popBack(destination: Int, inclusive: Boolean, parameters: Bundle?) {
        action.postValue(PopBackAction(destination, inclusive, parameters))
    }

    //endregion navigation
}