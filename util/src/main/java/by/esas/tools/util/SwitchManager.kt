/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util

import android.view.View
import android.widget.EditText

open class SwitchManager {
    /**
     * Method makes view editable
     * return true if method handled switch and false if not
     * */
    open fun enableView(view: View): Boolean {
        return when (view) {
            is ISwitchView -> {
                view.switchOn()
                true
            }
            is EditText -> {
                by.esas.tools.util.enableView(view)
                true
            }
            else -> {
                false
            }
        }
    }

    /**
     * Method makes view not editable
     * return true if method handled switch and false if not
     * */
    open fun disableView(view: View): Boolean {
        return when (view) {
            is ISwitchView -> {
                view.switchOff()
                true
            }
            is EditText -> {
                by.esas.tools.util.disableView(view)
                true
            }
            else -> {
                false
            }
        }
    }

    interface ISwitchView {
        fun switchOn()
        fun switchOff()
    }
}