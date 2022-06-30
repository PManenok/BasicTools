package com.example.numpad

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

open class NumPadImageView : ConstraintLayout {
    open val TAG: String = NumPadImageView::class.java.simpleName

    val numContainerZero: FrameLayout
    val numContainerOne: FrameLayout
    val numContainerTwo: FrameLayout
    val numContainerThree: FrameLayout
    val numContainerFour: FrameLayout
    val numContainerFive: FrameLayout
    val numContainerSix: FrameLayout
    val numContainerSeven: FrameLayout
    val numContainerEight: FrameLayout
    val numContainerNine: FrameLayout
    val butContainerLeft: FrameLayout
    val butContainerRight: FrameLayout

    val numIconZero: AppCompatImageView
    val numIconOne: AppCompatImageView
    val numIconTwo: AppCompatImageView
    val numIconThree: AppCompatImageView
    val numIconFour: AppCompatImageView
    val numIconFive: AppCompatImageView
    val numIconSix: AppCompatImageView
    val numIconSeven: AppCompatImageView
    val numIconEight: AppCompatImageView
    val numIconNine: AppCompatImageView
    val butIconLeft: AppCompatImageView
    val butIconRight: AppCompatImageView

    val iconsContainersList = ArrayList<FrameLayout>()
    val iconsNumbersList = ArrayList<AppCompatImageView>()

