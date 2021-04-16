/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.checker.checks

import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck

open class CustomCheck : BaseCheck {
    override var TAG: String = CustomCheck::class.java.simpleName
    private var func: (String) -> Boolean

    constructor(func: (String) -> Boolean, @StringRes errorRes: Int, vararg params: Any) : super(errorRes, params) {
        this.func = func
    }

    constructor(func: (String) -> Boolean, errorMessage: String="Does not match custom rule") : super(errorMessage) {
        this.func = func
    }

    constructor(func: (String) -> Boolean, exception: Exception) : super(exception) {
        this.func = func
    }

    constructor(func: (String) -> Boolean, status: Enum<*>) : super(status) {
        this.func = func
    }


    override fun check(value: Any?): Boolean {
        if (value == null) {
            throw NullPointerException()
        } else {
            return func(value.toString())
        }
    }
}