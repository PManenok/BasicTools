package by.esas.tools.accesscontainer.support.supporter

import android.content.Context
import javax.crypto.SecretKey

interface IUtil<T> {
    fun createErrorModel(code: Int, enumName: String): T
    fun generatePin(pin: String, userId: String, androidId: String): SecretKey?
    fun checkBiometricSupport(context: Context?, logging: (String, String) -> Unit): Boolean
}