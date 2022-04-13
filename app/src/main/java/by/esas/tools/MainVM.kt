package by.esas.tools

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import by.esas.tools.dialog.GetPasswordDialog
import javax.inject.Inject

class MainVM @Inject constructor() : AppVM() {
    override val TAG: String = "MainVM"
    var update: () -> Unit = {}

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
    val showAdditional = ObservableBoolean(false)
    val mainText = MutableLiveData<String>().apply { postValue("") }

    val password = MutableLiveData<String>("")

    fun onAdditionalClick() {
        showAdditional.set(!showAdditional.get())
        update()
    }

    fun onPhoneCodeClick() {
        showDialog(GetPasswordDialog())
    }
}

