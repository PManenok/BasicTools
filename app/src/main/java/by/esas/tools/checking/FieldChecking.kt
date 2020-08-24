package by.esas.tools.checking

import by.esas.tools.checker.BaseCheck
import by.esas.tools.checker.Checking
import by.esas.tools.checker.checks.NotEmptyCheck
import by.esas.tools.inputfieldview.InputFieldView
import java.lang.ref.WeakReference

class FieldChecking(view: InputFieldView) : Checking() {
    private val field: WeakReference<InputFieldView> = WeakReference(view)
    override var notEmptyRule: NotEmptyCheck? = NotEmptyCheck("Can\'t be empty")
    override var checkEmpty: Boolean = true


    constructor(view: InputFieldView, checkEmpty: Boolean) : this(view) {
        this.checkEmpty = checkEmpty
    }

    override fun checkField(setError: Boolean, errorGetter: BaseCheck.ErrorGetter?): Boolean {
        field.get()?.let { view ->
            if (setError) view.setError(null)

            val text: String = view.getText()

            val list = mutableListOf<BaseCheck>()
            if (checkEmpty)
                notEmptyRule?.let { list.add(it) }
            else if (text.isEmpty()) {
                return true
            }
            list.addAll(checks)
            list.forEach { rule ->
                if (!rule.check(text)) {
                    if (setError) {
                        view.setError(rule.getError(view.context, errorGetter))
                    }
                    return false
                }
            }
        }
        return true
    }
}