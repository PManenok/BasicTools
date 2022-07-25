package by.esas.tools

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityGone")
fun setVisibilityOrGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}