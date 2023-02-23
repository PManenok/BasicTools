/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.checker.checks

import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck

open class MinLengthCheck : BaseCheck {

    override var TAG: String = CustomCheck::class.java.simpleName
    private var min: Int = 0

    constructor(min: Int = 0, @StringRes errorRes: Int) : super(errorRes) {
        this.min = min
    }

    constructor(min: Int = 0, errorMessage: String = "Length can't be less then $min") : super(errorMessage) {
        this.min = min
    }

    constructor(min: Int = 0, exception: Exception) : super(exception) {
        this.min = min
    }

    constructor(min: Int = 0, status: Enum<*>) : super(status) {
        this.min = min
    }

    override fun check(value: Any?): Boolean {
        if (value == null) {
            throw NullPointerException()
        } else {
            val length = value.toString().length
            return length >= min
        }
    }
}