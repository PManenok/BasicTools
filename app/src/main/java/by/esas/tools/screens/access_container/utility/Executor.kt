package by.esas.tools.screens.access_container.utility

import by.esas.tools.accesscontainer.ContainerRequest
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.accesscontainer.support.IContainerExecutor
import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import by.esas.tools.logger.ILogger
import by.esas.tools.utils.logger.ErrorModel
import javax.crypto.Cipher
import javax.crypto.SecretKey

class Executor(
    private val logger: ILogger<ErrorModel>
) : IContainerExecutor<ErrorModel> {

    val TAG: String = Executor::class.java.simpleName

    var provider: SecretProvider = SecretProvider()

    override fun refreshAccessUC(
        refreshToken: String,
        block: ContainerRequest<Token?, ErrorModel>.() -> Unit
    ) {
        logger.order("refreshAccessUC")

        val response = ContainerRequest<Token?, ErrorModel>().apply { block() }
        provider.refreshToken().let {
            if (it.type == Result.SUCCESS_RESULT) {
                response(it.result)
            } else {
                response(ErrorModel(0, AppErrorStatusEnum.ACCESS_DENIED))
            }
        }
    }

    override fun getSecretsUC(block: ContainerRequest<List<AuthType>, ErrorModel>.() -> Unit) {
        logger.order("getSecretsUC")

        val response = ContainerRequest<List<AuthType>, ErrorModel>().apply { block() }
        provider.getSecrets().let {
            if (it.type == Result.SUCCESS_RESULT) {
                response(it.result!!)
            } else {
                response(ErrorModel(0, AppErrorStatusEnum.APP_USER_HAS_NO_SECRETS))
            }
        }
    }

    override fun refreshWithSecretUC(
        pinKey: SecretKey?,
        cipher: Cipher?,
        block: ContainerRequest<Token, ErrorModel>.() -> Unit
    ) {
        logger.order("refreshWithSecretUC pinKey!=null = ${pinKey != null}, cipher!=null = ${cipher != null}")

        val response = ContainerRequest<Token, ErrorModel>().apply { block() }
        provider.refreshWithSecret().let {
            if (it.type == Result.SUCCESS_RESULT) {
                response(it.result!!)
            } else {
                val status =
                    if (pinKey != null) AppErrorStatusEnum.APP_PIN_DECRYPTION_FAILED
                    else AppErrorStatusEnum.APP_BIOMETRIC_DECRYPTION_FAILED
                response(ErrorModel(0, status))
            }
        }
    }

    override fun checkSecretUC(
        pinKey: SecretKey?,
        cipher: Cipher?,
        block: ContainerRequest<Pair<Boolean, String>, ErrorModel>.() -> Unit
    ) {
        logger.order("checkSecretUC pinKey!=null = ${pinKey != null}, cipher!=null = ${cipher != null}")

        val response = ContainerRequest<Pair<Boolean, String>, ErrorModel>().apply { block() }
        provider.checkSecret().let {
            if (it.type == Result.SUCCESS_RESULT) {
                response(it.result!!)
            } else {
                response(ErrorModel(0, AppErrorStatusEnum.SECRET_CHECK_NOT_MATCH))
            }
        }
    }

    override fun refreshWithSecretUC(
        secret: String,
        block: ContainerRequest<Token, ErrorModel>.() -> Unit
    ) {
        logger.order("refreshWithSecretUC secret not empty ${secret.isNotBlank()}")

        val response = ContainerRequest<Token, ErrorModel>().apply { block() }
        provider.refreshWithSecretText().let {
            if (it.type == Result.SUCCESS_RESULT) {
                response(it.result!!)
            } else {
                response(ErrorModel(0, AppErrorStatusEnum.APP_REFRESH_TOKEN_DECRYPTION_FAILED))
            }
        }
    }

    override fun authorizationInSystemUC(
        password: String,
        block: ContainerRequest<Token, ErrorModel>.() -> Unit
    ) {
        logger.order("authorizationInSystemUC password not empty ${password.isNotBlank()}")

        val response = ContainerRequest<Token, ErrorModel>().apply { block() }
        provider.authorizationInSystem(password).let {
            if (it.type == Result.SUCCESS_RESULT) {
                response(it.result!!)
            } else {
                response(ErrorModel(400, AppErrorStatusEnum.API_WRONG_PASSWORD))
            }
        }
    }

    override fun createSecretUC(
        refreshToken: String,
        cipher: Cipher?,
        pinKey: SecretKey?,
        block: ContainerRequest<Boolean, ErrorModel>.() -> Unit
    ) {
        logger.order("createSecretUC pinKey!=null = ${pinKey != null}, cipher!=null = ${cipher != null}")

        val response = ContainerRequest<Boolean, ErrorModel>().apply { block() }
        provider.createSecret(refreshToken).let {
            if (it.type == Result.SUCCESS_RESULT) {
                response(it.result!!)
            } else {
                response(ErrorModel(400, AppErrorStatusEnum.API_WRONG_PASSWORD))
            }
        }
        response(true)
    }

    override fun deleteSecretTypeUC(
        type: AuthType,
        block: ContainerRequest<Boolean, ErrorModel>.() -> Unit
    ) {
        logger.order("deleteSecretTypeUC type=$type")

        val response = ContainerRequest<Boolean, ErrorModel>().apply { block() }
        response(true)
    }

    override fun deleteSecretsUC(block: ContainerRequest<Boolean, ErrorModel>.() -> Unit) {
        logger.order("deleteSecretsUC")

        val response = ContainerRequest<Boolean, ErrorModel>().apply { block() }
        response(true)
    }

    override fun unsubscribe() {
        logger.order("unsubscribe")
    }
}