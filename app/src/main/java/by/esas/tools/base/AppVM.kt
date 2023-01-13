package by.esas.tools.base

import by.esas.tools.App
import by.esas.tools.baseui.standard.StandardViewModel
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.logger.BaseLoggerImpl
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.logger.ILogger
import by.esas.tools.utils.logger.ErrorModel
import javax.inject.Inject

abstract class AppVM : StandardViewModel<ErrorModel>() {
    @Inject
    lateinit var mapper: AppErrorMapper

    override fun provideMapper(): IErrorMapper<ErrorModel> {
        return mapper
    }

    override var logger: ILogger<*> = BaseLoggerImpl(context = App.appContext)
}