/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.biometric_decryption

class DecryptionException(msg: String = "Decryption failed") : Exception(msg) {
    constructor(enum: DecryptionEnum) : this(enum.name)
}