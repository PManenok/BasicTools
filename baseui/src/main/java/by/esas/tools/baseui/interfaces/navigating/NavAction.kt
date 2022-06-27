/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.interfaces.navigating

import android.os.Bundle
import androidx.navigation.NavDirections
import by.esas.tools.logger.Action

open class NavAction(
    val direction: NavDirections,
    parameters: Bundle? = null
) : Action(ACTION_NAVIGATION, parameters) {

    companion object {
        const val ACTION_NAVIGATION: String = "ACTION_NAVIGATION"
    }
}