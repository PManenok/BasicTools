/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.interfaces.navigating

import android.os.Bundle
import by.esas.tools.logger.Action
import by.esas.tools.logger.BaseErrorModel

open class PopBackAction(
    val destination: Int = 0,
    val inclusive: Boolean = false,
    parameters: Bundle? = null
) : Action(ACTION_POP_BACK, parameters) {

    companion object {
        const val ACTION_POP_BACK: String = "ACTION_POP_BACK"
    }
}