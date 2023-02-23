package by.esas.tools.screens.numpad

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.esas.tools.R
import by.esas.tools.base.AppVM
import javax.inject.Inject

private const val SMALLEST_ICON_SIZE = 20
private const val LARGEST_ICON_SIZE = 80

class NumpadImageVM @Inject constructor() : AppVM() {

    private val builder: StringBuilder = StringBuilder()
    val numText = ObservableField<String>()

    private val _iconsSizeLive = MutableLiveData<Int>(50)
    val iconsSizeLive: LiveData<Int> = _iconsSizeLive

    private val _iconsIsDefaultLive = MutableLiveData(true)
    val iconsIsDefaultLive: LiveData<Boolean> = _iconsIsDefaultLive

    val numpadIconsList = listOf(
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
    val numpadLeftIconImage = R.drawable.numpad_cancel

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
        _iconsSizeLive.value?.let { size ->
            var iconsSize = size
            if (iconsSize != SMALLEST_ICON_SIZE) {
                iconsSize -= 10
                _iconsSizeLive.value = iconsSize
            }
        }
    }

    fun increaseIconsSize() {
        _iconsSizeLive.value?.let { size ->
            var iconsSize = size
            if (iconsSize != LARGEST_ICON_SIZE) {
                iconsSize += 10
                _iconsSizeLive.value = iconsSize
            }
        }
    }

    fun updateIconsImages() {
        _iconsIsDefaultLive.value = !iconsIsDefaultLive.value!!
    }
}
