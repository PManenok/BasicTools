package by.esas.tools.screens.numpad

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.esas.tools.R
import by.esas.tools.base.AppVM
import javax.inject.Inject

class NumpadImageVM @Inject constructor(): AppVM() {

    private val builder: StringBuilder = StringBuilder()
    val numText = ObservableField<String>()

    private val _iconsSizeLive = MutableLiveData<Int>(50)
    val iconsSizeLive: LiveData<Int> = _iconsSizeLive

    private val _iconsUpdateLive = MutableLiveData(false)
    val iconsUpdateLive: LiveData<Boolean> = _iconsUpdateLive

    val iconsList = listOf(
        R.drawable.number_0,
        R.drawable.number_1,
        R.drawable.number_2,
        R.drawable.number_3,
        R.drawable.number_4,
        R.drawable.number_5,
        R.drawable.number_6,
        R.drawable.number_7,
        R.drawable.number_8,
        R.drawable.number_9,
    )

    val numpadRightIconImage = R.drawable.numpad_backspace

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

    fun decreaseIconsSize(){
        _iconsSizeLive.value?.let { size ->
            var iconsSize = size
            if (iconsSize != 20){
                iconsSize -= 10
                _iconsSizeLive.value = iconsSize
            }
        }
    }

    fun increaseIconsSize(){
        _iconsSizeLive.value?.let { size ->
            var iconsSize = size
            if (iconsSize != 80){
                iconsSize += 10
                _iconsSizeLive.value = iconsSize
            }
        }
    }

    fun updateIconsImages(){
        _iconsUpdateLive.value = !_iconsUpdateLive.value!!
    }
}