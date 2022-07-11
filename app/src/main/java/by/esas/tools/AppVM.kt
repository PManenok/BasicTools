package by.esas.tools

import by.esas.tools.baseui.standard.StandardViewModel
import by.esas.tools.checker.Checker
import by.esas.tools.logger.ErrorModel

abstract class AppVM : StandardViewModel<ErrorModel>() {

    override fun provideCheckListener(): Checker.CheckListener {
        return object : Checker.CheckListener {

        }
    }
}