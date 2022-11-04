package by.esas.tools.utils.checking

import by.esas.tools.checker.BaseCheck
import by.esas.tools.checker.Checker
import by.esas.tools.checker.Checking
import by.esas.tools.checker.IFocusableChecking

open class AppChecker : Checker() {
    override var getter: BaseCheck.ErrorGetter? = object : BaseCheck.ErrorGetter {
        override fun getErrorMsgFromException(exception: Exception?): String {
            return super.getErrorMsgFromException(exception)
        }

        override fun getErrorMsgFromStatus(status: Enum<*>?): String {
            return super.getErrorMsgFromStatus(status)
        }
    }
    override var inContinueMode: Boolean = true
    override var showError: Boolean = true
    open var focusOnFirstError: Boolean = false

    override fun validate(checks: List<Checking>) {
        var result: Boolean = true
        if (inContinueMode) {
            checks.forEach {
                val tempResult = it.checkField(showError, getter) && result
                if (focusOnFirstError && result && !tempResult && it is IFocusableChecking)
                    it.requestFocus()
                result = tempResult
            }
        } else {
            for(it in checks) {
                result = it.checkField(showError, getter) && result
                if (!result) {
                    if (focusOnFirstError && it is IFocusableChecking)
                        it.requestFocus()
                    break
                }
            }
        }
        if (result)
            listener?.onSuccess()
        else
            listener?.onFailed()
    }
}