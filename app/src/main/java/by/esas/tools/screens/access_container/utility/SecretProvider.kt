package by.esas.tools.screens.access_container.utility

import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token

open class SecretProvider {

    var errorOnRefreshWithToken: Boolean = true
    var errorOnGetSecrets: Boolean = true
    var errorOnRefresh: Boolean = true
    var errorOnRefreshWithSecret: Boolean = true
    var errorOnCheckSecret: Boolean = true
    var errorOnCreateSecret: Boolean = true

    var authTypes: List<AuthType> = emptyList()
    var correctPassword = "12345678"
    private val secret: String = "secret"
    private var currentRefresh: String = "refreshToken"
    private var currentToken: String = "token"

    fun refreshToken(): SecretResult<Token?> {
        return if (errorOnRefreshWithToken) {
            SecretResult(type = Result.ERROR_RESULT)
        } else {
            val random = Math.random()
            currentRefresh = "refreshToken_$random"
            currentToken = "token_$random"
            SecretResult(type = Result.SUCCESS_RESULT, getToken())
        }
    }

    fun getSecrets(): SecretResult<List<AuthType>> {
        return if (errorOnGetSecrets) {
            SecretResult(type = Result.ERROR_RESULT)
        } else {
            SecretResult(type = Result.SUCCESS_RESULT, authTypes)
        }
    }

    fun refreshWithSecret(): SecretResult<Token> {
        return if (errorOnRefresh) {
            SecretResult(type = Result.ERROR_RESULT)
        } else {
            val random = Math.random()
            currentRefresh = "refreshToken_$random"
            currentToken = "token_$random"
            SecretResult(type = Result.SUCCESS_RESULT, getToken())
        }
    }

    fun refreshWithSecretText(): SecretResult<Token> {
        return if (errorOnRefreshWithSecret) {
            SecretResult(type = Result.ERROR_RESULT)
        } else {
            val random = Math.random()
            currentRefresh = "refreshToken_$random"
            currentToken = "token_$random"
            SecretResult(type = Result.SUCCESS_RESULT, getToken())
        }
    }

    fun checkSecret(): SecretResult<Pair<Boolean, String>> {
        return if (errorOnCheckSecret) {
            SecretResult(type = Result.ERROR_RESULT)
        } else {
            SecretResult(type = Result.SUCCESS_RESULT, Pair(true, secret))
        }
    }

    fun authorizationInSystem(password: String): SecretResult<Token> {
        return if (password == correctPassword) {
            val random = Math.random()
            currentRefresh = "refreshToken_$random"
            currentToken = "token_$random"
            SecretResult(type = Result.SUCCESS_RESULT, getToken())
        } else {
            SecretResult(type = Result.ERROR_RESULT)
        }
    }

    fun createSecret(refreshToken: String): SecretResult<Boolean> {
        return if (errorOnCreateSecret) {
            SecretResult(type = Result.ERROR_RESULT)
        } else {
            currentRefresh = refreshToken
            SecretResult(type = Result.SUCCESS_RESULT, true)
        }
    }

    fun deleteSecretType(type: AuthType): SecretResult<Boolean> {
        return SecretResult(type = Result.SUCCESS_RESULT, true)
    }

    fun deleteSecrets(): SecretResult<Boolean> {
        return SecretResult(type = Result.SUCCESS_RESULT, true)
    }

    private fun getToken(): Token {
        return Token(accessToken = currentToken, refreshToken = currentRefresh)
    }

    fun setToken(token: Token) {
        currentToken = token.accessToken
        currentRefresh = token.refreshToken ?: ""
    }

    fun getCurrentToken(): Token {
        return Token(currentToken, currentRefresh)
    }
}