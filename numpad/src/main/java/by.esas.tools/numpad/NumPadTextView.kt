package by.esas.tools.numpad

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.textview.MaterialTextView

open class NumPadTextView : ConstraintLayout {
    open val TAG: String = NumPadTextView::class.java.simpleName

    val numContainerOne: FrameLayout
    val numContainerTwo: FrameLayout
    val numContainerThree: FrameLayout
    val numContainerFour: FrameLayout
    val numContainerFive: FrameLayout
    val numContainerSix: FrameLayout
    val numContainerSeven: FrameLayout
    val numContainerEight: FrameLayout
    val numContainerNine: FrameLayout
    val numContainerZero: FrameLayout
    val btnContainerLeft: FrameLayout
    val btnContainerRight: FrameLayout

    val numTextOne: MaterialTextView
    val numTextTwo: MaterialTextView
    val numTextThree: MaterialTextView
    val numTextFour: MaterialTextView
    val numTextFive: MaterialTextView
    val numTextSix: MaterialTextView
    val numTextSeven: MaterialTextView
    val numTextEight: MaterialTextView
    val numTextNine: MaterialTextView
    val numTextZero: MaterialTextView
    val btnIconLeft: AppCompatImageView
    val btnIconRight: AppCompatImageView

    val iconsContainersList = ArrayList<FrameLayout>()
    val iconsNumbersList = ArrayList<MaterialTextView>()

    val defaultIconSize = 32
    val defaultIconPadding = 0
    val defaultRightIconImage = R.drawable.ic_backspace
    val defaultLeftIconImage = R.drawable.ic_cancel

    var handler: INumPadHandler? = null

    init {
        val view = inflate(context, R.layout.v_num_pad_text, this)
        numContainerOne = view.findViewById(R.id.v_num_pad_text_container_one)
        numContainerTwo = view.findViewById(R.id.v_num_pad_text_container_two)
        numContainerThree = view.findViewById(R.id.v_num_pad_text_container_three)
        numContainerFour = view.findViewById(R.id.v_num_pad_text_container_four)
        numContainerFive = view.findViewById(R.id.v_num_pad_text_container_five)
        numContainerSix = view.findViewById(R.id.v_num_pad_text_container_six)
        numContainerSeven = view.findViewById(R.id.v_num_pad_text_container_seven)
        numContainerEight = view.findViewById(R.id.v_num_pad_text_container_eight)
        numContainerNine = view.findViewById(R.id.v_num_pad_text_container_nine)
        numContainerZero = view.findViewById(R.id.v_num_pad_text_container_zero)
        btnContainerLeft = view.findViewById(R.id.v_num_pad_text_container_left_button)
        btnContainerRight = view.findViewById(R.id.v_num_pad_text_container_right_button)

        numTextOne = view.findViewById(R.id.v_num_pad_text_one)
        numTextTwo = view.findViewById(R.id.v_num_pad_text_two)
        numTextThree = view.findViewById(R.id.v_num_pad_text_three)
        numTextFour = view.findViewById(R.id.v_num_pad_text_four)
        numTextFive = view.findViewById(R.id.v_num_pad_text_five)
        numTextSix = view.findViewById(R.id.v_num_pad_text_six)
        numTextSeven = view.findViewById(R.id.v_num_pad_text_seven)
        numTextEight = view.findViewById(R.id.v_num_pad_text_eight)
        numTextNine = view.findViewById(R.id.v_num_pad_text_nine)
        numTextZero = view.findViewById(R.id.v_num_pad_text_zero)
        btnIconLeft = view.findViewById(R.id.v_num_pad_icon_left)
        btnIconRight = view.findViewById(R.id.v_num_pad_icon_right)

        iconsContainersList.addAll(
            listOf(
                numContainerZero,
                numContainerOne,
                numContainerTwo,
                numContainerThree,
                numContainerFour,
                numContainerFive,
                numContainerSix,
                numContainerSeven,
                numContainerEight,
                numContainerNine
            )
        )

        iconsNumbersList.addAll(
            listOf(
                numTextOne,
                numTextTwo,
                numTextThree,
                numTextFour,
                numTextFive,
                numTextSix,
                numTextSeven,
                numTextEight,
                numTextNine,
                numTextZero
            )
        )
    }

