/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.checker.checks

import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck

open class RegexCheck : BaseCheck {

    override var TAG: String = CustomCheck::class.java.simpleName
    private var regex: String = ""

    constructor(regex: String, @StringRes errorRes: Int) : super(errorRes) {
        this.regex = regex
    }

    constructor(regex: String, errorMessage: String = "Does not match regex rule") : super(errorMessage) {
        this.regex = regex
    }

    constructor(regex: String, exception: Exception) : super(exception) {
        this.regex = regex
    }

    constructor(regex: String, status: Enum<*>) : super(status) {
        this.regex = regex
    }

    /*companion object {
        const val defaultErrorText: String = "Does not match regex rule"
        fun getInstance(regex: String, exception: Int, isError: Boolean = true): RegexCheck {
            return if (isError) {
                RegexCheck(regex, ServerException(exception, "Wrong input format"))
            } else
                RegexCheck(regex, exception)
        }
    }*/

    override fun check(value: Any?): Boolean {
        if (value == null) {
            throw NullPointerException()
        } else {
            return value.toString().matches(regex.toRegex())
        }
    }
}