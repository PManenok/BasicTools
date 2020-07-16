package by.esas.tools.accesscontainer.dialog

interface BiometricUserInfo {
        fun getCurrentUser(): String
        fun setCurrentUserIv(iv: ByteArray)
        fun getCurrentUserIv(): ByteArray
    }