    val defaultIconSize: Int = 24
    val defaultIconPadding = 0
    val defaultNumbersImages = listOf(
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
    val defaultRightIconImage = R.drawable.ic_backspace
    val defaultLeftIconImage = R.drawable.ic_cancel
    val defaultHandler = object : INumPadHandler{
        override fun onNumClick(num: Int) {
        }
    }
    var handler = defaultHandler

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
        butIconLeft = view.findViewById(R.id.v_num_pad_icon_left)
        butIconRight = view.findViewById(R.id.v_num_pad_icon_right)

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

    open fun setNumpadHandler(numpadHandler: INumPadHandler){
        handler = numpadHandler
    }

    protected open fun setupNumpadHandler(){
        for (i in iconsContainersList){
            i.setOnClickListener {
                handler.onNumClick(iconsContainersList.indexOf(i))
            }
        }
        butContainerLeft.setOnClickListener {
            handler.onLeftIconClick()
        }
        butContainerRight.setOnClickListener {
            handler.onRightClick()
        }
    }

    protected open fun setIconClickListener(
        iconContainer: FrameLayout,
        listener: OnClickListener?
    ) {
        iconContainer.setOnClickListener(listener)
    }

    open fun setIconsSize(iconSize: Int) {
        for (i in iconsNumbersList) {
            setIconSize(i, iconSize)
        }
        setIconSize(butIconLeft, iconSize)
        setIconSize(butIconRight, iconSize)
    }

    protected open fun setIconSize(iconImageView: ImageView, iconSize: Int) {
        iconImageView.layoutParams.apply {
            width = dpToPx(iconSize)
            height = dpToPx(iconSize)
        }
        iconImageView.requestLayout()
    }

    open fun setIconsMargins(leftMargin: Int, topMargin: Int, rightMargin: Int, bottomMargin: Int) {
        for (i in iconsNumbersList) {
            setIconViewMargins(i, leftMargin, topMargin, rightMargin, bottomMargin)
        }
        setIconViewMargins(butIconLeft, leftMargin, topMargin, rightMargin, bottomMargin)
        setIconViewMargins(butIconRight, leftMargin, topMargin, rightMargin, bottomMargin)
    }

    protected open fun setIconViewMargins(
        iconImageView: ImageView,
        leftMargin: Int,
        topMargin: Int,
        rightMargin: Int,
        bottomMargin: Int
    ) {
        (iconImageView.layoutParams as LayoutParams).apply {
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
        for (i in iconsNumbersList) {
            setIconViewPaddings(i, leftPadding, topPadding, rightPadding, bottomPadding)
        }
        setIconViewPaddings(butIconLeft, leftPadding, topPadding, rightPadding, bottomPadding)
        setIconViewPaddings(butIconRight, leftPadding, topPadding, rightPadding, bottomPadding)
    }

    protected open fun setIconViewPaddings(
        iconImageView: ImageView,
        leftPadding: Int,
        topPadding: Int,
        rightPadding: Int,
        bottomPadding: Int
    ) {
        (iconImageView.layoutParams as LayoutParams).apply {
            setPadding(
                dpToPx(leftPadding),
                dpToPx(topPadding),
                dpToPx(rightPadding),
                dpToPx(bottomPadding)
            )
        }
    }

    open fun setIconsSelectableBackground(value: Boolean) {
        for (i in iconsContainersList) {
            setContainerSelectableBackground(i, value)
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

    open fun enableNumpadView(){
        for (i in iconsContainersList){
            i.isClickable = true
        }
        butContainerLeft.isClickable = true
        butContainerRight.isClickable = true
    }

    open fun disableNumpadView(){
        for (i in iconsContainersList){
            i.isClickable = false
        }
        butContainerLeft.isClickable = false
        butContainerRight.isClickable = false
    }
    /*endregion ################### All Icons ######################*/

    /*region ################### Containers Number Icons ######################*/
    open fun setNumbersClickListener(listener: View.OnClickListener?) {
        for (i in iconsContainersList) {
            setIconClickListener(i, listener)
        }
    }
    /*endregion ################### Containers Number Icons ######################*/

    /*region ################### Number Icons ######################*/
    open fun setNumbersIconsColor(color: Int) {
        for (i in iconsNumbersList) {
            setNumberIconColor(i, color)
        }
    }

    protected open fun setNumberIconColor(iconImageView: ImageView, color: Int) {
        val unwrappedIconDrawable = iconImageView.drawable
        val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedIconDrawable)
        DrawableCompat.setTint(wrappedDrawable, color)
    }

    open fun setNumbersIconsDrawable(drawableList: List<Drawable?>) {
        for (i in drawableList.indices) {
            setIconDrawable(iconsNumbersList[i], drawableList[i])
        }
    }

    open fun setNumbersIconsImageResources(imageResourcesList: List<Int?>) {
        for (i in imageResourcesList.indices) {
            setIconImageResource(iconsNumbersList[i], imageResourcesList[i])
        }
    }

    protected open fun setIconDrawable(iconImageView: ImageView, drawable: Drawable?) {
        if (drawable != null)
            iconImageView.setImageDrawable(drawable)
    }

    protected open fun setIconImageResource(iconImageView: ImageView, imageResource: Int?) {
        if (imageResource != null) {
            iconImageView.setImageResource(imageResource)
        }
    }

    protected open fun setDefaultNumbersIconsImages() {
        setNumbersIconsImageResources(defaultNumbersImages)
    }
    /*endregion ################### Number Icons ######################*/

    /*region ################### Left Image Icons ######################*/
    open fun setLeftIconVisibility(value: Boolean) {
        setIconVisibility(butContainerLeft, value)
    }

    open fun setLeftIconClickListener(listener: OnClickListener?) {
        setIconClickListener(butContainerLeft, listener)
    }

    open fun setLeftIconImage(drawable: Drawable?) {
        if (drawable != null){
            butIconLeft.setImageDrawable(drawable)
        } else {
            setLeftIconVisibility(false)
        }
    }

    open fun setLeftIconImage(imageResource: Int?) {
        if (imageResource != null) {
            butIconLeft.setImageResource(imageResource)
        } else {
            setLeftIconVisibility(false)
        }
    }
    /*endregion ################### Left Image Icons ######################*/

    /*region ################### Right Image Icons ######################*/
    open fun setRightIconVisibility(value: Boolean) {
        setIconVisibility(butContainerRight, value)
    }

    open fun setRightIconClickListener(listener: OnClickListener?) {
        setIconClickListener(butContainerRight, listener)
    }

    open fun setRightIconImage(drawable: Drawable?) {
        if (drawable != null)
            butIconRight.setImageDrawable(drawable)
        else
            setRightIconVisibility(false)
    }

    open fun setRightIconImage(resId: Int?) {
        if (resId != null)
            butIconRight.setImageResource(resId)
        else
            setRightIconVisibility(false)
    }
    /*endregion ################### Right Image Icons ######################*/

    /*region ################### Other ######################*/
    fun setDefaults(){
        setDefaultNumbersIconsImages()
        setIconsSize(defaultIconSize)
        setIconsPaddings(defaultIconPadding, defaultIconPadding, defaultIconPadding, defaultIconPadding)
        setLeftIconImage(defaultLeftIconImage)
        setRightIconImage(defaultRightIconImage)

        enableNumpadView()
    }

    fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumPadView)

        val iconPadding =
            typedArray.getDimensionPixelSize(R.styleable.NumPadView_numpadIconPadding, 0)
        val iconSize = typedArray.getDimensionPixelSize(R.styleable.NumPadView_numpadIconSize, defaultIconSize)

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

        val iconLeftImage = typedArray.getDrawable(R.styleable.NumPadView_numpadLeftDrawable)
        val iconRightImage = typedArray.getDrawable(R.styleable.NumPadView_numpadRightDrawable)

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
        setIconsSize(iconSize)
        setIconsPaddings(iconPadding, iconPadding, iconPadding, iconPadding)

        setLeftIconImage(iconLeftImage)
        setRightIconImage(iconRightImage)
        setupNumpadHandler()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
    /*endregion ################### Other ######################*/
}

