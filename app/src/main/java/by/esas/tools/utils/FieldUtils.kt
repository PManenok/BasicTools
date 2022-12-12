package by.esas.tools.utils

import android.widget.EditText
import by.esas.tools.inputfieldview.InputFieldView
import by.esas.tools.inputfieldview.dpToPx

fun getDimensInDp(editField: EditText): Int {
    val text = editField.text.toString()
    return if (text.isEmpty()) 0 else dpToPx(text.toInt()).toInt()
}

fun getDimensInDp(inputFieldView: InputFieldView): Int {
    val text = inputFieldView.getText()
    return if (text.isEmpty()) 0 else dpToPx(text.toInt()).toInt()
}

fun InputFieldView.getFloatValue(): Float {
    val text = this.getText()
    return if (text.isEmpty()) 0f else text.toFloat()
}
