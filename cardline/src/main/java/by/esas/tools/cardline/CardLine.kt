import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Color.red
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import by.esas.tools.cardline.R
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt

inline fun View.setPadding(@Px size: Int) {
    setPadding(size, size, size, size)
}

open class CardLine : LinearLayout {
    val TAG: String = CardLine::class.java.simpleName
    val container: ConstraintLayout
    val titleText: MaterialTextView
    val valueText: MaterialTextView
    val topDividerView: View
    val bottomDividerView: View
    val startIcon: AppCompatImageView
    val endIcon: AppCompatImageView

    constructor(context: Context) : super(context)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
        container = view.findViewById(R.id.v_card_line_container)
        titleText = view.findViewById(R.id.v_card_line_title)
        valueText = view.findViewById(R.id.v_card_line_value)
        startIcon = view.findViewById(R.id.v_card_line_icon_start)
        endIcon = view.findViewById(R.id.v_card_line_icon_end)
        topDividerView = view.findViewById(R.id.v_card_line_divider_top)
        bottomDividerView = view.findViewById(R.id.v_card_line_divider_bottom)
    }

    /*  Initialize attributes from XML file  */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun initAttrs(attrs: AttributeSet?) {
        val defDividerHeight = dpToPx(1)
        val defDividerColor = Color.parseColor("#D9E1EE")
        val defIconColor = Color.parseColor("#FF0000")
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardLine)

        val startIconAlignTop: Boolean = typedArray.getBoolean(R.styleable.CardLine_cardStartIconAlignTop, false)
        val endIconAlignTop: Boolean = typedArray.getBoolean(R.styleable.CardLine_cardEndIconAlignTop, false)
        val textAlignTop: Boolean = typedArray.getBoolean(R.styleable.CardLine_cardTextAlignTop, false)

        val title = typedArray.getString(R.styleable.CardLine_cardTitle)
        val titleStyleId: Int = typedArray.getResourceId(R.styleable.CardLine_cardTitleTextAppearance, -1)
        val titleSingleLine = typedArray.getBoolean(R.styleable.CardLine_cardTitleSingleLine, false)
        val titleWidthPercent = typedArray.getFloat(R.styleable.CardLine_cardTitleWidthPercent, 0.35f)

        val value = typedArray.getString(R.styleable.CardLine_cardValue)
        val valueAlignment = typedArray.getInt(R.styleable.CardLine_cardValueAlignment, 0)
        val valueStyleId: Int = typedArray.getResourceId(R.styleable.CardLine_cardValueTextAppearance, -1)
        val valueSingleLine = typedArray.getBoolean(R.styleable.CardLine_cardValueSingleLine, false)

        val topPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardContainerPaddingTop, 0)
        val bottomPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardContainerPaddingBottom, 0)
        val startPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardContainerPaddingStart, 0)
        val endPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardContainerPaddingEnd, 0)
        val textStartPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardTextPaddingStart, 0)
        val textBetweenPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardTextPaddingBetween, 0)
        val textEndPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardTextPaddingEnd, 0)
        val iconStartPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconPaddingStart, 0)
        val iconEndPadding = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconPaddingEnd, 0)

        /*##########  Start Icon  ##########*/
        val startIconSize = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconStartSize, 0)
        val startDrawableRes: Int = typedArray.getResourceId(R.styleable.CardLine_cardIconStart, -1)
        val startTint = typedArray.getColor(R.styleable.CardLine_cardIconStartTint, defIconColor)

        /*##########  End Icon  ##########*/
        val endIconSize = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconEndSize, 0)
        val endDrawableRes: Int = typedArray.getResourceId(R.styleable.CardLine_cardIconEnd, -1)
        val endTint = typedArray.getColor(R.styleable.CardLine_cardIconEndTint, defIconColor)

        val showTopDiv = typedArray.getBoolean(R.styleable.CardLine_cardShowDividerTop, false)
        val topDivHeight = typedArray.getDimension(R.styleable.CardLine_cardTopDividerHeight, defDividerHeight)
        val topDividerColor = typedArray.getColor(R.styleable.CardLine_cardTopDividerColor, defDividerColor)
        val showBottomDiv = typedArray.getBoolean(R.styleable.CardLine_cardShowDividerBottom, false)
        val bottomDivHeight = typedArray.getDimension(R.styleable.CardLine_cardBottomDividerHeight, defDividerHeight)
        val bottomDividerColor = typedArray.getColor(R.styleable.CardLine_cardBottomDividerColor, defDividerColor)

        typedArray.recycle()

        setAlignTop(startIconAlignTop, listOf(startIcon))
        setAlignTop(endIconAlignTop, listOf(endIcon))
        setTextAlignTop(textAlignTop)

        container.setPadding(
            paddingLeft + startPadding,
            paddingTop + topPadding,
            paddingRight + endPadding,
            paddingBottom + bottomPadding
        )

        titleText.apply {
            text = title ?: ""
            isSingleLine = titleSingleLine
            if (titleStyleId != -1)
                TextViewCompat.setTextAppearance(this, titleStyleId)
            setPadding(paddingLeft + textStartPadding, paddingTop, paddingRight + textBetweenPadding, paddingBottom)

            (layoutParams as ConstraintLayout.LayoutParams).apply {
                if (titleWidthPercent == 0f) {
                    width = LayoutParams.WRAP_CONTENT
                    matchConstraintPercentWidth = 1f
                } else {
                    matchConstraintPercentWidth = titleWidthPercent
                }
            }
        }

        valueText.apply {
            text = value ?: ""
            isSingleLine = valueSingleLine
            if (valueStyleId != -1)
                TextViewCompat.setTextAppearance(this, valueStyleId)
            textAlignment = valueAlignment
            setPadding(paddingLeft, paddingTop, paddingRight + textEndPadding, paddingBottom)
        }

        startIcon.apply {
            if (startDrawableRes != -1) {
                updateStartIconSize(startIconSize)
                setImageResource(startDrawableRes)
                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(startTint))
                setPadding(iconStartPadding)
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }
        endIcon.apply {
            if (endDrawableRes != -1) {
                updateEndIconSize(endIconSize)
                setImageResource(endDrawableRes)
                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(endTint))
                setPadding(iconEndPadding)
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }

        topDividerView.visibility = if (showTopDiv) View.VISIBLE else View.GONE
        topDividerView.setBackgroundColor(topDividerColor)
        val topDivParams = topDividerView.layoutParams as LayoutParams
        topDivParams.height = topDivHeight.toInt()
        topDividerView.layoutParams = topDivParams

        bottomDividerView.visibility = if (showBottomDiv) View.VISIBLE else View.GONE
        bottomDividerView.setBackgroundColor(bottomDividerColor)
        val bottomDivParams = bottomDividerView.layoutParams as LayoutParams
        bottomDivParams.height = bottomDivHeight.toInt()
        bottomDividerView.layoutParams = bottomDivParams
    }

    open fun updateStartIconSize(size: Int) {
        updateIconSize(size, startIcon)
    }

    open fun updateEndIconSize(size: Int) {
        updateIconSize(size, endIcon)
    }

    protected open fun updateIconSize(iconSize: Int, icon: AppCompatImageView) {
        val size: Int = if (iconSize == 0) dpToPx(32).roundToInt() else iconSize
        val params = icon.layoutParams
        if (params != null && (params.width != size || params.height != size)) {
            params.width = size
            params.height = size
            icon.layoutParams = params
        }
    }

    open fun setTextAlignTop(alignTop: Boolean) {
        setAlignTop(alignTop, listOf(titleText, valueText))
    }

    open fun setAllAlignTop(alignTop: Boolean) {
        setAlignTop(alignTop, listOf(startIcon, titleText, valueText, endIcon))
    }

    open fun setAlignTop(alignTop: Boolean, views: List<View>) {
        val bias = if (alignTop) 0f else 0.5f
        views.forEach { view ->
            (view.layoutParams as ConstraintLayout.LayoutParams).apply { verticalBias = bias }
        }
    }

    open fun setCardTitle(text: String) {
        if (!titleText.text.toString().equals(text)) {
            titleText.text = text
        }
    }

    open fun getCardTitle(): String {
        return titleText.text.toString()
    }

    open fun setCardValue(text: String) {
        if (!valueText.text.toString().equals(text)) {
            valueText.text = text
        }
    }

    open fun getCardValue(): String {
        return valueText.text.toString()
    }

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}