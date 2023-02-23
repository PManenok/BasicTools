package by.esas.tools.screens.numpad.numpad_text

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.esas.tools.base.AppVM
import by.esas.tools.entity.NumpadTextIconEntity
import javax.inject.Inject

private const val SMALLEST_NUM_TEXT_SIZE = 40
private const val LARGEST_NUM_TEXT_SIZE = 100

class NumpadTextVM @Inject constructor() : AppVM() {

    private val builder: StringBuilder = StringBuilder()
    val numText = ObservableField<String>()

    private val defaultIconsParams = NumpadTextIconEntity(70, 70, 50)
    private val _iconsParamsLive = MutableLiveData<NumpadTextIconEntity>(defaultIconsParams)
    val iconsParamsLive: LiveData<NumpadTextIconEntity> = _iconsParamsLive

    private val _iconsIsDefaultLive = MutableLiveData<Boolean>()
    val iconsIsDefaultLive: LiveData<Boolean> = _iconsIsDefaultLive

    fun onRestoreClick() {
        disableControls()
        if (builder.isNotEmpty()) {
            builder.setLength(builder.length - 1)
            numText.set(builder.toString())
        }
        enableControls()
    }

    fun onIconClick(num: Int) {
        disableControls()
        builder.append(num)
        numText.set(builder.toString())
        enableControls()
    }

    fun onCancelClick() {
        disableControls()
        builder.clear()
        numText.set("")
        enableControls()
    }

    fun decreaseIconsSize() {
        _iconsParamsLive.value?.let { params ->
            if (params.numTextSize != SMALLEST_NUM_TEXT_SIZE) {
                _iconsParamsLive.value = NumpadTextIconEntity(
                    params.iconSize - 10,
                    params.numTextSize - 10,
                    params.imageSize - 10
                )
            }
        }
    }

    fun increaseIconsSize() {
        _iconsParamsLive.value?.let { params ->
            if (params.numTextSize != LARGEST_NUM_TEXT_SIZE) {
                _iconsParamsLive.value = NumpadTextIconEntity(
                    params.iconSize + 10,
                    params.numTextSize + 10,
                    params.imageSize + 10
                )
            }
        }
    }

    fun updateIconsStyle() {
        _iconsIsDefaultLive.value = iconsIsDefaultLive.value.let {
            if (it == null)
                false
            else
                !it
        }
    }
}
