package by.esas.tools.accesscontainer.support

import by.esas.tools.accesscontainer.ContainerRequest
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.logger.IErrorModel
import javax.crypto.Cipher
import javax.crypto.SecretKey

interface IContainerExecutor<E:Enum<E>> {
    fun refreshAccessUC(refreshToken: String, block: ContainerRequest<Token?, E>.() -> Unit)

    fun getSecretsUC(block: ContainerRequest<List<AuthType>, E>.() -> Unit)

    fun refreshWithSecretUC(pinKey: SecretKey? = null, cipher: Cipher? = null, block: ContainerRequest<Token, E>.() -> Unit)

    fun checkSecretUC(pinKey: SecretKey? = null, cipher: Cipher? = null, block: ContainerRequest<Pair<Boolean, String>, E>.() -> Unit)

    fun refreshWithSecretUC(secret: String, block: ContainerRequest<Token, E>.() -> Unit)

    fun authorizationInSystemUC(password: String, block: ContainerRequest<Token, E>.() -> Unit)

    fun createSecretUC(token: String, cipher: Cipher? = null, pinKey: SecretKey? = null, block: ContainerRequest<Boolean, E>.() -> Unit)

    fun deleteSecretTypeUC(type: AuthType, block: ContainerRequest<Boolean, E>.() -> Unit)

    fun deleteSecretsUC(block: ContainerRequest<Boolean, E>.() -> Unit)

    fun unsubscribe()
}