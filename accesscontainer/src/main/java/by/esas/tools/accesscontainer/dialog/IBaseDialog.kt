/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentResultListener

interface IBaseDialog {
    fun dismiss()
    fun show(context: FragmentActivity?, tag: String)
    fun provideResultListener(tag: String): FragmentResultListener
}