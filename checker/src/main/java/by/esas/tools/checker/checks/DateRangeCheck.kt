package by.esas.tools.checker.checks

import android.text.TextUtils
import androidx.annotation.StringRes
import by.esas.tools.checker.BaseCheck
import java.text.SimpleDateFormat
import java.util.*

open class DateRangeCheck : BaseCheck {
    override var TAG: String = CustomCheck::class.java.simpleName

    private var pattern = "yyyy-MM-dd"
    private var formatter: SimpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())

    private var from: Long = Long.MIN_VALUE
    private var to: Long = Long.MAX_VALUE

    constructor(from: Long, to: Long, pattern: String = "yyyy-MM-dd") : super("") {
        this.pattern = pattern
        formatter = SimpleDateFormat(this.pattern, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        errorText = "%s - %s".format(formatter.format(Date(from)), formatter.format(Date(to)))
        this.from = from
        this.to = to
    }

    constructor(from: Long, to: Long, pattern: String = "yyyy-MM-dd", errorMessage: String) : super(errorMessage) {
        this.pattern = pattern
        formatter = SimpleDateFormat(this.pattern, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        this.from = from
        this.to = to
    }

    constructor(from: Long, to: Long, pattern: String = "yyyy-MM-dd", @StringRes errorRes: Int, vararg params: Any) : super(errorRes, params) {
        this.pattern = pattern
        formatter = SimpleDateFormat(this.pattern, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        this.from = from
        this.to = to
    }

    constructor(from: Long, to: Long, pattern: String = "yyyy-MM-dd", error: Exception) : super(error) {
        this.pattern = pattern
        formatter = SimpleDateFormat(this.pattern, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        this.from = from
        this.to = to
    }

    constructor(from: Long, to: Long, pattern: String = "yyyy-MM-dd", status: Enum<*>) : super(status) {
        this.pattern = pattern
        formatter = SimpleDateFormat(this.pattern, Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        this.from = from
        this.to = to
    }

    override fun check(value: Any?): Boolean {
        val text: String = (value as CharSequence?)?.toString() ?: ""

        return if (TextUtils.isEmpty(text)) {
            false
        } else {
            try {
                val date = formatter.parse(text)
                if (date != null) date.time in (from..to)
                else false
            } catch (e: Exception) {
                false
            }
        }
    }
}
