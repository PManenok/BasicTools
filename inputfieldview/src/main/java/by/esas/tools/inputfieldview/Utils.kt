package by.esas.tools.inputfieldview

import android.content.res.Resources
import android.text.InputType
import android.widget.EditText

fun Double.toFormattedInput(): String {
    val temp = this.toString()
    val dotIndex = temp.indexOf(".")
    return when {
        dotIndex == -1 -> temp//.plus(".00")
        dotIndex == temp.length - 2 -> {
            if (temp.last() == "0".toCharArray()[0])
                temp.substring(0, temp.length - 2)
            else
                temp.plus("0")
            //temp.substring(0, temp.length - 2)
        }
        dotIndex < temp.length - 3 -> {
            temp.substring(0, dotIndex + 3)
        }
        else -> temp
    }
}
fun dpToPx(dp: Int): Float {
    return (dp * Resources.getSystem().displayMetrics.density)
}

fun pxToDp(px: Int): Float {
    return (px / Resources.getSystem().displayMetrics.density)
}
fun isInputTypePassword(editText: EditText?): Boolean {
    return (editText != null
            && (editText.inputType == InputType.TYPE_NUMBER_VARIATION_PASSWORD
            || editText.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            || editText.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD
            || editText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            || editText.inputType == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD))
}