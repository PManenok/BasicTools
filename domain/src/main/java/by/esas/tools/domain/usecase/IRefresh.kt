package by.esas.tools.domain.usecase

import by.esas.tools.logger.BaseErrorModel

interface IRefresh<E : Enum<E>, Model : BaseErrorModel<E>> {
    fun getToken(): String
    fun refresh(repeat: () -> Unit, onError: (Model) -> Unit, onCancel: () -> Unit)
    fun onCancel()
}