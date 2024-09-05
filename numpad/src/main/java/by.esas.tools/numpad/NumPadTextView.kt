package by.esas.tools.numpad

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import by.esas.tools.util.SwitchManager
import com.google.android.material.textview.MaterialTextView

open class NumPadTextView : ConstraintLayout, SwitchManager.ISwitchView {

    open val TAG: String = NumPadTextView::class.java.simpleName

    protected val numContainerOne: FrameLayout
    protected val numContainerTwo: FrameLayout
    protected val numContainerThree: FrameLayout
    protected val numContainerFour: FrameLayout
    protected val numContainerFive: FrameLayout
    protected val numContainerSix: FrameLayout
    protected val numContainerSeven: FrameLayout
    protected val numContainerEight: FrameLayout
    protected val numContainerNine: FrameLayout
    protected val numContainerZero: FrameLayout
    protected val btnContainerLeft: FrameLayout
    protected val btnContainerRight: FrameLayout

    protected val numTextOne: MaterialTextView
    protected val numTextTwo: MaterialTextView
    protected val numTextThree: MaterialTextView
    protected val numTextFour: MaterialTextView
    protected val numTextFive: MaterialTextView
    protected val numTextSix: MaterialTextView
    protected val numTextSeven: MaterialTextView
    protected val numTextEight: MaterialTextView
    protected val numTextNine: MaterialTextView
    protected val numTextZero: MaterialTextView
    protected val btnIconLeft: AppCompatImageView
    protected val btnIconRight: AppCompatImageView

    protected val iconsContainers = ArrayList<FrameLayout>()
    protected val iconsNumbers = ArrayList<MaterialTextView>()
    protected val iconsNumbersContainers = ArrayList<FrameLayout>()

    protected val defaultIconSize = dpToPx(32)
    protected val defaultIconPadding = 0
    protected val defaultRightIconImage = R.drawable.ic_backspace
    protected val defaultLeftIconImage = R.drawable.ic_cancel

    protected var handler: INumPadHandler? = null

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

