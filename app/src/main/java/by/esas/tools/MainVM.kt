package by.esas.tools

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import by.esas.tools.basedaggerui.simple.SimpleViewModel
import by.esas.tools.domain.mapper.ErrorModel
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.logger.ILogger
import by.esas.tools.logger.LoggerImpl
import javax.inject.Inject

class MainVM @Inject constructor() : SimpleViewModel<AppErrorStatusEnum>() {
    override val TAG: String = "MainVM"
    override var logger: ILogger<AppErrorStatusEnum> = LoggerImpl()

    override fun getErrorMessage(error: ErrorModel<AppErrorStatusEnum>): String {
        return "Error"
    }

    override fun mapError(e: Throwable): ErrorModel<AppErrorStatusEnum> {
        return ErrorModel(0, AppErrorStatusEnum.NOT_SET)
    }

    override fun initLogger() {
        logger.setTag(TAG)
    }

    override fun onChangeLanguage() {

    }

    fun setUp() {
        serviceName.addOnPropertyChangedCallback(
            object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    mainText.postValue(serviceName.get() ?: "")
                }
            }
        )
    }

    val serviceName = ObservableField<String>("")
    val mainText = MutableLiveData<String>().apply { postValue("") }
}