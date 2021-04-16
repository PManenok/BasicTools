/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.dialog

interface BiometricUserInfo {
        fun getCurrentUser(): String
        fun setCurrentUserIv(iv: ByteArray)
        fun getCurrentUserIv(): ByteArray
    }