package by.esas.tools.accesscontainer.support.supporter

import android.content.Context
import by.esas.tools.logger.IErrorModel
import javax.crypto.SecretKey

interface IUtil<E:Enum<E>> {
    fun createErrorModel(code: Int, enumName: String): IErrorModel<E>
    fun generatePin(pin: String, userId: String, androidId: String): SecretKey?
    fun checkBiometricSupport(context: Context?, logging: (String, String) -> Unit): Boolean
}