package by.esas.tools.accesscontainer

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.accesscontainer.error.IModel
import by.esas.tools.accesscontainer.support.IContainerCancellationCallback

interface IRefreshContainer<T> {
    fun setToken(token: Token)
    fun getToken(): String

    fun setCancellationCallback(callback: IContainerCancellationCallback)
    fun setActivity(activity: FragmentActivity)
    fun setParams(manager: FragmentManager)
    fun setForgotPasswordAction(enable: Boolean, forgotPasswordAction: () -> Unit)

    fun refresh(repeat: () -> Unit, onError: (IModel<T>) -> Unit, onCancel: () -> Unit)
    fun checkAccess(refreshExplicitly: Boolean = false, response: ContainerRequest<String, T>.() -> Unit)

    fun onCancel()

    fun clearAccess()
    fun clear()
}