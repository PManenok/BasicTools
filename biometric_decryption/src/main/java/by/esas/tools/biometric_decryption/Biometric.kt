package by.esas.tools.biometric_decryption

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import by.esas.tools.logger.ILogger
import java.io.IOException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class Biometric {
    companion object {
        val TAG: String = Biometric::class.java.simpleName
        private val KEY_ALIAS = Config.BIOM_ALIAS
        var logger: ILogger<*, *>? = null
        private lateinit var sKeyStore: KeyStore
        private fun getKeyStore(): Boolean {
            try {
                sKeyStore = KeyStore.getInstance("AndroidKeyStore")
                sKeyStore.load(null)
                return true
            } catch (e: KeyStoreException) {
                logger?.logError(e)
            } catch (e: IOException) {
                logger?.logError(e)
            } catch (e: NoSuchAlgorithmException) {
                logger?.logError(e)
            } catch (e: CertificateException) {
                logger?.logError(e)
            }
            return false
        }

        fun deleteInvalidKey(userId: String) {
            logger?.logInfo("deleteInvalidKey")
            if (getKeyStore() && sKeyStore.containsAlias(KEY_ALIAS + userId)) {
                try {
                    sKeyStore.deleteEntry(KEY_ALIAS + userId)
                } catch (e: KeyStoreException) {
                    logger?.logError(e)
                }
            }
        }
    }

    enum class DialogMode {
        ENCRYPT_MODE,
        DECRYPT_MODE
    }

    private val listener: BiometricUserInfo
    protected val executor: ExecutorService
    private lateinit var sCipher: Cipher
    private val biometricPrompt: BiometricPrompt
    private val userId: String

    constructor(
        activity: FragmentActivity,
        callback: BiometricPrompt.AuthenticationCallback,
        listener: BiometricUserInfo,
        executor: ExecutorService = Executors.newSingleThreadExecutor()
    ) {
        this.listener = listener
        this.executor = executor
        userId = listener.getCurrentUser()
        biometricPrompt = BiometricPrompt(activity, executor, callback)
    }

    constructor(
        fragment: Fragment,
        callback: BiometricPrompt.AuthenticationCallback,
        listener: BiometricUserInfo,
        executor: ExecutorService = Executors.newSingleThreadExecutor()
    ) {
        this.listener = listener
        this.executor = executor
        userId = listener.getCurrentUser()
        biometricPrompt = BiometricPrompt(fragment, executor, callback)
    }

    fun authenticate(mode: DialogMode, info: BiometricPrompt.PromptInfo): BiometricPrompt {
        if (prepare() && initCipher(convertMode(mode))) {
            val cipher = BiometricPrompt.CryptoObject(sCipher)
            biometricPrompt.authenticate(info, cipher)
        } else {
            biometricPrompt.cancelAuthentication()
            throw DecryptionException(DecryptionEnum.BIOMETRIC_DECRYPTION_FAILED)
        }
        return biometricPrompt
    }

    fun createInfo(title: String, negativeText: String): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder().setTitle(title).setNegativeButtonText(negativeText).build()
    }


    private fun prepare(): Boolean {
        return getKeyStore() && getCipher() && isKeyReady()
    }


    private fun convertMode(mode: DialogMode): Int {
        return when (mode) {
            DialogMode.ENCRYPT_MODE -> Cipher.ENCRYPT_MODE
            DialogMode.DECRYPT_MODE -> Cipher.DECRYPT_MODE
        }
    }

    private fun initCipher(mode: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val key = sKeyStore.getKey(KEY_ALIAS + userId, null) as SecretKey
                if (mode == Cipher.ENCRYPT_MODE) {
                    sCipher.init(mode, key)
                    listener.setCurrentUserIv(sCipher.iv)
                } else {
                    val iv = listener.getCurrentUserIv()
                    sCipher.init(mode, key, IvParameterSpec(iv))
                }

                return true
            } catch (e: NoSuchAlgorithmException) {
                logger?.logError(e)
            } catch (e: KeyStoreException) {
                logger?.logError(e)
            } catch (e: CertificateException) {
                logger?.logError(e)
            } catch (e: IOException) {
                logger?.logError(e)
            } catch (e: NoSuchAlgorithmException) {
                logger?.logError(e)
            } catch (e: InvalidKeyException) {
                logger?.logError(e)
                deleteInvalidKey(userId)
            }
            false
        } else
            false
    }

    private fun generateNewKey(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")


            val parameter = KeyGenParameterSpec.Builder(
                KEY_ALIAS + userId,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //todo ask if needed
                parameter.setInvalidatedByBiometricEnrollment(false)
            }*/

            generator.init(
                parameter.build()
            )
            generator.generateKey() // random key in key store
            true
        } else
            false
    }


    private fun isKeyReady(): Boolean {
        try {
            //deleteInvalidKey()
            val contains = sKeyStore.containsAlias(KEY_ALIAS + userId)
            return contains || generateNewKey()
        } catch (e: KeyStoreException) {
            logger?.logError(e)
        }
        return false
    }

    private fun getCipher(): Boolean {
        try {
            sCipher = Cipher.getInstance("AES/CBC/PKCS7Padding")//"RSA/ECB/OAEPWithSHA-256AndMGF1Padding"
            return true
        } catch (e: NoSuchAlgorithmException) {
            logger?.logError(e)
        } catch (e: NoSuchPaddingException) {
            logger?.logError(e)
        }
        return false
    }

    interface BiometricUserInfo {
        fun getCurrentUser(): String
        fun setCurrentUserIv(iv: ByteArray)
        fun getCurrentUserIv(): ByteArray
    }
}



