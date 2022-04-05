/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.navigating

import android.os.Bundle
import androidx.navigation.NavDirections
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

open class NavAction<E : Enum<E>, M : BaseErrorModel<E>>(
    val direction: NavDirections? = null,
    val popBack: PopBackEntity? = null,
    parameters: Bundle? = null
) : Action(ACTION_NAVIGATION, parameters) {

    companion object {
        const val ACTION_NAVIGATION: String = "ACTION_NAVIGATION"
    }
}