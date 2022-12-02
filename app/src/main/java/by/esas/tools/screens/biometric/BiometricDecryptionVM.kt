package by.esas.tools.screens.biometric

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.R
import by.esas.tools.base.AppVM
import by.esas.tools.biometric_decryption.Biometric
import javax.inject.Inject

class BiometricDecryptionVM @Inject constructor() : AppVM() {
    val messageLive = MutableLiveData<String>()
    var isBiometricAvailable = false
    var showBiometricDialog: (block: (Fragment) -> Unit) -> Unit = {}

    fun showBiometricDialog() {
        showBiometricDialog {
            val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    logger.logInfo(errString.toString())
                    messageLive.postValue(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    logger.logInfo(App.appContext.resources.getString(R.string.biometric_decryption_authentication_succeeded))
                    messageLive.postValue(App.appContext.resources.getString(R.string.biometric_decryption_authentication_succeeded))
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    logger.logInfo(App.appContext.resources.getString(R.string.biometric_decryption_authentication_failed))
                    messageLive.postValue(App.appContext.resources.getString(R.string.biometric_decryption_authentication_failed))
                }
            }
            val builder = Biometric(
                fragment = it,
                callback = biometricCallback,
                listener = object : Biometric.BiometricUserInfo {
                    override fun getCurrentUser(): String {
                        return ""
                    }

                    override fun setCurrentUserIv(iv: ByteArray) {
                    }

                    override fun getCurrentUserIv(): ByteArray {
                        return byteArrayOf()
                    }
                })
            val title = App.appContext.resources.getString(R.string.biometric_decryption_dialog_title)
            val negativeText = App.appContext.resources.getString(R.string.biometric_decryption_dialog_close)
            val info = builder.createInfo(title, negativeText)
            builder.authenticate(Biometric.DialogMode.ENCRYPT_MODE, info)
        }
    }
}