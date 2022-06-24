/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.standard

import android.os.Bundle
import androidx.navigation.NavDirections
import by.esas.tools.baseui.test.interfaces.IExecutingVM
import by.esas.tools.baseui.test.interfaces.navigating.INavigateVM
import by.esas.tools.baseui.test.interfaces.navigating.NavAction
import by.esas.tools.baseui.test.interfaces.navigating.PopBackAction
import by.esas.tools.baseui.test.mvvm.BaseViewModel
import by.esas.tools.domain.usecase.UseCase
import by.esas.tools.logger.BaseErrorModel

abstract class StandardViewModel<E : Enum<E>, M : BaseErrorModel<E>>
    : BaseViewModel<E, M>(), IExecutingVM, INavigateVM {

    companion object {
        const val ACTION_CHECK_FIELDS: String = "ACTION_CHECK_FIELDS"
    }

    override var useCases: MutableList<UseCase<*, *, *>> = mutableListOf<UseCase<*, *, *>>()

    override fun onCleared() {
        super.onCleared()
        unsubscribeUseCases()
    }

    override fun navigate(direction: NavDirections) {
        requestAction.postValue(NavAction(direction = direction))
    }

    override fun navigate(direction: NavDirections, parameters: Bundle?) {
        requestAction.postValue(NavAction(direction = direction, parameters = parameters))
    }

    override fun popBack(destination: Int, inclusive: Boolean, parameters: Bundle?) {
        requestAction.postValue(PopBackAction(destination, inclusive, parameters))
    }
}