/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.checker.checks

import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck

open class LengthCheck : BaseCheck {

    override var TAG: String = CustomCheck::class.java.simpleName
    private var min: Int = 0
    private var max: Int = Int.MAX_VALUE

    constructor(min: Int = 0, max: Int = Int.MAX_VALUE, @StringRes errorRes: Int, vararg params: Any) : super(
        errorRes,
        params
    ) {
        this.min = min
        this.max = max
    }

    constructor(
        min: Int = 0,
        max: Int = Int.MAX_VALUE,
        errorMessage: String = "Length must be from $min to $max"
    ) : super(errorMessage) {
        this.min = min
        this.max = max
    }

    constructor(min: Int = 0, max: Int = Int.MAX_VALUE, exception: Exception) : super(exception) {
        this.min = min
        this.max = max
    }

    constructor(min: Int = 0, max: Int = Int.MAX_VALUE, status: Enum<*>) : super(status) {
        this.min = min
        this.max = max
    }

    override fun check(value: Any?): Boolean {
        if (value == null) {
            throw NullPointerException()
        } else {
            val length = value.toString().length
            return length in min..max
        }
    }
}