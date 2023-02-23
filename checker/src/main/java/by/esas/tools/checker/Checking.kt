/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.checker

import by.esas.tools.checker.checks.NotEmptyCheck

abstract class Checking {

    protected open var checkEmpty: Boolean = false
    protected open var notEmptyRule: NotEmptyCheck? = null
    protected open val checks: MutableList<BaseCheck> = mutableListOf()

    abstract fun checkField(setError: Boolean, errorGetter: BaseCheck.ErrorGetter? = null): Boolean

    open fun addCheck(check: BaseCheck): Checking {
        checks.add(check)
        return this
    }

    open fun setCheckIfEmpty(doCheck: Boolean, check: NotEmptyCheck?): Checking {
        checkEmpty = doCheck && check != null
        notEmptyRule = check
        return this
    }
}