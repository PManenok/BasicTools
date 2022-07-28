package by.esas.tools.screens.numpad.numpad_text

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.esas.tools.R
import by.esas.tools.base.AppVM
import javax.inject.Inject

class NumpadTextVM @Inject constructor() : AppVM() {
    private val builder: StringBuilder = StringBuilder()
    val numText = ObservableField<String>()

    private val _iconsTextSizeLive = MutableLiveData<Int>(50)
    val iconsTextSizeLive: LiveData<Int> = _iconsTextSizeLive

    private val _iconsIsDefaultLive = MutableLiveData(true)
    val iconsIsDefaultLive: LiveData<Boolean> = _iconsIsDefaultLive

    val numpadRightIconImage = R.drawable.numpad_backspace
    val numpadLeftIconImage = R.drawable.numpad_cancel

    fun onRestoreClick(){
        disableControls()
        if (builder.isNotEmpty()){
            builder.setLength(builder.length - 1)
            numText.set(builder.toString())
        }
        enableControls()
    }

    fun onIconClick(num: Int){
        disableControls()
        builder.append(num)
        numText.set(builder.toString())
        enableControls()
    }

    fun onCancelClick(){
        disableControls()
        builder.clear()
        numText.set("")
        enableControls()
    }

    fun decreaseIconsSize(){
        _iconsTextSizeLive.value?.let { size ->
            var iconsSize = size
            if (iconsSize != 20){
                iconsSize -= 10
                _iconsTextSizeLive.value = iconsSize
            }
        }
    }

    fun increaseIconsSize(){
        _iconsTextSizeLive.value?.let { size ->
            var iconsSize = size
            if (iconsSize != 80){
                iconsSize += 10
                _iconsTextSizeLive.value = iconsSize
            }
        }
    }

    fun updateIconsStyle(){
        _iconsIsDefaultLive.value = !iconsIsDefaultLive.value!!
    }
}