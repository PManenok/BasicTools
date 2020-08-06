package by.esas.tools.checker.checks

import android.text.TextUtils
import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck

class BiggerCheck<T> : BaseCheck {
    override var TAG: String = BiggerCheck::class.java.simpleName
    private var biggerThen: T? = null

    constructor(biggerThen: T, errorText: String = "Value must be bigger then %d".format(biggerThen)) : super(errorText) {
        this.biggerThen = biggerThen
    }

    constructor(biggerThen: T, @StringRes errorRes: Int, vararg params: Any) : super(errorRes, params) {
        this.biggerThen = biggerThen
    }

    constructor(biggerThen: T, error: Exception) : super(error) {
        this.biggerThen = biggerThen
    }

    constructor(biggerThen: T, status: Enum<*>) : super(status) {
        this.biggerThen = biggerThen
    }

    override fun check(value: Any?): Boolean {
        val text: CharSequence = value as CharSequence
        return if (TextUtils.isEmpty(value)) {
            false
        } else {
            try {
                return when (biggerThen) {
                    is Int -> {
                        val number = text.toString().toInt()
                        number > (biggerThen as Int)
                    }
                    is Float -> {
                        val number = text.toString().toFloat()
                        number > (biggerThen as Float)
                    }
                    is Double -> {
                        val number = text.toString().toDouble()
                        number > (biggerThen as Double)
                    }
                    else -> false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
    }
}