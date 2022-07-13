/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.inputfieldview

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

/*############ InputFieldView ############*/

@BindingAdapter("inputEndIconMode")
fun setClearEndIcon(view: InputFieldView, enabled: Boolean) {
    view.setupEndIconMode(if (enabled) 2 else 0)
}

@BindingAdapter("inputEditable")
fun setEditable(view: InputFieldView, editable: Boolean) {
    view.setInputIsEnableableValue(editable)
}

@BindingAdapter("inputStartIconIsCheckable")
fun setFirstIconCheckable(view: InputFieldView, checkable: Boolean) {
    if (checkable)
        view.setupStartIconMode(InputFieldView.START_ICON_CHECKABLE)
    else
        view.setupStartIconMode()
}

@BindingAdapter("android:text")
fun setTextDouble(view: InputFieldView, text: Double) {
    val result = if (text == 0.0) "" else text.toFormattedInput()
    if (view.inputText?.text.toString().toDoubleOrNull() != text || result.toDoubleOrNull() != text) {
        view.setText(result)
    }
}

@InverseBindingAdapter(attribute = "android:text")
fun getTextDouble(view: InputFieldView): Double {
    return view.getText().toDoubleOrNull() ?: 0.0
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(view: InputFieldView): String {
    return view.getText()
}

@BindingAdapter("inputIsStartChecked")
fun setStartChecked(view: InputFieldView, isStartChecked: Boolean) {
    return view.isStartChecked(isStartChecked)
}

@InverseBindingAdapter(attribute = "inputIsStartChecked")
fun getStartChecked(view: InputFieldView): Boolean {
    return view.isStartChecked()
}

@BindingAdapter("inputIsEndChecked")
fun setEndChecked(view: InputFieldView, isStartChecked: Boolean) {
    return view.isEndChecked(isStartChecked)
}

@InverseBindingAdapter(attribute = "inputIsEndChecked")
fun getEndChecked(view: InputFieldView): Boolean {
    return view.isEndChecked()
}

@BindingAdapter("inputIsCheckedAttrChanged")
fun setCheckedListener(view: InputFieldView, attrChange: InverseBindingListener) {
    view.startCheckedListener = object : InputFieldView.IconCheckedListener {
        override fun onCheckChanged(isChanged: Boolean) {
            attrChange.onChange()
        }
    }
    view.endCheckedListener = object : InputFieldView.IconCheckedListener {
        override fun onCheckChanged(isChanged: Boolean) {
            attrChange.onChange()
        }
    }
}

@BindingAdapter("inputIsPasswordCheckedAttrChanged")
fun setPasswordCheckedListener(view: InputFieldView, attrChange: InverseBindingListener) {
    view.passwordCheckedListener = object : InputFieldView.IconCheckedListener {
        override fun onCheckChanged(isChanged: Boolean) {
            attrChange.onChange()
        }
    }
}

@BindingAdapter("android:textAttrChanged")
fun setListeners(view: InputFieldView, attrChange: InverseBindingListener) {
    view.inputText?.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            attrChange.onChange()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

/*############ InputFieldView ############*/