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
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import by.esas.tools.util.SwitchManager

open class NumPadImageView : ConstraintLayout, SwitchManager.ISwitchView {
    open val TAG: String = NumPadImageView::class.java.simpleName

    protected val numContainerZero: FrameLayout
    protected val numContainerOne: FrameLayout
    protected val numContainerTwo: FrameLayout
    protected val numContainerThree: FrameLayout
    protected val numContainerFour: FrameLayout
    protected val numContainerFive: FrameLayout
    protected val numContainerSix: FrameLayout
    protected val numContainerSeven: FrameLayout
    protected val numContainerEight: FrameLayout
    protected val numContainerNine: FrameLayout
    protected val butContainerLeft: FrameLayout
    protected val butContainerRight: FrameLayout

    protected val numIconZero: AppCompatImageView
    protected val numIconOne: AppCompatImageView
    protected val numIconTwo: AppCompatImageView
    protected val numIconThree: AppCompatImageView
    protected val numIconFour: AppCompatImageView
    protected val numIconFive: AppCompatImageView
    protected val numIconSix: AppCompatImageView
    protected val numIconSeven: AppCompatImageView
    protected val numIconEight: AppCompatImageView
    protected val numIconNine: AppCompatImageView
    protected val btnIconLeft: AppCompatImageView
    protected val btnIconRight: AppCompatImageView

    protected val iconsContainersList = ArrayList<FrameLayout>()
    protected val iconsNumbersList = ArrayList<AppCompatImageView>()

    protected val defaultIconSize: Int = 24
    protected val defaultIconPadding = 10
    protected val defaultNumbersImages = listOf(
        R.drawable.ic_pin_0,
        R.drawable.ic_pin_1,
        R.drawable.ic_pin_2,
        R.drawable.ic_pin_3,
        R.drawable.ic_pin_4,
        R.drawable.ic_pin_5,
        R.drawable.ic_pin_6,
        R.drawable.ic_pin_7,
        R.drawable.ic_pin_8,
        R.drawable.ic_pin_9
    )

    protected val defaultRightIconImage = R.drawable.ic_backspace
    protected val defaultLeftIconImage = R.drawable.ic_cancel

    protected var handler: INumPadHandler? = null

