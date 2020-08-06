package by.esas.tools.checker

import android.content.Context
import androidx.annotation.StringRes

abstract class BaseCheck {
    abstract var TAG: String

    @StringRes
    protected var errorRes: Int = 0
    protected var params: Array<out Any> = emptyArray()
    protected var errorText: String = "Error"
    protected var exception: Exception? = null
    protected var status: Enum<*>? = null

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
        this.exception = error
    }

    constructor(status: Enum<*>) : this() {
        this.status = status
    }

    abstract fun check(value: Any?): Boolean

    open fun getError(context: Context?): String {
        return if (errorRes != 0 && context != null) {
            context.resources.getString(errorRes, params)
        } else if (exception != null) {
            getErrorMsgFromException()
        } else if (status != null) {
            getErrorMsgFromStatus()
        } else errorText
    }

    protected open fun getErrorMsgFromException(): String {
        return exception?.message ?: ""
    }

    protected open fun getErrorMsgFromStatus(): String {
        return status?.name ?: ""
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