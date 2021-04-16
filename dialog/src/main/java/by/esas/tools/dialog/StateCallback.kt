/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.dialog

interface StateCallback<T : Exception> {
    fun onError(e: T)
    fun onDismiss(afterOk: Boolean)
}