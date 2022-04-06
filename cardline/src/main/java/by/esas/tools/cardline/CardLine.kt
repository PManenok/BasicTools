package by.esas.tools.cardline

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textview.MaterialTextView

class CardLine : ConstraintLayout {
    val TAG: String = CardLine::class.java.simpleName
    val titleText: MaterialTextView
    val valueText: MaterialTextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(attrs)
    }

    init {
        val view = inflate(context, R.layout.v_card_line, this)
        titleText = view.findViewById(R.id.v_card_line_title)
        valueText = view.findViewById(R.id.v_card_line_value)
    }

    /*  Initialize attributes from XML file  */
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardLine)
        val title = typedArray.getString(R.styleable.CardLine_cardTitle)
        val value = typedArray.getString(R.styleable.CardLine_cardValue)

        val titleSize =
            typedArray.getDimension(R.styleable.CardLine_cardTitleSize, context.resources.getDimension(R.dimen._11ssp))
        val valueSize =
            typedArray.getDimension(R.styleable.CardLine_cardValueSize, context.resources.getDimension(R.dimen._11ssp))

        val valueColor = typedArray.getColor(R.styleable.CardLine_cardValueColor, valueText.currentTextColor)

        typedArray.recycle()

        titleText.text = title ?: ""
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
        valueText.text = value ?: ""
        valueText.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueSize)
        valueText.setTextColor(valueColor)
    }

    fun setCardTitle(text: String) {
        if (!titleText.text.toString().equals(text)) {
            titleText.text = text
        }
    }

    fun setCardValue(text: String) {
        if (!valueText.text.toString().equals(text)) {
            valueText.text = text
        }
    }

    fun setCardValueColor(color: Int) {
        valueText.setTextColor(color)
    }

    fun setCardValueColorRes(@ColorRes colorRes: Int) {
        valueText.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun getCardTitle(): String {
        return titleText.text.toString()
    }

    fun getCardValue(): String {
        return valueText.text.toString()
    }
}