        iconsNumbersContainers.addAll(
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

        iconsNumbers.addAll(
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

        iconsContainers.addAll(iconsNumbersContainers)
        iconsContainers.addAll(listOf(btnContainerLeft, btnContainerRight))
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

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(attrs)
    }

    /*endregion ################### Constructors ######################*/

    /*region ################### ISwitchView interface ######################*/

    override fun switchOn() {
        enableNumpadView()
    }

    override fun switchOff() {
        disableNumpadView()
    }

    /*endregion ################### ISwitchView interface ######################*/

    /*region ################### All Icons Settings ######################*/

    open fun enableNumpadView() {
        iconsContainers.forEach { container ->
            container.isClickable = true
        }
    }

    open fun disableNumpadView() {
        iconsContainers.forEach { container ->
            container.isClickable = false
        }
    }

    /**
     * Set size for all icons containers.
     */
    open fun setIconsSize(widthValue: Int, heightValue: Int) {
        iconsContainers.forEach { container ->
            setIconContainerSize(container, widthValue, heightValue)
        }
    }

    protected open fun setIconContainerSize(
        iconContainer: FrameLayout,
        widthValue: Int,
        heightValue: Int
    ) {
        iconContainer.layoutParams.apply {
            width = widthValue
            height = heightValue
        }
        iconContainer.requestLayout()
    }

    open fun setIconsPadding(
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        iconsContainers.forEach { container ->
            setIconContainerPadding(container, leftPadding, topPadding, rightPadding, bottomPadding)
        }
    }

    protected open fun setIconContainerPadding(
        iconContainer: FrameLayout,
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        iconContainer.setPadding(
            leftPadding,
            topPadding,
            rightPadding,
            bottomPadding
        )
    }

    open fun setIconsMargin(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
        iconsContainers.forEach { container ->
            setIconContainerMargin(container, leftMargin, topMargin, rightMargin, bottomMargin)
        }
    }

    protected open fun setIconContainerMargin(
        iconContainer: FrameLayout,
        leftMargin: Int,
        topMargin: Int,
        rightMargin: Int,
        bottomMargin: Int
    ) {
        (iconContainer.layoutParams as MarginLayoutParams).apply {
            setMargins(
                leftMargin,
                topMargin,
                rightMargin,
                bottomMargin
            )
        }
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

    open fun setIconsSelectableBackground(value: Boolean) {
        iconsContainers.forEach { container ->
            setContainerSelectableBackground(container, value)
        }
    }

    protected open fun setContainerSelectableBackground(container: FrameLayout, value: Boolean) {
        container.apply {
            background = if (value) {
                with(TypedValue()) {
                    context.theme.resolveAttribute(
                        R.attr.selectableItemBackground, this, value
                    )
                    ContextCompat.getDrawable(context, resourceId)
                }
            } else {
                null
            }
        }
    }

    open fun setNumpadHandler(numpadHandler: INumPadHandler) {
        handler = numpadHandler
    }

    protected open fun setupNumpadHandler() {
        iconsNumbersContainers.forEach { container ->
            container.setOnClickListener {
                handler?.onNumClick(iconsNumbersContainers.indexOf(container))
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

    /**
     * Set textSize for number icons.
     * Remember, that containers for numbers icons have height and width fixed size(not wrap_content).
     * So when you want to change numbers textSize you should change size of containers too.
     * And size of left and right icon button should change in relevant methods
     * [setLeftIconSize] and [setRightIconSize].
     * @see [setIconsSize].
     */
    open fun setNumbersTextSize(size: Int) {
        iconsNumbers.forEach { num ->
            num.textSize = size.toFloat()
        }
    }

    open fun setNumbersTextStyle(styleId: Int) {
        if (styleId != -1) {
            iconsNumbers.forEach { num ->
                setNumberTextStyle(num, styleId)
            }
        }
    }

    open fun setNumbersDefaultStyle() {
        iconsNumbers.forEach { num ->
            setNumberTextStyle(num, R.style.NumPadTextIconsStyle)
        }
    }

    protected open fun setNumberTextStyle(textView: MaterialTextView, styleId: Int) {
        textView.apply { TextViewCompat.setTextAppearance(this, styleId) }
    }

    open fun setNumbersTextColor(color: Int) {
        iconsNumbers.forEach { num ->
            setNumberTextColor(num, color)
        }
    }

    protected open fun setNumberTextColor(textView: MaterialTextView, color: Int) {
        textView.setTextColor(color)
    }

    /*endregion ################### Number Text Icons ######################*/

    /*region ################### Left Button Icon ######################*/

    open fun setLeftIconImage(drawable: Drawable?) {
        if (drawable != null) {
            btnIconLeft.setImageDrawable(drawable)
        } else {
            setLeftIconVisibility(false)
        }
    }

    open fun setLeftIconImageResource(resId: Int?) {
        if (resId != null) {
            btnIconLeft.setImageResource(resId)
        } else {
            setLeftIconVisibility(false)
        }
    }

    open fun setLeftIconSize(widthValue: Int, heightValue: Int) {
        btnIconLeft.layoutParams.apply {
            width = widthValue
            height = heightValue
        }
        btnIconLeft.requestLayout()
    }

    open fun setLeftIconVisibility(value: Boolean) {
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
        if (color != -1)
            ImageViewCompat.setImageTintList(
                btnIconLeft,
                ContextCompat.getColorStateList(context, color)
            )
    }

    /*endregion ################### Left Button Icon ######################*/

    /*region ################### Right Button Icon ######################*/

    open fun setRightIconImage(drawable: Drawable?) {
        if (drawable != null) {
            btnIconRight.setImageDrawable(drawable)
        } else {
            btnIconRight.setImageResource(defaultRightIconImage)
        }
    }

    open fun setRightIconImageResource(resId: Int?) {
        val imageRes = resId ?: defaultRightIconImage
        btnIconRight.setImageResource(imageRes)
    }

    open fun setRightIconSize(widthValue: Int, heightValue: Int) {
        btnIconRight.layoutParams.apply {
            width = widthValue
            height = heightValue
        }
        btnIconRight.requestLayout()
    }

    open fun setRightIconVisibility(value: Boolean) {
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
        if (color != -1)
            ImageViewCompat.setImageTintList(
                btnIconRight,
                ContextCompat.getColorStateList(context, color)
            )
    }

    /*endregion ################### Right Button Icon ######################*/

    /*region ################### Other ######################*/

    fun setDefault() {
        setIconsSize(defaultIconSize, defaultIconSize)
        setIconsPadding(
            defaultIconPadding,
            defaultIconPadding,
            defaultIconPadding,
            defaultIconPadding
        )
        setNumbersDefaultStyle()

        setLeftIconImageResource(defaultLeftIconImage)
        setLeftIconVisibility(true)
        setRightIconImageResource(defaultRightIconImage)
        setRightIconVisibility(true)

        enableNumpadView()
        setupNumpadHandler()
    }

    fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumPadTextView)

        val iconPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.NumPadTextView_numpadTextIconPadding,
                defaultIconPadding
            )
        val iconSize = typedArray.getDimensionPixelSize(
            R.styleable.NumPadTextView_numpadTextIconSize,
            defaultIconSize
        )
        val iconTextStyle =
            typedArray.getResourceId(R.styleable.NumPadTextView_numpadTextIconStyle, -1)

        val iconLeftImage =
            typedArray.getDrawable(R.styleable.NumPadTextView_numpadTextLeftIconImage)
        val iconLeftSize =
            typedArray.getInt(R.styleable.NumPadTextView_numpadTextLeftIconSize, defaultIconSize)
        val iconLeftColor =
            typedArray.getResourceId(R.styleable.NumPadTextView_numpadTextLeftIconColor, -1)
        val iconLeftVisibility =
            typedArray.getBoolean(R.styleable.NumPadTextView_numpadTextLeftIconVisibitity, true)

        val iconRightImage =
            typedArray.getDrawable(R.styleable.NumPadTextView_numpadTextRightIconImage)
        val iconRightSize =
            typedArray.getInt(R.styleable.NumPadTextView_numpadTextRightIconSize, defaultIconSize)
        val iconRightColor =
            typedArray.getResourceId(R.styleable.NumPadTextView_numpadTextRightIconColor, -1)
        val iconRightVisibility =
            typedArray.getBoolean(R.styleable.NumPadTextView_numpadTextRightIconVisibitity, true)

        typedArray.recycle()

        setNumbersTextStyle(iconTextStyle)
        setIconsPadding(iconPadding, iconPadding, iconPadding, iconPadding)
        setIconsSize(iconSize, iconSize)

        setLeftIconImage(iconLeftImage)
        setLeftIconSize(iconLeftSize, iconLeftSize)
        setLeftIconColorResource(iconLeftColor)
        setLeftIconVisibility(iconLeftVisibility)

        setRightIconImage(iconRightImage)
        setRightIconColorResource(iconRightColor)
        setRightIconVisibility(iconRightVisibility)
        setRightIconSize(iconRightSize, iconRightSize)

        setupNumpadHandler()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    /*endregion ################### Other ######################*/
}