    init {
        val view = inflate(context, R.layout.v_num_pad_image, this)
        numContainerZero = view.findViewById(R.id.v_num_pad_container_zero)
        numContainerOne = view.findViewById(R.id.v_num_pad_container_one)
        numContainerTwo = view.findViewById(R.id.v_num_pad_container_two)
        numContainerThree = view.findViewById(R.id.v_num_pad_container_three)
        numContainerFour = view.findViewById(R.id.v_num_pad_container_four)
        numContainerFive = view.findViewById(R.id.v_num_pad_container_five)
        numContainerSix = view.findViewById(R.id.v_num_pad_container_six)
        numContainerSeven = view.findViewById(R.id.v_num_pad_container_seven)
        numContainerEight = view.findViewById(R.id.v_num_pad_container_eight)
        numContainerNine = view.findViewById(R.id.v_num_pad_container_nine)
        butContainerLeft = view.findViewById(R.id.v_num_pad_container_left_button)
        butContainerRight = view.findViewById(R.id.v_num_pad_container_right_button)

        numIconZero = view.findViewById(R.id.v_num_pad_icon_zero)
        numIconOne = view.findViewById(R.id.v_num_pad_icon_one)
        numIconTwo = view.findViewById(R.id.v_num_pad_icon_two)
        numIconThree = view.findViewById(R.id.v_num_pad_icon_three)
        numIconFour = view.findViewById(R.id.v_num_pad_icon_four)
        numIconFive = view.findViewById(R.id.v_num_pad_icon_five)
        numIconSix = view.findViewById(R.id.v_num_pad_icon_six)
        numIconSeven = view.findViewById(R.id.v_num_pad_icon_seven)
        numIconEight = view.findViewById(R.id.v_num_pad_icon_eight)
        numIconNine = view.findViewById(R.id.v_num_pad_icon_nine)
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
                numIconZero,
                numIconOne,
                numIconTwo,
                numIconThree,
                numIconFour,
                numIconFive,
                numIconSix,
                numIconSeven,
                numIconEight,
                numIconNine
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
    protected open fun setIconVisibility(iconContainer: FrameLayout, value: Boolean) {
        if (value) {
            iconContainer.visibility = View.VISIBLE
        } else {
            iconContainer.visibility = View.INVISIBLE
            setIconClickListener(iconContainer, null)
        }
    }

    protected open fun setIconClickListener(
        iconContainer: FrameLayout,
        listener: OnClickListener?
    ) {
        iconContainer.setOnClickListener(listener)
    }

    open fun setIconsSize(iconSize: Int) {
        iconsNumbersList.forEach { icon ->
            setIconSize(icon, iconSize)
        }
        setIconSize(btnIconLeft, iconSize)
        setIconSize(btnIconRight, iconSize)
    }

    protected open fun setIconSize(iconImageView: ImageView, iconSize: Int) {
        iconImageView.layoutParams.apply {
            width = dpToPx(iconSize)
            height = dpToPx(iconSize)
        }
        iconImageView.requestLayout()
    }

    open fun setIconsMargins(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
        iconsNumbersList.forEach { num ->
            setIconViewMargins(num, leftMargin, topMargin, rightMargin, bottomMargin)
        }
        setIconViewMargins(btnIconLeft, leftMargin, topMargin, rightMargin, bottomMargin)
        setIconViewMargins(btnIconRight, leftMargin, topMargin, rightMargin, bottomMargin)
    }

    protected open fun setIconViewMargins(
        iconImageView: ImageView,
        leftMargin: Int,
        topMargin: Int,
        rightMargin: Int,
        bottomMargin: Int
    ) {
        (iconImageView.layoutParams as MarginLayoutParams).apply {
            setMargins(
                dpToPx(leftMargin),
                dpToPx(topMargin),
                dpToPx(rightMargin),
                dpToPx(bottomMargin)
            )
        }
    }

    open fun setIconsPaddings(
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        iconsNumbersList.forEach { icon ->
            setIconViewPaddings(icon, leftPadding, topPadding, rightPadding, bottomPadding)
        }
        setIconViewPaddings(btnIconLeft, leftPadding, topPadding, rightPadding, bottomPadding)
        setIconViewPaddings(btnIconRight, leftPadding, topPadding, rightPadding, bottomPadding)
    }

    protected open fun setIconViewPaddings(
        iconImageView: ImageView,
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        iconImageView.setPadding(
            dpToPx(leftPadding),
            dpToPx(topPadding),
            dpToPx(rightPadding),
            dpToPx(bottomPadding)
        )
    }

    open fun setIconsSelectableBackground(value: Boolean) {
        iconsContainersList.forEach { container ->
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

    open fun enableNumpadView() {
        iconsContainersList.forEach { container ->
            container.isClickable = true
        }
        butContainerLeft.isClickable = true
        butContainerRight.isClickable = true
    }

    open fun disableNumpadView() {
        iconsContainersList.forEach { container ->
            container.isClickable = false
        }
        butContainerLeft.isClickable = false
        butContainerRight.isClickable = false
    }
    /*endregion ################### All Icons ######################*/

    /*region ################### Containers Number Icons ######################*/
    open fun setNumbersClickListener(listener: View.OnClickListener?) {
        iconsContainersList.forEach { container ->
            setIconClickListener(container, listener)
        }
    }
    /*endregion ################### Containers Number Icons ######################*/

    /*region ################### Number Icons ######################*/
    /**
     * Set color for numbers icons. Color is set once and doesn't save.
     * So at first you should set icons drawable and then change color.
     */
    open fun setNumbersIconsColor(color: Int) {
        if (color != -1) {
            iconsNumbersList.forEach { num ->
                setIconColor(num, color)
            }
        }
    }

    protected open fun setIconColor(iconImageView: ImageView, color: Int) {
        ImageViewCompat.setImageTintList(iconImageView, ColorStateList.valueOf(color))
    }

    open fun setNumbersIconsDrawable(drawableList: List<Drawable?>) {
        if (iconsNumbersList.size == drawableList.size) {
            drawableList.forEachIndexed { index, drawable ->
                if (drawable != null)
                    setIconDrawable(iconsNumbersList[index], drawable)
                else
                    setIconImageResource(iconsNumbersList[index], defaultNumbersImages[index])
            }
        } else {
            throw IndexOutOfBoundsException("Amount of drawables is not the same as amount of items in iconsNumbersList (${drawableList.size} instead of ${iconsNumbersList.size}).")
        }
    }

    open fun setNumbersIconsImageResources(imageResourcesList: List<Int?>) {
        if (iconsNumbersList.size == imageResourcesList.size) {
            imageResourcesList.forEachIndexed { index, image ->
                val imageRes = image ?: defaultNumbersImages[index]
                setIconImageResource(iconsNumbersList[index], imageRes)
            }
        } else {
            throw IndexOutOfBoundsException("Amount of images is not the same as amount of items in iconsNumbersList (${imageResourcesList.size} instead of ${iconsNumbersList.size}).")
        }
    }

    protected open fun setIconDrawable(iconImageView: ImageView, drawable: Drawable) {
        iconImageView.setImageDrawable(drawable)
    }

    protected open fun setIconImageResource(iconImageView: ImageView, imageResource: Int?) {
        if (imageResource != null) {
            iconImageView.setImageResource(imageResource)
        }
    }

    open fun setDefaultNumpadIcons() {
        setNumbersIconsImageResources(defaultNumbersImages)
        setLeftIconImageResource(defaultLeftIconImage)
        setRightIconImageResource(defaultRightIconImage)
    }
    /*endregion ################### Number Icons ######################*/

    /*region ################### Left Image Icons ######################*/
    open fun setLeftIconVisibility(value: Boolean) {
        setIconVisibility(butContainerLeft, value)
    }

    open fun setLeftIconDrawable(drawable: Drawable?) {
        if (drawable != null) {
            btnIconLeft.setImageDrawable(drawable)
        } else {
            btnIconLeft.setImageResource(defaultLeftIconImage)
        }
    }

    open fun setLeftIconImageResource(resId: Int?) {
        val imageRes = resId ?: defaultLeftIconImage
        btnIconLeft.setImageResource(imageRes)
    }

    /**
     * Set color for left icon. Color is set once and doesn't save.
     * So at first you should set icon drawable and then change color.
     */
    open fun setLeftIconColor(color: Int) {
        if (color != -1)
            setIconColor(btnIconLeft, color)
    }

    open fun setLeftIconColorResource(@ColorRes color: Int) {
        setLeftIconColor(ContextCompat.getColor(context, color))
    }
    /*endregion ################### Left Image Icons ######################*/

    /*region ################### Right Image Icons ######################*/
    open fun setRightIconVisibility(value: Boolean) {
        setIconVisibility(butContainerRight, value)
    }

    open fun setRightIconDrawable(drawable: Drawable?) {
        if (drawable != null)
            btnIconRight.setImageDrawable(drawable)
        else
            btnIconRight.setImageResource(defaultRightIconImage)
    }

    open fun setRightIconImageResource(resId: Int?) {
        val imageRes = resId ?: defaultRightIconImage
        btnIconRight.setImageResource(imageRes)
    }

    /**
     * Set color for right icon. Color is set once and doesn't save.
     * So at first you should set icon drawable and then change color.
     */
    open fun setRightIconColor(color: Int) {
        if (color != -1)
            setIconColor(btnIconRight, color)
    }

    open fun setRightIconColorResource(@ColorRes color: Int) {
        setRightIconColor(ContextCompat.getColor(context, color))
    }
    /*endregion ################### Right Image Icons ######################*/

    /*region ################### Handler ######################*/
    open fun setNumpadHandler(numpadHandler: INumPadHandler) {
        handler = numpadHandler
    }

    protected open fun setupNumpadHandler() {
        iconsContainersList.forEach { container ->
            container.setOnClickListener {
                handler?.onNumClick(iconsContainersList.indexOf(container))
            }
        }
        butContainerLeft.setOnClickListener {
            handler?.onLeftIconClick()
        }
        butContainerRight.setOnClickListener {
            handler?.onRightIconClick()
        }
    }
    /*endregion ################### Handler ######################*/

    /*region ################### Other ######################*/
    fun setDefaults() {
        setDefaultNumpadIcons()
        setLeftIconVisibility(true)
        setRightIconVisibility(true)
        setIconsSize(defaultIconSize)
        setIconsPaddings(
            defaultIconPadding,
            defaultIconPadding,
            defaultIconPadding,
            defaultIconPadding
        )
        setupNumpadHandler()
        enableNumpadView()
    }

    fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumPadView)

        val iconPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.NumPadView_numpadIconPadding,
                defaultIconPadding
            )
        val iconSize =
            typedArray.getDimensionPixelSize(R.styleable.NumPadView_numpadIconSize, defaultIconSize)

        val iconZeroImage: Drawable? =
            typedArray.getDrawable(R.styleable.NumPadView_numpadZeroDrawable)
        val iconOneImage = typedArray.getDrawable(R.styleable.NumPadView_numpadOneDrawable)
        val iconTwoImage = typedArray.getDrawable(R.styleable.NumPadView_numpadTwoDrawable)
        val iconThreeImage = typedArray.getDrawable(R.styleable.NumPadView_numpadThreeDrawable)
        val iconFourImage = typedArray.getDrawable(R.styleable.NumPadView_numpadFourDrawable)
        val iconFiveImage = typedArray.getDrawable(R.styleable.NumPadView_numpadFiveDrawable)
        val iconSixImage = typedArray.getDrawable(R.styleable.NumPadView_numpadSixDrawable)
        val iconSevenImage = typedArray.getDrawable(R.styleable.NumPadView_numpadSevenDrawable)
        val iconEightImage = typedArray.getDrawable(R.styleable.NumPadView_numpadEightDrawable)
        val iconNineImage = typedArray.getDrawable(R.styleable.NumPadView_numpadNineDrawable)
        val iconsNumbersColor =
            typedArray.getColor(R.styleable.NumPadView_numpadNumbersIconsColor, -1)

        val iconLeftImage = typedArray.getDrawable(R.styleable.NumPadView_numpadLeftDrawable)
        val iconLeftColor = typedArray.getColor(R.styleable.NumPadView_numpadLeftIconColor, -1)
        val iconLeftVisibility =
            typedArray.getBoolean(R.styleable.NumPadView_numpadLeftIconVisibitity, true)

        val iconRightImage = typedArray.getDrawable(R.styleable.NumPadView_numpadRightDrawable)
        val iconRightColor = typedArray.getColor(R.styleable.NumPadView_numpadLeftIconColor, -1)
        val iconRightVisibility =
            typedArray.getBoolean(R.styleable.NumPadView_numpadRightIconVisibitity, true)

        typedArray.recycle()

        val iconsImagesDrawableList = listOf<Drawable?>(
            iconZeroImage,
            iconOneImage,
            iconTwoImage,
            iconThreeImage,
            iconFourImage,
            iconFiveImage,
            iconSixImage,
            iconSevenImage,
            iconEightImage,
            iconNineImage
        )

        setNumbersIconsDrawable(iconsImagesDrawableList)
        setNumbersIconsColor(iconsNumbersColor)
        setIconsSize(iconSize)
        setIconsMargins(iconPadding, iconPadding, iconPadding, iconPadding)

        setLeftIconDrawable(iconLeftImage)
        setLeftIconColor(iconLeftColor)
        setLeftIconVisibility(iconLeftVisibility)

        setRightIconDrawable(iconRightImage)
        setRightIconColor(iconRightColor)
        setRightIconVisibility(iconRightVisibility)
        setupNumpadHandler()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun switchOn() {
        enableNumpadView()
    }

    override fun switchOff() {
        disableNumpadView()
    }
    /*endregion ################### Other ######################*/
}
