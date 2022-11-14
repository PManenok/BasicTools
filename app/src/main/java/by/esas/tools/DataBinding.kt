package by.esas.tools

import android.view.View
import androidx.databinding.BindingAdapter
import by.esas.tools.topbarview.TopbarView

@BindingAdapter("visibilityGone")
fun setVisibilityOrGone(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}


@BindingAdapter("barTitleText")
fun setBarTitle(view: TopbarView, text: String?) {
    view.setTitle(text)
}

@BindingAdapter("buttonsClickable")
fun setButtonsEnabled(view: TopbarView, value: Boolean) {
    view.setButtonsEnabled(value)
}

@BindingAdapter("onEndActionClick")
fun setBarEndActionClick(view: TopbarView, listener: View.OnClickListener) {
    view.setEndActionListener(listener)
}

@BindingAdapter("onStartActionClick")
fun setBarStartActionClick(view: TopbarView, listener: View.OnClickListener) {
    view.setStartActionListener(listener)
}

@BindingAdapter("barShowNavIcon")
fun setBarNavIconVisibility(view: TopbarView, value: Boolean) {
    view.setNavIconVisibility(value)
}

@BindingAdapter("barShowActionIcon")
fun setBarActionIconVisibility(view: TopbarView, value: Boolean) {
    view.setActionIconVisibility(value)
}

@BindingAdapter("barShowDivider")
fun setBarDividerVisibility(view: TopbarView, value: Boolean) {
    view.setDividerVisibility(value)
}