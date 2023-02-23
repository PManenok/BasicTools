/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog.setters

import androidx.fragment.app.FragmentActivity
import by.esas.tools.accesscontainer.dialog.BiometricUserInfo
import by.esas.tools.accesscontainer.dialog.IBiometric
import javax.crypto.Cipher

interface SetBiometricDialog {

    fun onAuthenticationCallback(onError: (Boolean, String) -> Unit, onSuccess: (Cipher?) -> Unit)
    fun create(activity: FragmentActivity, userInfo: BiometricUserInfo)
    fun setInfo(title: String, negativeText: String)
    fun authenticate(isDecrypt: Boolean): IBiometric?
    fun clear()
}