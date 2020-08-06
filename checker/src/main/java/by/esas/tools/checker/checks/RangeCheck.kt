package by.esas.tools.checker.checks

import android.text.TextUtils
import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck

class RangeCheck : BaseCheck {
    override var TAG: String = CustomCheck::class.java.simpleName
    private var from: Double = 0.0
    private var to: Double = 1.1

    constructor(from: Double, to: Double, @StringRes errorRes: Int) : super(errorRes) {
        this.from = from
        this.to = to
    }

    constructor(
        from: Double, to: Double,
        errorMessage: String = "Value must be in range from %s to %s".format(from.toString(), to.toString())
    ) : super(errorMessage) {
        this.from = from
        this.to = to
    }

    constructor(from: Double, to: Double, exception: Exception) : super(exception) {
        this.from = from
        this.to = to
    }

    constructor(from: Double, to: Double, status: Enum<*>) : super(status) {
        this.from = from
        this.to = to
    }

    override fun check(value: Any?): Boolean {
        val text: CharSequence = value as CharSequence

        return if (TextUtils.isEmpty(value)) {
            false
        } else {
            try {
                val number = text.toString().toDouble()
                number in (from..to)
            } catch (e: Exception) {
                false
            }
        }
    }
}