    /*region ################### Constructors ######################*/
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
    /*endregion ################### Constructors ######################*/

    /*region ################### All Icons Settings ######################*/
    open fun enableNumpadView() {
        iconsContainersList.forEach { container ->
            container.isClickable = true
        }
        btnContainerLeft.isClickable = true
        btnContainerRight.isClickable = true
    }

    open fun disableNumpadView() {
        iconsContainersList.forEach { container ->
            container.isClickable = false
        }
        btnContainerLeft.isClickable = false
        btnContainerRight.isClickable = false
    }

    open fun setIconsSize(widthValue: Int, heightValue: Int) {
        iconsContainersList.forEach { container ->
            setIconContainerSize(container, widthValue, heightValue)
        }
        setIconContainerSize(btnContainerLeft, widthValue, heightValue)
        setIconContainerSize(btnContainerRight, widthValue, heightValue)
    }

    protected open fun setIconContainerSize(iconContainer: FrameLayout, widthValue: Int, heightValue: Int) {
        iconContainer.layoutParams.apply {
            width = dpToPx(widthValue).toInt()
            height = dpToPx(heightValue).toInt()
        }
        iconContainer.requestLayout()
    }

    open fun setIconsPadding(paddingValue: Int) {
        iconsNumbersList.forEach { icon ->
            setIconPadding(icon, paddingValue)
        }
        setIconPadding(btnIconLeft, paddingValue)
        setIconPadding(btnIconRight, paddingValue)
    }

    protected open fun setIconPadding(iconTextView: MaterialTextView, paddingValue: Int) {
        val padding = dpToPx(paddingValue).toInt()
        iconTextView.setPadding(padding, padding, padding, padding)
    }

    protected open fun setIconPadding(iconButton: AppCompatImageView, paddingValue: Int) {
        val padding = dpToPx(paddingValue).toInt()
        iconButton.setPadding(padding, padding, padding, padding)
    }

    protected open fun setButtonVisibility(iconContainer: FrameLayout, value: Boolean) {
        if (value) {
            iconContainer.visibility = View.VISIBLE
            iconContainer.isClickable = true
        } else {
            iconContainer.visibility = View.INVISIBLE
            iconContainer.isClickable = false
        }
    }

    open fun setNumpadHandler(numpadHandler: INumPadHandler) {
        handler = numpadHandler
    }

    protected open fun setupNumpadHandler() {
        iconsContainersList.forEach { container ->
            container.setOnClickListener {
                handler?.onNumClick(iconsContainersList.indexOf(container))
            }
        }
        btnContainerLeft.setOnClickListener {
            handler?.onLeftIconClick()
        }
        btnContainerRight.setOnClickListener {
            handler?.onRightIconClick()
        }
    }
    /*endregion ################### All Icons Settings ######################*/

    /*region ################### Number Text Icons ######################*/
    open fun setNumbersTextSize(size: Int) {
        iconsNumbersList.forEach { num ->
            num.textSize = size.toFloat()
        }
    }

    open fun setNumbersTextStyle(styleId: Int) {
        if (styleId != -1) {
            iconsNumbersList.forEach { num ->
                setNumberTextStyle(num, styleId)
            }
        }
    }

    protected open fun setNumberTextStyle(textView: MaterialTextView, styleId: Int) {
        textView.apply { TextViewCompat.setTextAppearance(this, styleId) }
    }

    open fun setNumbersTextColor(color: Int) {
        iconsNumbersList.forEach { num ->
            setNumberTextColor(num, color)
        }
    }

    protected open fun setNumberTextColor(textView: MaterialTextView, color: Int) {
        textView.setTextColor(color)
    }
    /*endregion ################### Number Text Icons ######################*/

    /*region ################### Left Button Icon ######################*/
    open fun setLeftButtonIconImage(drawable: Drawable?) {
        if (drawable != null) {
            btnIconLeft.setImageDrawable(drawable)
        } else {
            setLeftButtonVisibility(false)
        }
    }

