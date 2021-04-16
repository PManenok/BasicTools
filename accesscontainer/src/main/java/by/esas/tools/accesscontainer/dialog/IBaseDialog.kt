/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager

interface IBaseDialog {
    fun dismiss()
    fun show(manager: FragmentManager?, context: Context?, tag: String)
}