package by.esas.tools

import androidx.databinding.ViewDataBinding
import by.esas.tools.basedaggerui.simple.SimpleActivity
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.handler.ErrorHandler

abstract class AppActivity<VM : AppVM, B : ViewDataBinding> : SimpleActivity<VM, B, AppErrorStatusEnum, ErrorModel>() {

    override fun provideErrorHandler(): ErrorHandler<AppErrorStatusEnum, ErrorModel> {
        return object : ErrorHandler<AppErrorStatusEnum, ErrorModel>() {
            override fun getErrorMessage(error: ErrorModel): String {
                TODO("Not yet implemented")
            }

            override fun getErrorMessage(e: Throwable): String {
                TODO("Not yet implemented")
            }

            override fun mapError(e: Throwable): ErrorModel {
                TODO("Not yet implemented")
            }

        }
    }
}