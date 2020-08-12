package by.esas.tools.accesscontainer

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.accesscontainer.support.IContainerCancellationCallback
import by.esas.tools.logger.IErrorModel

interface IRefreshContainer<E : Enum<E>> {
    fun setToken(token: Token)
    fun getToken(): String

    fun setCancellationCallback(callback: IContainerCancellationCallback)
    fun setActivity(activity: FragmentActivity)
    fun setParams(manager: FragmentManager)
    fun setForgotPasswordAction(enable: Boolean, forgotPasswordAction: () -> Unit)

    /**
     * This function can refresh token without secret key
     * Access token would be refreshed anyway
     **/
    fun refresh(onComplete: (String?) -> Unit, onError: (IErrorModel<E>) -> Unit, onCancel: () -> Unit)

    /**
     * This function would always invoke dialogs with secrets if it can, because it checks if user can access secret
     * can refresh token or not
     * @see refreshExplicitly
     **/
    fun checkAccess(refreshExplicitly: Boolean = false, response: ContainerRequest<String, E>.() -> Unit)

    fun onCancel()

    fun clearAccess()
    fun clear()
}