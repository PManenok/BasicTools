package by.esas.tools.biometric_decryption

enum class DecryptionEnum {
    PIN_DECRYPTION_FAILED,
    BIOMETRIC_DECRYPTION_FAILED,
    PRIVATE_KEY_DECRYPTION_FAILED,
    SECRET_KEY_DECRYPTION_FAILED;

    override fun toString(): String {
        return name
    }
}