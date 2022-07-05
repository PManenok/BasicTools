package by.esas.tools.recycler

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("android:visibility")
internal fun setVisibility(view: View, visibility: Boolean) {
    view.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("visibilityGone")
internal fun setVisibilityGone(view: View, visibility: Boolean) {
    view.visibility = if (visibility) View.VISIBLE else View.GONE
}