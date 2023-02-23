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

/**
 * This custom view is Numpad for entering numbers. It consists of 9 clickable icons for numbers
 * and two buttons in the lower left and right corners. All icons are images and each of them
 * has its own container.
 *
 * Icons for numbers are images which you can set by adding them in XML or
 * programmatically using methods [setNumbersIconsDrawable] or [setNumbersIconsImageResources].
 * Also you can change color of numbers using [setNumbersIconsColor] or
 * [setNumbersIconsColorResource] method.
 *
 * By default bottom left and right buttons are visible. You can change their visibility
 * via [setLeftIconVisibility] and [setRightIconVisibility]. If these buttons are not visible
 * they also become not clickable. For these buttons you also can set your own images
 * with [setLeftIconDrawable]/[setLeftIconImageResource] and
 * [setRightIconDrawable]/[setRightIconImageResource] methods,
 * set their colors [setLeftIconColor] and [setRightIconColor].
 * If you didn't set your images for numbers and buttons Numpad will use default icons,
 * but also you can set default icons using [setDefaultNumpadIcons] method.
 *
 * All icons by default have the same size in 24dp and you can change it in XML or
 * use [setIconsSize] method. All image icons have their own containers which are FrameLayouts
 * and their size is wrap_content. These containers have selectable background and
 * you can turn on and off it using [setIconsSelectableBackground] method. For all icons containers
 * you can set paddings via [setIconsContainersPaddings].
 * To use all default characteristics for Numpad use [setDefaults] method.
 *
 * For handling Numpad clicks use [INumPadHandler]. Override this handler methods to
 * handle clicks on number icons and bottom left and right buttons.
 *
 * Numpad view implements ISwitchView view interface. On switchOn method
 * Numpad is enabled(see [enableNumpadView]) and on switchOff Numpad is disabled(see [disableNumpadView]).
 *
 * @see NumPadTextView
 */

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
    protected val btnContainerLeft: FrameLayout
    protected val btnContainerRight: FrameLayout

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

    protected val iconsContainers = ArrayList<FrameLayout>()
    protected val iconsNumbers = ArrayList<AppCompatImageView>()

    protected val defaultIconSize: Int = dpToPx(24)
    protected val defaultContainerPadding = 0
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
        btnContainerLeft = view.findViewById(R.id.v_num_pad_container_left_button)
        btnContainerRight = view.findViewById(R.id.v_num_pad_container_right_button)

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

        iconsContainers.addAll(
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

    /*region ################### ISwitchView interface ######################*/

    override fun switchOn() {
        enableNumpadView()
    }

    override fun switchOff() {
        disableNumpadView()
    }

    /*endregion ################### ISwitchView interface ######################*/

    /*region ################### All Icons Settings ######################*/

    protected open fun setIconVisibility(iconContainer: FrameLayout, value: Boolean) {
        if (value) {
            iconContainer.visibility = View.VISIBLE
        } else {
            iconContainer.visibility = View.INVISIBLE
            iconContainer.setOnClickListener(null)
        }
    }

    /**
     * Set icons size in pixels.
     */
    open fun setIconsSize(iconSize: Int) {
        iconsNumbers.forEach { icon ->
            setIconSize(icon, iconSize)
        }
        setIconSize(btnIconLeft, iconSize)
        setIconSize(btnIconRight, iconSize)
    }

    protected open fun setIconSize(iconImageView: ImageView, iconSize: Int) {
        iconImageView.layoutParams.apply {
            width = iconSize
            height = iconSize
        }
        iconImageView.requestLayout()
    }

    open fun setIconsContainersPaddings(
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        iconsContainers.forEach { container ->
            setIconContainerPaddings(container, leftPadding, topPadding, rightPadding, bottomPadding)
        }
        setIconContainerPaddings(btnContainerLeft, leftPadding, topPadding, rightPadding, bottomPadding)
        setIconContainerPaddings(btnContainerRight, leftPadding, topPadding, rightPadding, bottomPadding)
    }

    protected open fun setIconContainerPaddings(
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

    open fun setIconsSelectableBackground(value: Boolean) {
        iconsContainers.forEach { container ->
            setContainerSelectableBackground(container, value)
        }
        setContainerSelectableBackground(btnContainerLeft, value)
        setContainerSelectableBackground(btnContainerRight, value)
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

    open fun setDefaultNumpadIcons() {
        setNumbersIconsImageResources(defaultNumbersImages)
        setLeftIconImageResource(defaultLeftIconImage)
        setRightIconImageResource(defaultRightIconImage)
    }

    open fun enableNumpadView() {
        iconsContainers.forEach { container ->
            container.isClickable = true
        }
        btnContainerLeft.isClickable = true
        btnContainerRight.isClickable = true
    }

    open fun disableNumpadView() {
        iconsContainers.forEach { container ->
            container.isClickable = false
        }
        btnContainerLeft.isClickable = false
        btnContainerRight.isClickable = false
    }

    /*endregion ################### All Icons ######################*/

    /*region ################### Number Icons ######################*/

    /**
     * Set color for numbers icons. Color is set once and doesn't save.
     * So at first you should set icons drawable and then change color.
     */
    open fun setNumbersIconsColor(color: Int) {
        if (color != -1) {
            iconsNumbers.forEach { num ->
                setIconColor(num, color)
            }
        }
    }

    open fun setNumbersIconsColorResource(@ColorRes color: Int) {
        if (color != -1) {
            iconsNumbers.forEach { num ->
                setIconColorRes(num, color)
            }
        }
    }

    protected open fun setIconColor(iconImageView: ImageView, color: Int) {
        ImageViewCompat.setImageTintList(iconImageView, ColorStateList.valueOf(color))
    }

    protected open fun setIconColorRes(iconImageView: ImageView, color: Int) {
        ImageViewCompat.setImageTintList(
            iconImageView,
            ContextCompat.getColorStateList(context, color)
        )
    }

    open fun setNumbersIconsDrawable(drawableList: List<Drawable?>) {
        if (iconsNumbers.size == drawableList.size) {
            drawableList.forEachIndexed { index, drawable ->
                if (drawable != null)
                    setIconDrawable(iconsNumbers[index], drawable)
                else
                    setIconImageResource(iconsNumbers[index], defaultNumbersImages[index])
            }
        } else {
            throw IndexOutOfBoundsException("Amount of drawables is not the same as amount of items in iconsNumbers (${drawableList.size} instead of ${iconsNumbers.size}).")
        }
    }

    open fun setNumbersIconsImageResources(imageResourcesList: List<Int?>) {
        if (iconsNumbers.size == imageResourcesList.size) {
            imageResourcesList.forEachIndexed { index, image ->
                val imageRes = image ?: defaultNumbersImages[index]
                setIconImageResource(iconsNumbers[index], imageRes)
            }
        } else {
            throw IndexOutOfBoundsException("Amount of images is not the same as amount of items in iconsNumbers (${imageResourcesList.size} instead of ${iconsNumbers.size}).")
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

    /*endregion ################### Number Icons ######################*/

    /*region ################### Left Image Icons ######################*/

    open fun setLeftIconVisibility(value: Boolean) {
        setIconVisibility(btnContainerLeft, value)
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
        if (color != -1)
            setIconColorRes(btnIconLeft, color)
    }

    /*endregion ################### Left Image Icons ######################*/

    /*region ################### Right Image Icons ######################*/

    open fun setRightIconVisibility(value: Boolean) {
        setIconVisibility(btnContainerRight, value)
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
        if (color != -1)
            setIconColorRes(btnIconRight, color)
    }

    /*endregion ################### Right Image Icons ######################*/

    /*region ################### Handler ######################*/

    open fun setNumpadHandler(numpadHandler: INumPadHandler) {
        handler = numpadHandler
    }

    protected open fun setupNumpadHandler() {
        iconsContainers.forEach { container ->
            container.setOnClickListener {
                handler?.onNumClick(iconsContainers.indexOf(container))
            }
        }
        btnContainerLeft.setOnClickListener {
            handler?.onLeftIconClick()
        }
        btnContainerRight.setOnClickListener {
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
        setIconsContainersPaddings(
            defaultContainerPadding,
            defaultContainerPadding,
            defaultContainerPadding,
            defaultContainerPadding
        )
        setupNumpadHandler()
        enableNumpadView()
    }

    fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumPadView)

        val containerPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.NumPadView_numpadContainerPadding,
                defaultContainerPadding
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
            typedArray.getResourceId(R.styleable.NumPadView_numpadNumbersIconsColor, -1)

        val iconLeftImage = typedArray.getDrawable(R.styleable.NumPadView_numpadLeftDrawable)
        val iconLeftColor = typedArray.getResourceId(R.styleable.NumPadView_numpadLeftIconColor, -1)
        val iconLeftVisibility =
            typedArray.getBoolean(R.styleable.NumPadView_numpadLeftIconVisibitity, true)

        val iconRightImage = typedArray.getDrawable(R.styleable.NumPadView_numpadRightDrawable)
        val iconRightColor = typedArray.getResourceId(R.styleable.NumPadView_numpadLeftIconColor, -1)
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
        setNumbersIconsColorResource(iconsNumbersColor)
        setIconsSize(iconSize)
        setIconsContainersPaddings(
            containerPadding,
            containerPadding,
            containerPadding,
            containerPadding
        )

        setLeftIconDrawable(iconLeftImage)
        setLeftIconColorResource(iconLeftColor)
        setLeftIconVisibility(iconLeftVisibility)

        setRightIconDrawable(iconRightImage)
        setRightIconColorResource(iconRightColor)
        setRightIconVisibility(iconRightVisibility)
        setupNumpadHandler()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    /*endregion ################### Other ######################*/
}
