package by.esas.tools.checking

import androidx.lifecycle.MutableLiveData
import by.esas.tools.checker.BaseCheck
import by.esas.tools.checker.Checking
import by.esas.tools.checker.checks.NotEmptyCheck

class ValueChecker(val field: MutableLiveData<String>, val error: MutableLiveData<Throwable?>) : Checking() {
    protected override var checkEmpty: Boolean = true
    protected override var notEmptyRule: NotEmptyCheck? = NotEmptyCheck("Can\'t be empty")

    constructor(field: MutableLiveData<String>, error: MutableLiveData<Throwable?>, checkEmpty: Boolean) : this(field, error) {
        this.checkEmpty = checkEmpty
    }

    override fun checkField(setError: Boolean, errorGetter: BaseCheck.ErrorGetter?): Boolean {
        if (setError)
            error.postValue(null)
        field.value?.let { text ->
            val list = mutableListOf<BaseCheck>()
            if (checkEmpty)
                notEmptyRule?.let { list.add(it) }
            else if (text.isEmpty())
                return true
            else{
                list.addAll(checks)
                list.forEach { rule ->
                    if (!rule.check(text)) {
                        if (setError) error.postValue(rule.getException())
                        return false
                    }
                }}
        }
        return true
    }
}