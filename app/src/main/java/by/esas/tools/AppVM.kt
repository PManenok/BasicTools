package by.esas.tools

import by.esas.tools.basedaggerui.simple.SimpleViewModel
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.logger.ErrorModel

abstract class AppVM : SimpleViewModel<AppErrorStatusEnum, ErrorModel>() {

}