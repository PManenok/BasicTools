package by.esas.tools.biometric_decryption

class DecryptionException(msg: String = "Decryption failed") : Exception(msg) {
    constructor(enum: DecryptionEnum) : this(enum.name)
}