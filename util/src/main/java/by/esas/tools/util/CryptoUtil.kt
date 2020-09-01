package by.esas.tools.util

import android.util.Base64
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun generateKey(): String {
    val generator = KeyGenerator.getInstance("AES")
    val key: SecretKey = generator.generateKey()
    return Base64.encodeToString(key.encoded, Base64.NO_WRAP)
}

fun generateRSAKey(): KeyPair {
    val kpg = KeyPairGenerator.getInstance("RSA")
    kpg.initialize(1024)
    return kpg.generateKeyPair()
}

fun encryptRSA(info: String, rowPublic: String): String {
    //val iv = ByteArray(16)
    val key = Base64.decode(rowPublic, Base64.NO_WRAP)
    val publicKey = KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(key))
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    //val decoded = Base64.decode(info, Base64.NO_WRAP)
    val encrypted = cipher.doFinal(info.toByteArray())//cipher.doFinal(decoded)
    return Base64.encodeToString(encrypted, Base64.NO_WRAP)
}

fun decryptRSA(info: String, rowPrivate: String): String {
    //val iv = ByteArray(16)
    val key = Base64.decode(rowPrivate, Base64.NO_WRAP)
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(key))
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    val decoded = Base64.decode(info, Base64.NO_WRAP)
    val decrypted = cipher.doFinal(decoded)
    return decrypted.toString(Charsets.UTF_8)//java.lang.String(decrypted, Charsets.UTF_8).toString()//Base64.encodeToString(decrypted, Base64.NO_WRAP)
}

fun encryptAES(info: String, row: String): String {
    val key = Base64.decode(row, Base64.NO_WRAP)
    val secretKey = SecretKeySpec(key, "AES")
    return encryptAES(info, secretKey)
}

fun decryptAES(info: String, row: String): String {
    val key = Base64.decode(row, Base64.NO_WRAP)
    val secretKey = SecretKeySpec(key, "AES")
    return decryptAES(info, secretKey)
}

fun encryptAES(info: String, secretKey: SecretKey): String {
    val iv = ByteArray(16)
    val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
    return encryptAES(info, cipher)
}

fun decryptAES(info: String, secretKey: SecretKey): String {
    val iv = ByteArray(16)
    val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
    return decryptAES(info, cipher)
}

fun encryptAES(info: String, cipher: Cipher): String {
    //val encoded = Base64.decode(info, Base64.NO_WRAP)
    val cipherText = cipher.doFinal(info.toByteArray())
    return Base64.encodeToString(cipherText,Base64.NO_WRAP)
}

fun decryptAES(info: String, cipher: Cipher): String {
    val decoded = Base64.decode(info, Base64.NO_WRAP)
    val decrypted = cipher.doFinal(decoded)
    return decrypted.toString(Charsets.UTF_8)//Base64.encodeToString(decrypted, Base64.NO_WRAP)
}