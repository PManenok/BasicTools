package by.esas.tools.logger.handler

import by.esas.tools.logger.BaseErrorModel

open class ErrorData<E : Enum<E>, M : BaseErrorModel<E>>(
    val throwable: Throwable? = null,
    val model: M? = null,
    val showType: ShowErrorType,
    val doOnDialogOK: () -> Unit = {},
    var handled: Boolean = false,
    val actionName: String? = null
)