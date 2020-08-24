package by.esas.tools.checking

import by.esas.tools.checker.BaseCheck
import by.esas.tools.checker.Checker

class AppChecker : Checker() {
    override var getter: BaseCheck.ErrorGetter? = object :BaseCheck.ErrorGetter{
        override fun getErrorMsgFromException(exception: Exception?): String {
            return super.getErrorMsgFromException(exception)
        }

        override fun getErrorMsgFromStatus(status: Enum<*>?): String {
            return super.getErrorMsgFromStatus(status)
        }
    }
    override var inContinueMode: Boolean = true
    override var showError: Boolean = true

}