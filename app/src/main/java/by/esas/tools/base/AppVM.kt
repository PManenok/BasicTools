package by.esas.tools.base

import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.baseui.standard.StandardViewModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.logger.ILogger
import by.esas.tools.utils.logger.ErrorModel
import by.esas.tools.utils.logger.LoggerImpl
import javax.inject.Inject

abstract class AppVM : StandardViewModel<ErrorModel>() {
    @Inject
    lateinit var mapper: AppErrorMapper

    override var logger: ILogger<*> = LoggerImpl()

    override fun provideMapper(): IErrorMapper<ErrorModel> {
        return mapper
    }
}