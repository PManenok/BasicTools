/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

import android.os.Bundle

/**
 * Should be used for every action that requires some actions from different parts of application
 * For example between view model and fragment, or fragment and activity.
 * Some basic actions are listed here in companion object
 */
open class Action(
    val name: String,
    val parameters: Bundle? = null,
    var handled: Boolean = false
) {

    companion object {

        const val ACTION_NOT_SET: String = "ACTION_NOT_SET"
        const val ACTION_FINISH: String = "ACTION_FINISH"
        const val ACTION_HIDE_KEYBOARD: String = "ACTION_HIDE_KEYBOARD"
        const val ACTION_CHECK_PERMISSIONS: String = "ACTION_CHECK_PERMISSIONS"
        const val ACTION_CHECK_AND_REQUEST_PERMISSIONS: String =
            "ACTION_CHECK_AND_REQUEST_PERMISSIONS"
    }

    override fun toString(): String {
        return "Action(name='$name', handled=$handled, params=${parameters.toString()})"
    }
}