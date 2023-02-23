/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.checker

open class Checker {

    protected open var inContinueMode: Boolean = true
    protected open var showError: Boolean = true
    protected open var listener: CheckListener? = null
    protected open var getter: BaseCheck.ErrorGetter? = null

    fun setMode(isContinue: Boolean): Checker {
        inContinueMode = isContinue
        return this
    }

    fun setShowError(showError: Boolean): Checker {
        this.showError = showError
        return this
    }

    fun setListener(listener: CheckListener?): Checker {
        this.listener = listener
        return this
    }

    fun setErrorGetter(getter: BaseCheck.ErrorGetter?): Checker {
        this.getter = getter
        return this
    }

    open fun validate(checks: List<Checking>) {
        var result: Boolean = true
        if (inContinueMode) {
            checks.forEach {
                result = it.checkField(showError, getter) && result
            }
        } else {
            for (it in checks) {
                result = it.checkField(showError, getter) && result
                if (!result)
                    break
            }
        }
        if (result)
            listener?.onSuccess()
        else
            listener?.onFailed()
    }

    interface CheckListener {

        fun onFailed() {}
        fun onSuccess() {}
    }
}