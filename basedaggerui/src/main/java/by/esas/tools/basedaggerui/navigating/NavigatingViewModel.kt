/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.navigating

import android.os.Bundle
import androidx.navigation.NavDirections
import by.esas.tools.basedaggerui.interfaces.INavigateVM
import by.esas.tools.basedaggerui.simple.SimpleViewModel
import by.esas.tools.logger.BaseErrorModel

abstract class NavigatingViewModel<E : Enum<E>, M : BaseErrorModel<E>> : SimpleViewModel<E, M>(), INavigateVM {
    override fun navigate(direction: NavDirections) {
        requestAction.postValue(NavAction<E, M>(direction = direction, popBack = null, parameters = null))
    }

    override fun popBack() {
        requestAction.postValue(NavAction<E, M>(direction = null, popBack = PopBackEntity(0, false), parameters = null))
    }

    override fun popBack(entity: PopBackEntity, parameters: Bundle?) {
        requestAction.postValue(NavAction<E, M>(direction = null, popBack = entity, parameters = parameters))
    }
}