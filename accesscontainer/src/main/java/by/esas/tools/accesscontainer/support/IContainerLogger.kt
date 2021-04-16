/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.support

import android.widget.Toast

interface IContainerLogger<T> {
    fun setTag(tag: String)
    fun log(msg: String)
    fun log(res: Int)
    fun log(tag: String, msg: String)
    fun showMessage(msg: String, length: Int = Toast.LENGTH_SHORT)
    fun showMessage(msgRes: Int, length: Int = Toast.LENGTH_SHORT)
    fun logError(err: Exception)
    fun logError(msg: String)
    fun logError(msgRes: Int)
    fun logError(err: T)
}