    open fun setLeftButtonIconImage(resId: Int?) {
        if (resId != null) {
            btnIconLeft.setImageResource(resId)
        } else {
            setLeftButtonVisibility(false)
        }
    }

    open fun setLeftButtonIconSize(widthValue: Int, heightValue: Int) {
        btnIconLeft.layoutParams.apply {
            width = dpToPx(widthValue).toInt()
            height = dpToPx(heightValue).toInt()
        }
        btnIconLeft.requestLayout()
    }

    open fun setLeftButtonVisibility(value: Boolean) {
        setButtonVisibility(btnContainerLeft, value)
    }

    /**
     * Set color for left icon. Color is set once and doesn't save.
     * So at first you should set icon drawable and then change color.
     */
    open fun setLeftIconColor(color: Int) {
        if (color != -1)
            ImageViewCompat.setImageTintList(btnIconLeft, ColorStateList.valueOf(color))
    }

    open fun setLeftIconColorResource(@ColorRes color: Int) {
        setLeftIconColor(ContextCompat.getColor(context, color))
    }
    /*endregion ################### Left Button Icon ######################*/

    /*region ################### Right Button Icon ######################*/
    open fun setRightButtonIconImage(drawable: Drawable?) {
        if (drawable != null) {
            btnIconRight.setImageDrawable(drawable)
        } else {
            btnIconRight.setImageResource(defaultRightIconImage)
        }
    }

    open fun setRightButtonIconImage(resId: Int?) {
        val imageRes = resId ?: defaultRightIconImage
        btnIconRight.setImageResource(imageRes)
    }

    open fun setRightButtonIconSize(widthValue: Int, heightValue: Int) {
        btnIconRight.layoutParams.apply {
            width = dpToPx(widthValue).toInt()
            height = dpToPx(heightValue).toInt()
        }
        btnIconRight.requestLayout()
    }

    open fun setRightButtonVisibility(value: Boolean) {
        setButtonVisibility(btnContainerRight, value)
    }

    /**
     * Set color for right icon. Color is set once and doesn't save.
     * So at first you should set icon drawable and then change color.
     */
    open fun setRightIconColor(color: Int) {
        if (color != -1)
            ImageViewCompat.setImageTintList(btnIconRight, ColorStateList.valueOf(color))
    }

    open fun setRightIconColorResource(@ColorRes color: Int) {
        setRightIconColor(ContextCompat.getColor(context, color))
    }

    /*endregion ################### Right Button Icon ######################*/

    /*region ################### Other ######################*/

    fun setDefault() {
        setIconsSize(defaultIconSize, defaultIconSize)
        setIconsPadding(0)
        setLeftButtonIconImage(defaultLeftIconImage)
        setRightButtonIconImage(defaultRightIconImage)
        enableNumpadView()
        setupNumpadHandler()
    }

    fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumPadTextView)

        val iconPadding =
            typedArray.getDimensionPixelSize(R.styleable.NumPadTextView_numpadTextIconPadding, defaultIconPadding)
        val iconSize = typedArray.getDimensionPixelSize(R.styleable.NumPadTextView_numpadTextIconSize, defaultIconSize)
        val iconTextStyle = typedArray.getResourceId(R.styleable.NumPadTextView_numpadTextIconStyle, -1)

        val iconLeftImage = typedArray.getDrawable(R.styleable.NumPadTextView_numpadTextIconLeft)
        val iconLeftColor = typedArray.getColor(R.styleable.NumPadTextView_numpadTextLeftIconColor, -1)
        val iconRightImage = typedArray.getDrawable(R.styleable.NumPadTextView_numpadTextIconRight)
        val iconRightColor = typedArray.getColor(R.styleable.NumPadTextView_numpadTextRightIconColor, -1)

        setNumbersTextStyle(iconTextStyle)
        setIconsPadding(iconPadding)
        setIconsSize(iconSize, iconSize)
        setLeftButtonIconImage(iconLeftImage)
        setLeftIconColor(iconLeftColor)
        setRightButtonIconImage(iconRightImage)
        setRightIconColor(iconRightColor)
        setupNumpadHandler()
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
    /*endregion ################### Other ######################*/
}