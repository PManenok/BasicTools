package by.esas.tools.utils

import android.widget.EditText
import by.esas.tools.inputfieldview.dpToPx

fun getDimensInDp(editField: EditText): Int {
    val text = editField.text.toString()
    return if (text.isEmpty()) 0 else dpToPx(text.toInt()).toInt()
}

fun EditText.getFloatValue(): Float {
    val text = this.text.toString()
    return if (text.isEmpty()) 0f else text.toFloat()
}
