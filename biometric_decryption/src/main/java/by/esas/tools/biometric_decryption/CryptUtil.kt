package by.esas.tools.biometric_decryption

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

fun generatePin(pin: String, userId: String, deviceId: String): SecretKey {
    val salt = createRow(userId, deviceId.toByteArray())
    val encoded = createRow(pin, salt)
    return SecretKeySpec(encoded, "AES")
}

fun createRow(first: String, second: ByteArray): ByteArray {
    val factory = SecretKeyFactory.getInstance("PBKDF2withHmacSHA1")
    val rowSalt = PBEKeySpec(first.toCharArray(), second, 25536, 256)
    return factory.generateSecret(rowSalt).encoded
}

fun checkBiometricSupport(context: Context, logError: (String, String) -> Unit): Boolean {
    //val manager = FingerprintManagerCompat.from(context)
    val keyguardManager = ContextCompat.getSystemService(context, KeyguardManager::class.java) as KeyguardManager
    val tag = "Biometric Support Check"
    val manager = BiometricManager.from(context)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        logError(tag, "This Android version does not support fingerprint authentication.")
        return false
    }

    if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
        logError(tag, "Fingerprint Sensor not supported")
        return false
    }
    val result = manager.canAuthenticate()
    if (result != BiometricManager.BIOMETRIC_SUCCESS) {
        logError(
            tag, when (result) {
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "The hardware is unavailable"
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "The user does not have any biometrics enrolled"
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "There is no biometric hardware"
                else -> "Unknown error (code: $result)"
            }
        )
        return false
    }
    /*if (!manager.hasEnrolledFingerprints()) {
        logError(tag, "No enrolled fingerprints")
        return false
    }*/

    if (!keyguardManager.isKeyguardSecure) {
        logError(tag, "Lock screen security not enabled in Settings")
        return false
    }

    /* if (ActivityCompat.checkSelfPermission(
             context,
             Manifest.permission.USE_BIOMETRIC
         ) != PackageManager.PERMISSION_GRANTED
     ) {
         logError(tag, "Fingerprint authentication permission not enabled")
         return false
     }*/
    return true
}