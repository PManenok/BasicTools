package by.esas.tools.checker

import com.google.android.material.textfield.TextInputLayout
import java.lang.ref.WeakReference

class BaseChecking(view: TextInputLayout) : Checking() {
    private val field: WeakReference<TextInputLayout> = WeakReference(view)

    constructor(view: TextInputLayout, checkEmpty: Boolean) : this(view) {
        this.checkEmpty = checkEmpty
    }

    override fun checkField(setError: Boolean, errorGetter: BaseCheck.ErrorGetter?): Boolean {
        field.get()?.let { view ->
            if (setError) view.error = null

            val text: String = view.editText?.text.toString() ?: ""

            val list = mutableListOf<BaseCheck>()
            if (checkEmpty && notEmptyRule != null)
                list.add(notEmptyRule!!)
            else if (text.isEmpty()) {
                return true
            }
            list.addAll(checks)
            list.forEach { rule ->
                if (!rule.check(text)) {
                    if (setError) {
                        view.error = rule.getError(view.context, errorGetter)
                    }
                    return false
                }
            }
        }
        return true
    }
}