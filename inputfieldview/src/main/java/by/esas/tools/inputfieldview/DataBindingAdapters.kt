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
    view.setEditable(editable)
}

@BindingAdapter("inputStartIconIsCheckable")
fun setFirstIconCheckable(view: InputFieldView, checkable: Boolean) {
    view.setFirstIconCheckable(checkable)
}

@BindingAdapter("android:text")
fun setTextDouble(view: InputFieldView, text: Double) {
    val result = if (text == 0.0) "" else text.toFormattedInput()
    if (view.inputText.text.toString().toDoubleOrNull() != text || result.toDoubleOrNull() != text) {
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
    return view.setChecked(isChecked)
}

@InverseBindingAdapter(attribute = "inputIsChecked")
fun getChecked(view: InputFieldView): Boolean {
    return view.isChecked()
}

@BindingAdapter("inputIsCheckedAttrChanged")
fun setCheckedListener(view: InputFieldView, attrChange: InverseBindingListener) {
    view.setOnCheckedListener(object : InputFieldView.StartIconCheckedListener {
        override fun onCheckChanged(isChanged: Boolean) {
            attrChange.onChange()
        }
    })
}

@BindingAdapter("android:textAttrChanged")
fun setListeners(view: InputFieldView, attrChange: InverseBindingListener) {
    view.inputText.addTextChangedListener(object : TextWatcher {
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


/*############ SpinnerField ###########*/

/*@BindingAdapter("spinnerEditable")
fun setEditable(view: SpinnerFieldView, editable: Boolean) {
    view.setEditable(editable)
}*/

@InverseBindingAdapter(attribute = "android:text")
fun getText(view: SpinnerFieldView): String {
    return view.getText()
}

@BindingAdapter("android:textAttrChanged")
fun setListeners(view: SpinnerFieldView, attrChange: InverseBindingListener) {
    view.inputText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            attrChange.onChange()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}
/*############ SpinnerField ###########*/