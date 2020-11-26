package by.esas.tools.accesscontainer.support.supporter

import android.content.Context
import by.esas.tools.logger.BaseErrorModel
import javax.crypto.SecretKey

interface IUtil<E : Enum<E>, M : BaseErrorModel<E>> {
    fun createErrorModel(code: Int, enumName: String): M
    fun generatePin(pin: String, userId: String, androidId: String): SecretKey?
    fun checkBiometricSupport(context: Context?, logging: (String, String) -> Unit): Boolean
}