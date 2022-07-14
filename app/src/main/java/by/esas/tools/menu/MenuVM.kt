package by.esas.tools.menu

import by.esas.tools.error_mapper.AppErrorMapper
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.simple.AppVM
import javax.inject.Inject

class MenuVM @Inject constructor(val mapper: AppErrorMapper) : AppVM() {
    override fun provideMapper(): IErrorMapper<ErrorModel> {
        return mapper
    }


}