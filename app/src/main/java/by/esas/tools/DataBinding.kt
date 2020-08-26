package by.esas.tools

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import by.esas.tools.fields.RohabInputField

@BindingAdapter("visibilityGone")
fun setVisibilityOrGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("android:textAttrChanged")
fun setListeners(view: RohabInputField, attrChange: InverseBindingListener) {
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

@InverseBindingAdapter(attribute = "android:text")
fun getText(view: RohabInputField): String {
    return view.getText()
}