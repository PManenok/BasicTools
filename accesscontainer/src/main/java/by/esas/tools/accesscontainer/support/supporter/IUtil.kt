/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.support.supporter

import android.content.Context
import by.esas.tools.logger.BaseErrorModel
import javax.crypto.SecretKey

interface IUtil {
    fun generatePin(pin: String, userId: String, androidId: String): SecretKey?
    fun checkBiometricSupport(context: Context?, logging: (String, String) -> Unit): Boolean
}