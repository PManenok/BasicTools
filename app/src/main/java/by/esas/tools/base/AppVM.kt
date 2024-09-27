package by.esas.tools.base

import android.widget.Toast
import by.esas.tools.app_domain.error_mapper.AppErrorMapper
import by.esas.tools.baseui.standard.StandardViewModel
import by.esas.tools.logger.IErrorMapper
import by.esas.tools.logger.ILogger
import by.esas.tools.util.TAGk
import by.esas.tools.utils.logger.ErrorModel
import javax.inject.Inject

abstract class AppVM : StandardViewModel<ErrorModel>() {

    @Inject
    lateinit var mapper: AppErrorMapper

    override fun provideMapper(): IErrorMapper<ErrorModel> {
        return mapper
    }

    fun showMessage(text: String, duration: Int = Toast.LENGTH_SHORT) {
        requestAction(MessageAction(text, duration))
    }

    fun showMessage(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        requestAction(MessageAction(resId, duration))
    }
}