package by.esas.tools.checker

import android.content.Context
import androidx.annotation.StringRes

abstract class BaseCheck {
    abstract var TAG: String

    @StringRes
    protected var errorRes: Int = 0
    protected var params: Array<out Any> = emptyArray()
    protected var errorText: String = "Error"
    protected var exceptionValue: Exception? = null
    protected var errStatus: Enum<*>? = null
    protected var errorGetter: ErrorGetter = object : ErrorGetter {}

    private constructor()

    constructor(errorText: String) : this() {
        this.errorText = errorText
    }

    constructor(@StringRes errorRes: Int, vararg params: Any) : this() {
        this.errorRes = errorRes
        this.params = params
    }

    /*constructor(@StringRes errorRes: Int) : this() {
        this.errorRes = errorRes
    }*/

    constructor(error: Exception) : this() {
        this.exceptionValue = error
    }

    constructor(status: Enum<*>) : this() {
        this.errStatus = status
    }

    abstract fun check(value: Any?): Boolean

    open fun getError(context: Context?, errorGetter: ErrorGetter? = null): String {
        return if (errorRes != 0 && context != null) {
            context.resources.getString(errorRes, params)
        } else if (exceptionValue != null) {
            (errorGetter ?: this.errorGetter).getErrorMsgFromException(exceptionValue)
        } else if (errStatus != null) {
            (errorGetter ?: this.errorGetter).getErrorMsgFromStatus(errStatus)
        } else errorText
    }

    fun getException(): Exception? {
        return exceptionValue
    }

    fun getStatus(): Enum<*>? {
        return errStatus
    }

    interface ErrorGetter {
        fun getErrorMsgFromException(exception: Exception?): String {
            return exception?.message ?: ""
        }

        fun getErrorMsgFromStatus(status: Enum<*>?): String {
            return status?.name ?: ""
        }
    }
}

/*fun test() {
    object : BaseCheck(0, 2, "")
}*/

/*abstract class BaseCheck {
    val errorMessage: String
    val errorRes: Int
    val exception: ServerException?

    constructor(errorMessage: String = "") {
        this.errorMessage = errorMessage
        this.errorRes = -1
        this.exception = null
    }

    constructor(errorRes: Int = -1) {
        this.errorMessage = ""
        this.errorRes = errorRes
        this.exception = null
    }

    constructor(exception: ServerException) {
        this.errorMessage = exception.message ?: ""
        this.errorRes = -1
        this.exception = exception
    }

    abstract fun check(value: Any?): Boolean

    fun getError(): ServerException? {
        return exception
    }
}*/