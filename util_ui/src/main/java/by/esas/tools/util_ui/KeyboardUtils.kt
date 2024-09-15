/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util_ui

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun showKeyboard(activity: Activity?, requestFocus: Boolean = true) {
    activity?.apply {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
        if (requestFocus) {
            val contentView = window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
            contentView.requestFocus()
        }
    }
}

fun hideKeyboard(activity: Activity?) {
    if (activity == null) return
    val view = activity.currentFocus
    if (view != null) {
        hideKeyboard(activity, view.windowToken)
    }
}

fun focusAndShowKeyboard(activity: Activity?, view: View) {
    if (activity == null) return
    if (view is EditText) {
        showKeyboard(activity, false)
    }
    if (view.isFocusable) {
        view.requestFocus()
    }
}

fun defocusAndHideKeyboard(activity: Activity?) {
    if (activity == null) return
    val view = activity.currentFocus
    if (view != null) {
        view.clearFocus()
        hideKeyboard(activity, view.windowToken)
    }
}

private fun hideKeyboard(activity: Activity?, windowToken: IBinder?) {
    if (activity == null) return
    val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}
