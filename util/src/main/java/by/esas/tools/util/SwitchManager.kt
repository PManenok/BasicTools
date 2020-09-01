package by.esas.tools.util

import android.view.View
import android.widget.EditText

open class SwitchManager {
    open fun enableView(view: View) {
        if (view is EditText) {
            by.esas.tools.util.enableView(view)
        }
    }

    open fun disableView(view: View) {
        if (view is EditText) {
            by.esas.tools.util.disableView(view)
        }
    }
}