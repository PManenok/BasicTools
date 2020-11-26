package by.esas.tools.accesscontainer.support

import by.esas.tools.accesscontainer.ContainerRequest
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.logger.BaseErrorModel
import javax.crypto.Cipher
import javax.crypto.SecretKey

interface IContainerExecutor<E:Enum<E>, M : BaseErrorModel<E>> {
    fun refreshAccessUC(refreshToken: String, block: ContainerRequest<Token?, E, M>.() -> Unit)

    fun getSecretsUC(block: ContainerRequest<List<AuthType>, E, M>.() -> Unit)

    fun refreshWithSecretUC(pinKey: SecretKey? = null, cipher: Cipher? = null, block: ContainerRequest<Token, E, M>.() -> Unit)

    fun checkSecretUC(pinKey: SecretKey? = null, cipher: Cipher? = null, block: ContainerRequest<Pair<Boolean, String>, E, M>.() -> Unit)

    fun refreshWithSecretUC(secret: String, block: ContainerRequest<Token, E, M>.() -> Unit)

    fun authorizationInSystemUC(password: String, block: ContainerRequest<Token, E, M>.() -> Unit)

    fun createSecretUC(token: String, cipher: Cipher? = null, pinKey: SecretKey? = null, block: ContainerRequest<Boolean, E, M>.() -> Unit)

    fun deleteSecretTypeUC(type: AuthType, block: ContainerRequest<Boolean, E, M>.() -> Unit)

    fun deleteSecretsUC(block: ContainerRequest<Boolean, E, M>.() -> Unit)

    fun unsubscribe()
}