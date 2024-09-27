/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.cardline

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat.setImageTintList
import androidx.core.widget.TextViewCompat
import by.esas.tools.util_ui.SwitchManager
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt

open class CardLine : LinearLayout, SwitchManager.ISwitchView {

    val TAG: String = CardLine::class.java.simpleName
    protected val container: ConstraintLayout
    protected val titleText: MaterialTextView
    protected val valueText: MaterialTextView
    protected val topDividerView: View
    protected val bottomDividerView: View
    protected val startIcon: AppCompatImageView
    protected val endIcon: AppCompatImageView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

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
        orientation = VERTICAL
    }

    /*region ####################### ISwitchView interface ############################*/
    override fun switchOn() {
        super.setEnabled(true)
    }

    override fun switchOff() {
        super.setEnabled(false)
    }
    /*endregion ####################### ISwitchView interface ############################*/

    protected val defDividerHeight = dpToPx(1)
    protected val defDividerColor = Color.parseColor("#D9E1EE")
    protected val defIconColor = Color.parseColor("#D9E1EE")
    protected val defStartIconAlignVertical = 0.5f
    protected val defEndIconAlignVertical = 0.5f
    protected val defTextAlignVertical = 0.5f
    protected val defShowTopDiv = false
    protected val defShowBottomDiv = false
    protected val defPadding = 0
    protected val defTitleWidthPercent = 0.35f
    protected val defSingleLine = false
    protected val defValueAlignment = 0

    protected var startIconVerticalBias = 0.5f
    protected var endIconVerticalBias = 0.5f
    protected var textAlignVertical = 0.5f
    protected var showTopDiv = false
    protected var showBottomDiv = false
    protected var titleWidthPercent = 0.35f
    protected var valueAlignment = 0

    /*  Initialize attributes from XML file  */
    protected fun initAttrs(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardLine)

        startIconVerticalBias =
            typedArray.getFloat(R.styleable.CardLine_cardStartIconAlignVertical, defStartIconAlignVertical)
        endIconVerticalBias =
            typedArray.getFloat(R.styleable.CardLine_cardEndIconAlignVertical, defEndIconAlignVertical)
        textAlignVertical = typedArray.getFloat(R.styleable.CardLine_cardTextAlignVertical, defTextAlignVertical)

        val title = typedArray.getString(R.styleable.CardLine_cardTitle)
        val titleStyleId: Int =
            typedArray.getResourceId(R.styleable.CardLine_cardTitleTextAppearance, -1)
        val titleSingleLine = typedArray.getBoolean(R.styleable.CardLine_cardTitleSingleLine, defSingleLine)
        titleWidthPercent =
            typedArray.getFloat(R.styleable.CardLine_cardTitleWidthPercent, defTitleWidthPercent)

        val value = typedArray.getString(R.styleable.CardLine_cardValue)
        valueAlignment = typedArray.getInt(R.styleable.CardLine_cardValueAlignment, defValueAlignment)
        val valueStyleId: Int =
            typedArray.getResourceId(R.styleable.CardLine_cardValueTextAppearance, -1)
        val valueSingleLine = typedArray.getBoolean(R.styleable.CardLine_cardValueSingleLine, defSingleLine)

        val topPadding = typedArray.getDimensionPixelSize(
            R.styleable.CardLine_cardContainerPaddingTop,
            defPadding
        )
        val bottomPadding = typedArray.getDimensionPixelSize(
            R.styleable.CardLine_cardContainerPaddingBottom,
            defPadding
        )
        val startPadding = typedArray.getDimensionPixelSize(
            R.styleable.CardLine_cardContainerPaddingStart,
            defPadding
        )
        val endPadding = typedArray.getDimensionPixelSize(
            R.styleable.CardLine_cardContainerPaddingEnd,
            defPadding
        )
        val textStartPadding = typedArray.getDimensionPixelSize(
            R.styleable.CardLine_cardTextPaddingStart,
            defPadding
        )
        val textBetweenPadding = typedArray.getDimensionPixelSize(
            R.styleable.CardLine_cardTextPaddingBetween,
            defPadding
        )
        val textEndPadding = typedArray.getDimensionPixelSize(
            R.styleable.CardLine_cardTextPaddingEnd,
            defPadding
        )
        val iconStartPadding =
            typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconPaddingStart, 0)
        val iconEndPadding =
            typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconPaddingEnd, 0)

        /*##########  Start Icon  ##########*/
        val startIconSize =
            typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconStartSize, 0)
        val startDrawableRes: Int = typedArray.getResourceId(R.styleable.CardLine_cardIconStart, -1)
        val startTint = typedArray.getColor(R.styleable.CardLine_cardIconStartTint, defIconColor)

        /*##########  End Icon  ##########*/
        val endIconSize = typedArray.getDimensionPixelSize(R.styleable.CardLine_cardIconEndSize, 0)
        val endDrawableRes: Int = typedArray.getResourceId(R.styleable.CardLine_cardIconEnd, -1)
        val endTint = typedArray.getColor(R.styleable.CardLine_cardIconEndTint, defIconColor)

        showTopDiv = typedArray.getBoolean(R.styleable.CardLine_cardShowDividerTop, defShowTopDiv)
        val topDivHeight =
            typedArray.getDimension(R.styleable.CardLine_cardTopDividerHeight, defDividerHeight)
        val topDividerColor =
            typedArray.getColor(R.styleable.CardLine_cardTopDividerColor, defDividerColor)
        showBottomDiv =
            typedArray.getBoolean(R.styleable.CardLine_cardShowDividerBottom, defShowBottomDiv)
        val bottomDivHeight =
            typedArray.getDimension(R.styleable.CardLine_cardBottomDividerHeight, defDividerHeight)
        val bottomDividerColor =
            typedArray.getColor(R.styleable.CardLine_cardBottomDividerColor, defDividerColor)

        typedArray.recycle()

        setAlignVertical(startIconVerticalBias, listOf(startIcon))
        setAlignVertical(endIconVerticalBias, listOf(endIcon))
        setupTextAlignVertical(textAlignVertical)

        setContainerPaddings(startPadding, topPadding, endPadding, bottomPadding)

        titleText.apply {
            text = title ?: ""
            isSingleLine = titleSingleLine
            if (titleStyleId != -1)
                TextViewCompat.setTextAppearance(this, titleStyleId)
            setPadding(
                paddingLeft + textStartPadding,
                paddingTop,
                paddingRight + textBetweenPadding,
                paddingBottom
            )
        }

        setupTitleWidthPercent(titleWidthPercent)

        valueText.apply {
            text = value ?: ""
            isSingleLine = valueSingleLine
            if (valueStyleId != -1)
                TextViewCompat.setTextAppearance(this, valueStyleId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textAlignment = valueAlignment
            }
            setPadding(paddingLeft, paddingTop, paddingRight + textEndPadding, paddingBottom)
        }

        startIcon.apply {
            if (startDrawableRes != -1) {
                setStartIconSize(startIconSize)
                setImageResource(startDrawableRes)
                setImageTintList(this, ColorStateList.valueOf(startTint))
                setPadding(iconStartPadding, iconStartPadding, iconStartPadding, iconStartPadding)
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }

        endIcon.apply {
            if (endDrawableRes != -1) {
                setEndIconSize(endIconSize)
                setImageResource(endDrawableRes)
                setImageTintList(this, ColorStateList.valueOf(endTint))
                setPadding(iconEndPadding, iconStartPadding, iconStartPadding, iconStartPadding)
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }

        setTopDividerVisibility(showTopDiv)
        setTopDividerParams(topDividerColor, topDivHeight.toInt())

        setBottomDividerVisibility(showBottomDiv)
        setBottomDividerParams(bottomDividerColor, bottomDivHeight.toInt())
    }

    /*region ####################### Card Text ############################*/

    open fun setupTextAlignVertical(bias: Float) {
        setAlignVertical(bias, listOf(titleText, valueText))
    }

    /* ####################### Card Title ############################*/

    open fun setCardTitle(text: String) {
        if (titleText.text.toString() != text) {
            titleText.text = text
        }
    }

    open fun setCardTitle(textRes: Int) {
        setCardTitle(Resources.getSystem().getString(textRes))
    }

    open fun getCardTitle(): String {
        return titleText.text.toString()
    }

    open fun setCardTitleStyle(styleId: Int) {
        if (styleId != -1)
            TextViewCompat.setTextAppearance(titleText, styleId)
    }

    open fun isTitleSingleLine(value: Boolean) {
        titleText.isSingleLine = value
    }

    open fun setupTitleWidthPercent(value: Float) {
        titleWidthPercent = value
        titleText.apply {
            (layoutParams as ConstraintLayout.LayoutParams).apply {
                if (value == 0f) {
                    width = LayoutParams.WRAP_CONTENT
                    matchConstraintPercentWidth = 1f
                } else {
                    matchConstraintPercentWidth = value
                }
            }
        }
    }

    /* ####################### Card Value ############################*/

    open fun setCardValue(text: String) {
        if (valueText.text.toString() != text) {
            valueText.text = text
        }
    }

    open fun setCardValue(textRes: Int) {
        setCardValue(Resources.getSystem().getString(textRes))
    }

    open fun setCardValueStyle(styleId: Int) {
        if (styleId != -1)
            TextViewCompat.setTextAppearance(valueText, styleId)
    }

    open fun getCardValue(): String {
        return valueText.text.toString()
    }

    open fun isValueSingleLine(value: Boolean) {
        valueText.isSingleLine = value
    }

    open fun setCardValueColor(color: Int) {
        valueText.setTextColor(color)
    }

    open fun setCardValueColorRes(@ColorRes colorRes: Int) {
        setCardValueColor(ContextCompat.getColor(context, colorRes))
    }

    /*endregion ####################### Text ############################*/

    /*region ############################ Icons ################################*/

    /*####################### Start Icon ############################*/

    open fun setStartIcon(resId: Int) {
        startIcon.setImageResource(resId)
    }

    open fun setStartIcon(drawable: Drawable) {
        startIcon.setImageDrawable(drawable)
    }

    open fun setStartIconColor(color: Int) {
        setImageTintList(startIcon, ColorStateList.valueOf(color))
    }

    open fun setStartIconColorRes(@ColorRes colorRes: Int) {
        setStartIconColor(ContextCompat.getColor(context, colorRes))
    }

    open fun setStartIconVisibility(value: Boolean) {
        if (value) {
            startIcon.visibility = View.VISIBLE
        } else {
            startIcon.visibility = View.GONE
        }
    }

    open fun setStartIconSize(size: Int) {
        updateIconSize(size, startIcon)
    }

    open fun setStartIconAlignVertical(bias: Float) {
        startIconVerticalBias = bias
        setAlignVertical(bias, listOf(startIcon))
    }

    open fun setStartIconPadding(leftPadding: Int, topPadding: Int, rightPadding: Int, bottomPadding: Int) {
        startIcon.setPadding(
            leftPadding,
            topPadding,
            rightPadding,
            bottomPadding
        )
    }

    /*####################### End Icon ############################*/

    open fun setEndIcon(resId: Int) {
        endIcon.setImageResource(resId)
    }

    open fun setEndIcon(drawable: Drawable) {
        endIcon.setImageDrawable(drawable)
    }

    open fun setEndIconVisibility(value: Boolean) {
        if (value) {
            endIcon.visibility = View.VISIBLE
        } else {
            endIcon.visibility = View.GONE
        }
    }

    open fun setEndIconSize(size: Int) {
        updateIconSize(size, endIcon)
    }

    open fun setEndIconColor(color: Int) {
        setImageTintList(endIcon, ColorStateList.valueOf(color))
    }

    open fun setEndIconColorRes(@ColorRes colorRes: Int) {
        setEndIconColor(ContextCompat.getColor(context, colorRes))
    }

    open fun setEndIconAlignVertical(bias: Float) {
        endIconVerticalBias = bias
        setAlignVertical(bias, listOf(endIcon))
    }

    open fun setEndIconPadding(leftPadding: Int, topPadding: Int, rightPadding: Int, bottomPadding: Int) {
        endIcon.setPadding(
            leftPadding,
            topPadding,
            rightPadding,
            bottomPadding
        )
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

    /*endregion ############################ Icons ################################*/

    /*region ############################ Dividers ################################*/

    /*######################## Top divider ############################*/

    open fun setTopDividerVisibility(value: Boolean) {
        showTopDiv = value
        topDividerView.visibility = if (showTopDiv) View.VISIBLE else View.GONE
    }

    open fun setTopDividerColor(color: Int) {
        topDividerView.setBackgroundColor(color)
    }

    open fun setTopDividerColorRes(@ColorRes colorRes: Int) {
        setTopDividerColor(ContextCompat.getColor(context, colorRes))
    }

    open fun setTopDividerHeight(value: Int) {
        val topDivParams = topDividerView.layoutParams as LayoutParams
        topDivParams.height = value
        topDividerView.layoutParams = topDivParams
    }

    open fun setTopDividerParams(color: Int, height: Int) {
        setTopDividerColor(color)
        setTopDividerHeight(height)
    }

    /*######################## Bottom divider ############################*/

    open fun setBottomDividerVisibility(value: Boolean) {
        showBottomDiv = value
        bottomDividerView.visibility = if (value) View.VISIBLE else View.GONE
    }

    open fun setBottomDividerColor(color: Int) {
        bottomDividerView.setBackgroundColor(color)
    }

    open fun setBottomDividerColorRes(@ColorRes colorRes: Int) {
        setBottomDividerColor(ContextCompat.getColor(context, colorRes))
    }

    open fun setBottomDividerHeight(value: Int) {
        val bottomDivParams = bottomDividerView.layoutParams as LayoutParams
        bottomDivParams.height = value
        bottomDividerView.layoutParams = bottomDivParams
    }

    open fun setBottomDividerParams(color: Int, height: Int) {
        setBottomDividerColor(color)
        setBottomDividerHeight(height)
    }
    /*endregion ############################ Dividers ################################*/

    /*region ############################ Other ################################*/
    fun setDefaultValues() {
        startIconVerticalBias = defStartIconAlignVertical
        endIconVerticalBias = defEndIconAlignVertical
        textAlignVertical = defTextAlignVertical
        valueAlignment = defValueAlignment

        setupTitleWidthPercent(defTitleWidthPercent)

        setAlignVertical(defStartIconAlignVertical, listOf(startIcon))
        setAlignVertical(defEndIconAlignVertical, listOf(endIcon))
        setupTextAlignVertical(defTextAlignVertical)

        setContainerPaddings(
            defPadding,
            defPadding,
            defPadding,
            defPadding
        )

        setCardTitle("")
        titleText.apply {
            isSingleLine = defSingleLine
            setPadding(
                paddingLeft + defPadding,
                paddingTop,
                paddingRight + defPadding,
                paddingBottom
            )
        }

        setCardValue("")
        valueText.apply {
            isSingleLine = defSingleLine
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textAlignment = defValueAlignment
            }
            setPadding(paddingLeft, paddingTop, paddingRight + defPadding, paddingBottom)
        }

        setStartIconVisibility(false)
        setEndIconVisibility(false)

        setTopDividerVisibility(defShowTopDiv)
        setTopDividerParams(defDividerColor, defDividerHeight.toInt())

        setBottomDividerVisibility(showBottomDiv)
        setBottomDividerParams(defDividerColor, defDividerHeight.toInt())
    }

    open fun setContainerPaddings(
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        container.setPadding(
            paddingLeft + leftPadding,
            paddingTop + topPadding,
            paddingRight + rightPadding,
            paddingBottom + bottomPadding
        )
    }

    open fun setAllAlignVertical(bias: Float) {
        setAlignVertical(bias, listOf(startIcon, titleText, valueText, endIcon))
    }

    protected open fun setAlignVertical(bias: Float, views: List<View>) {
        views.forEach { view ->
            (view.layoutParams as ConstraintLayout.LayoutParams).apply { verticalBias = bias }
        }
    }

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
    /*endregion ############################ Other ################################*/
}
