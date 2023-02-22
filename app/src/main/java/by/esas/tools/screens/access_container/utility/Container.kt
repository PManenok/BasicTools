package by.esas.tools.screens.access_container.utility

import android.content.Context
import by.esas.tools.accesscontainer.Refresher
import by.esas.tools.accesscontainer.dialog.BiometricUserInfo
import by.esas.tools.accesscontainer.dialog.DialogProvider
import by.esas.tools.accesscontainer.error.IErrorMapper
import by.esas.tools.accesscontainer.support.IContainerExecutor
import by.esas.tools.accesscontainer.support.ITypeManager
import by.esas.tools.accesscontainer.support.supporter.IUtil
import by.esas.tools.accesscontainer.support.supporter.Supporter
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.logger.ILogger
import by.esas.tools.util.TAGk
import by.esas.tools.utils.logger.ErrorModel
import javax.crypto.SecretKey

class Container(
    logger: ILogger<ErrorModel>,
    executor: IContainerExecutor<ErrorModel>,
    typeManager: ITypeManager,
    userInfo: BiometricUserInfo,
    mapper: IErrorMapper<ErrorModel>,
    dialogProvider: DialogProvider,
    supporter: Supporter
) : Refresher<ErrorModel>(logger, executor, typeManager, userInfo, mapper, dialogProvider, supporter) {

    companion object {
        fun getInstance(
            logger: ILogger<ErrorModel>,
            executor: IContainerExecutor<ErrorModel>,
            appMapper: AppErrorMapper,
            typeManager: ITypeManager,
            userInfo: BiometricUserInfo
        ): Container {
            val errorMapper = object : IErrorMapper<ErrorModel> {
                private val mapper = appMapper
                override fun mapError(e: Exception): ErrorModel {
                    return mapper.mapErrorException(TAGk, e)
                }

                override fun createErrorModel(code: Int, enumName: String): ErrorModel {
                    return mapper.createModel(code, enumName)
                }

            }

            return Container(
                logger = logger,
                executor = executor,
                typeManager = typeManager,
                userInfo = userInfo,
                mapper = errorMapper,
                dialogProvider = DialogProviderImpl(logger),
                supporter = Supporter(ResProviderImpl(), provideUtil())
            )
        }

        private fun provideUtil(): IUtil {
            return object : IUtil {

                override fun generatePin(pin: String, userId: String, androidId: String): SecretKey? {
                    return by.esas.tools.biometric_decryption.generatePin(pin, userId, androidId)
                }

                override fun checkBiometricSupport(
                    context: Context?,
                    logging: (String, String) -> Unit
                ): Boolean {
                    return context?.let {
                        by.esas.tools.biometric_decryption.checkBiometricSupport(
                            context,
                            logging
                        )
                    }
                        ?: false
                }
            }
        }
    }
}