/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.checker.checks

import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck

open class NotEmptyCheck : BaseCheck {
    override var TAG: String = CustomCheck::class.java.simpleName

    constructor(@StringRes errorRes: Int) : super(errorRes)
    constructor(errorMessage: String = "Value must not be empty") : super(errorMessage)
    constructor(exception: Exception) : super(exception)
    constructor(status: Enum<*>) : super(status)

    override fun check(value: Any?): Boolean {
        if (value == null) {
            throw NullPointerException()
        } else {
            return value.toString().isNotEmpty()
        }
    }
}