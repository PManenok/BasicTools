package by.esas.tools.accesscontainer.support

import by.esas.tools.accesscontainer.ContainerRequest
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token
import javax.crypto.Cipher
import javax.crypto.SecretKey

interface IContainerExecutor<T> {
    fun refreshAccessUC(refreshToken: String, block: ContainerRequest<Token?, T>.() -> Unit)

    fun getSecretsUC(block: ContainerRequest<List<AuthType>, T>.() -> Unit)

    fun refreshWithSecretUC(pinKey: SecretKey? = null, cipher: Cipher? = null, block: ContainerRequest<Token, T>.() -> Unit)

    fun checkSecretUC(pinKey: SecretKey? = null, cipher: Cipher? = null, block: ContainerRequest<Pair<Boolean, String>, T>.() -> Unit)

    fun refreshWithSecretUC(secret: String, block: ContainerRequest<Token, T>.() -> Unit)

    fun authorizationInSystemUC(password: String, block: ContainerRequest<Token, T>.() -> Unit)

    fun createSecretUC(token: String, cipher: Cipher? = null, pinKey: SecretKey? = null, block: ContainerRequest<Boolean, T>.() -> Unit)

    fun deleteSecretTypeUC(type: AuthType, block: ContainerRequest<Boolean, T>.() -> Unit)

    fun unsubscribe()
}