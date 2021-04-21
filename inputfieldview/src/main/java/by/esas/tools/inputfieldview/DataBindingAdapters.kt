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
    view.setEndIconMode(if (enabled) 2 else 0)
}

@BindingAdapter("inputEditable")
fun setEditable(view: InputFieldView, editable: Boolean) {
    view.isEditable(editable)
}

@BindingAdapter("inputStartIconIsCheckable")
fun setFirstIconCheckable(view: InputFieldView, checkable: Boolean) {
    if (checkable)
        view.setStartIconMode(InputFieldView.START_ICON_CHECKABLE)
    else
        view.setStartIconMode()
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

@BindingAdapter("inputIsChecked")
fun setChecked(view: InputFieldView, isChecked: Boolean) {
    return view.isChecked(isChecked)
}

@InverseBindingAdapter(attribute = "inputIsChecked")
fun getChecked(view: InputFieldView): Boolean {
    return view.isChecked()
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

@BindingAdapter("inputIsPasswordChecked")
fun setPasswordChecked(view: InputFieldView, isChecked: Boolean) {
    return view.isPasswordToggleChecked(isChecked)
}

@InverseBindingAdapter(attribute = "inputIsPasswordChecked")
fun getPasswordChecked(view: InputFieldView): Boolean {
    return view.isPasswordToggleChecked()
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