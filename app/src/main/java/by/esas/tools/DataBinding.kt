package by.esas.tools

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import by.esas.tools.entity.TestStatusEnum
import by.esas.tools.topbarview.TopbarView
import com.google.android.material.textview.MaterialTextView

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

@BindingAdapter("caseTestStatus")
fun setInvoiceStatus(view: MaterialTextView, state: TestStatusEnum) {
    val bgRes: Int
    val textColorRes: Int
    val textRes: Int
    when (state) {
        TestStatusEnum.CHECKED -> {
            bgRes = R.drawable.bg_case_status_checked
            textColorRes = R.color.green_dark
            textRes = R.string.case_status_checked
        }
        TestStatusEnum.IN_PROCESS -> {
            bgRes = R.drawable.bg_case_status_in_process
            textColorRes = R.color.blue
            textRes = R.string.case_status_in_process
        }
        TestStatusEnum.FAILED -> {
            bgRes = R.drawable.bg_case_status_failed
            textColorRes = R.color.red
            textRes = R.string.case_status_failed
        }
        else -> {
            bgRes = R.drawable.bg_case_status_unchecked
            textColorRes = R.color.gray_dark
            textRes = R.string.case_status_unchecked
        }
    }
    view.setBackgroundResource(bgRes)
    view.setText(textRes)
    view.setTextColor(ContextCompat.getColor(view.context, textColorRes))
}