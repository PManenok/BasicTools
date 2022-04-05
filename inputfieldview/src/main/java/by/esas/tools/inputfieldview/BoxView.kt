/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.inputfieldview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

open class BoxView : View {
    private val path = Path()
    private val rectF = RectF()
    private val paint = Paint()

    protected open val defaultStrokeColor: Int = Color.BLACK
    protected open val defaultBgColor: Int = Color.TRANSPARENT
    protected open val defaultStrokeWidth: Int = dpToPx(1).toInt()
    protected open val defaultStrokeRadius: Int = dpToPx(0).toInt()
    protected open val defaultStrokeStartPadding: Int = dpToPx(0).toInt()
    protected open val defaultTopClip: Int = dpToPx(0).toInt()

    protected var paintColor: Int = Color.BLACK
    protected var paintBGColor: Int = Color.TRANSPARENT
    protected var strokeWidth: Float = dpToPx(1)
    protected var strokeRadius: Float = dpToPx(0)
    protected var startStrokePadding: Float = dpToPx(0)
    protected var topClip: Float = dpToPx(0)

    constructor(context: Context?) : super(context) {
        initialSetting()
        setDefaults()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialSetting()
        initAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialSetting()
        initAttrs(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initialSetting()
        initAttrs(attrs)
    }

    fun initialSetting() {
        paint.isAntiAlias = true
    }

    fun setDefaults() {
        paintColor = defaultStrokeColor
        paintBGColor = defaultBgColor
        strokeWidth = defaultStrokeWidth.toFloat()
        strokeRadius = defaultStrokeRadius.toFloat()
        startStrokePadding = defaultStrokeStartPadding.toFloat()
        topClip = defaultTopClip.toFloat()
    }

    /*  Initialize attributes from XML file  */
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BoxView)

        paintColor = typedArray.getColor(R.styleable.BoxView_boxVStrokeColor, defaultStrokeColor)
        paintBGColor = typedArray.getColor(R.styleable.BoxView_boxVBgColor, defaultBgColor)
        strokeWidth = typedArray.getDimensionPixelOffset(R.styleable.BoxView_boxVStrokeWidth, defaultStrokeWidth).toFloat()
        strokeRadius = typedArray.getDimensionPixelOffset(R.styleable.BoxView_boxVStrokeRadius, defaultStrokeRadius).toFloat()
        startStrokePadding = typedArray.getDimensionPixelOffset(R.styleable.BoxView_boxVStartPadding, defaultStrokeStartPadding).toFloat()
        topClip = typedArray.getDimensionPixelOffset(R.styleable.BoxView_boxVTopClip, defaultTopClip).toFloat()

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val halfStroke = strokeWidth / 2f
        //Draw Background
        paint.strokeWidth = strokeWidth
        paint.color = paintBGColor
        paint.style = Paint.Style.FILL
        rectF.set(halfStroke, halfStroke, (width - strokeWidth), (height - strokeWidth))
        canvas?.drawRoundRect(rectF, strokeRadius, strokeRadius, paint)
        //Draw Stroke
        paint.strokeWidth = strokeWidth
        paint.color = paintColor
        paint.style = Paint.Style.STROKE
        countStrokePath(halfStroke, halfStroke, (width - strokeWidth), (height - strokeWidth))
        canvas?.drawPath(path, paint)
    }

    protected open fun countStrokePath(left: Float, top: Float, right: Float, bottom: Float) {
        path.reset()
        var rx = strokeRadius
        var ry = strokeRadius
        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry
        val startTopPadding = if (startStrokePadding > rx) startStrokePadding - rx else 0f
        path.moveTo(right, top + ry)
        path.rQuadTo(0f, -ry, -rx, -ry) //top-right corner
        path.rLineTo(-widthMinusCorners + startTopPadding + topClip, 0f)
        path.rMoveTo(-topClip, 0f)
        path.rLineTo(-startTopPadding, 0f)
        path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
        path.rLineTo(0f, heightMinusCorners)

        path.rQuadTo(0f, ry, rx, ry) //bottom-left corner
        path.rLineTo(widthMinusCorners, 0f)
        path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner

        path.rLineTo(0f, -heightMinusCorners)
        //path.close() //Given close, last lineto can be removed.
    }

    fun setStrokeColorRes(@ColorRes color: Int) {
        setStrokeColor(ContextCompat.getColor(context, color))
    }

    fun setStrokeColor(color: Int) {
        if (paintColor != color) {
            paintColor = color
            invalidate()
        }
    }

    fun setBgColorRes(@ColorRes color: Int) {
        setBgColor(ContextCompat.getColor(context, color))
    }

    fun setBgColor(color: Int) {
        if (paintBGColor != color) {
            paintBGColor = color
            invalidate()
        }
    }

    fun setStrokeWidthInDp(value: Int) {
        setStrokeWidthInPx(dpToPx(value))
    }

    fun setStrokeWidthInPx(value: Float) {
        strokeWidth = value
        invalidate()
    }

    fun setStrokeRadiusInDp(value: Int) {
        setStrokeRadiusInPx(dpToPx(value))
    }

    fun setStrokeRadiusInPx(value: Float) {
        strokeRadius = value
        invalidate()
    }

    fun setTopStartPaddingInDp(value: Int) {
        setTopStartPaddingInPx(dpToPx(value))
    }

    fun setTopStartPaddingInPx(value: Float) {
        startStrokePadding = value
        invalidate()
    }

    fun setTopClipInDp(value: Int) {
        setTopClipInPx(dpToPx(value))
    }

    fun setTopClipInPx(value: Float) {
        topClip = value
        invalidate()
    }

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}
