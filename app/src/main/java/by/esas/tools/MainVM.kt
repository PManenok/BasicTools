package by.esas.tools

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import by.esas.tools.basedaggerui.simple.SimpleViewModel
import by.esas.tools.error_mapper.AppErrorStatusEnum
import by.esas.tools.logger.BaseErrorModel
import by.esas.tools.logger.ErrorModel
import by.esas.tools.logger.LoggerImpl
import javax.inject.Inject

class MainVM @Inject constructor() : AppVM() {
    override val TAG: String = "MainVM"

    override fun getErrorMessage(error: ErrorModel): String {
        return "Error"
    }

    override fun mapError(e: Throwable): ErrorModel {
        return ErrorModel(0, AppErrorStatusEnum.NOT_SET)
    }

    override fun initLogger() {
        logger = LoggerImpl()
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