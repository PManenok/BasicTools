/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.pinview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat

/**
 * PinView is a custom view for showing process of adding/deleting/clearing values entered by
 * the user. The most common use case is entering a pincode. User sees unfilled cells on the screen,
 * the number of which means how many numbers he needs to enter
 * and when he enters a numbers cells become filled.
 *
 * This PinView is a LinearLayout with image cells which are horizontally arranged.
 *
 * Use [setPinAmount] method to set amount of cells programmatically or set it in XML,
 * by default this amount is 0. One cell can be in 2 states - filled and unfilled.
 * For each state you can set your drawable resource in xml. If you don't set your images the view
 * will set default images: [filledRes], [unfilledRes]. Also you can change color, paddings,
 * size of filled and unfilled images and margins between them.
 *
 * Use [fillPin] method to make one cell filled, [unFillPin] to make one cell unfilled,
 * [clearPins] to make all cells unfilled. You can set/change animation time of making cells
 * filled/unfilled via [setAnimInDuration]/[setAnimOutDuration].
 */

open class PinView : LinearLayout {
    val TAG: String = PinView::class.java.simpleName
    protected var lastUnFilledIndex: Int = 0
    protected val listImages: ArrayList<AppCompatImageView> = arrayListOf()
    protected open var animInDurationMillis: Long = 200L
    protected open var animOutDurationMillis: Long = 200L
    protected open val animInRes: Int = android.R.anim.fade_in
    protected open val animOutRes: Int = android.R.anim.fade_out

    protected open var pinsAmount: Int = 0
    protected open var filledRes: Int = R.drawable.ic_pin_filled_24dp
    protected open var unfilledRes: Int = R.drawable.ic_pin_unfilled_24dp

    protected open var filledTint: Int = R.color.colorPrimary
    protected open var unfilledTint: Int = R.color.colorPrimary

    protected open var filledPadding: Int = context.resources.getDimensionPixelSize(R.dimen.pin_filled_padding)
    protected open var unfilledPadding: Int = context.resources.getDimensionPixelSize(R.dimen.pin_unfilled_padding)
    protected open var betweenMargin: Int = context.resources.getDimensionPixelSize(R.dimen.pin_icon_margin)

    protected open var iconSize: Int = ViewGroup.LayoutParams.WRAP_CONTENT

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    init {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }

    /*  Initialize attributes from XML file  */
    protected open fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinView)
        // Pins Amount
        pinsAmount = typedArray.getInt(R.styleable.PinView_pinAmount, 0)

        val filledAmount = typedArray.getInt(R.styleable.PinView_pinFilledAmount, 0)

        animInDurationMillis = typedArray.getInt(R.styleable.PinView_pinInAnimationDuration, 200).toLong()
        animOutDurationMillis = typedArray.getInt(R.styleable.PinView_pinOutAnimationDuration, 200).toLong()

        filledRes = typedArray.getResourceId(R.styleable.PinView_pinFilledIcon, R.drawable.ic_pin_filled_24dp)

        unfilledRes = typedArray.getResourceId(R.styleable.PinView_pinUnfilledIcon, R.drawable.ic_pin_unfilled_24dp)

        filledTint = typedArray.getResourceId(R.styleable.PinView_pinFilledTint, filledTint)
        unfilledTint = typedArray.getResourceId(R.styleable.PinView_pinUnfilledTint, unfilledTint)

        filledPadding = typedArray.getDimensionPixelSize(R.styleable.PinView_pinFilledPadding, filledPadding)
        unfilledPadding = typedArray.getDimensionPixelSize(R.styleable.PinView_pinUnfilledPadding, unfilledPadding)
        betweenMargin = typedArray.getDimensionPixelSize(R.styleable.PinView_pinBetweenMargin, betweenMargin)

        iconSize = typedArray.getDimensionPixelSize(R.styleable.PinView_pinIconSize, iconSize)

        typedArray.recycle()

        addPins()
        setPinWithoutAnimation(filledAmount)
    }

    open fun setPinAmount(pinsAmount: Int, filledAmount: Int = 0) {
        this.pinsAmount = pinsAmount
        addPins()
        setPinWithoutAnimation(filledAmount)
    }

    protected open fun addPins() {
        for (ind in 0 until pinsAmount) {
            val pin = AppCompatImageView(context)
            pin.setImageResource(unfilledRes)
            ImageViewCompat.setImageTintList(pin, ContextCompat.getColorStateList(context, unfilledTint))
            val params = LayoutParams(iconSize, iconSize)
            pin.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            val height = if (iconSize > 0) iconSize else pin.measuredHeight
            params.setMargins(betweenMargin, 0, betweenMargin, 0)
            params.height = height
            pin.setPadding(0, unfilledPadding, 0, unfilledPadding)
            addView(pin, params)
            listImages.add(pin)
        }
    }

    protected open fun animate(pin: AppCompatImageView, imageRes: Int, dimensionValue: Int, tint: Int) {
        val animationOut = AnimationUtils.loadAnimation(context, animOutRes)
        animationOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                pin.setImageResource(imageRes)
                val padding: Int = dimensionValue
                ImageViewCompat.setImageTintList(pin, ContextCompat.getColorStateList(context, tint))
                pin.setPadding(0, padding, 0, padding)
                val animationIn = AnimationUtils.loadAnimation(context, animInRes)
                animationIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {}
                })
                animationIn.duration = animInDurationMillis
                pin.startAnimation(animationIn)
            }
        })
        animationOut.duration = animOutDurationMillis
        pin.startAnimation(animationOut)
    }

    open fun setPin(pinLength: Int) {
        while (lastUnFilledIndex < pinLength) {
            fillPin()
        }
        while (lastUnFilledIndex > pinLength) {
            unFillPin()
        }
    }

    open fun setPinWithoutAnimation(pinLength: Int) {
        while (lastUnFilledIndex < pinLength) {
            val pin = listImages[lastUnFilledIndex]
            pin.setImageResource(filledRes)
            ImageViewCompat.setImageTintList(pin, ContextCompat.getColorStateList(context, filledTint))
            pin.setPadding(0, filledPadding, 0, filledPadding)
            lastUnFilledIndex++
        }
        while (lastUnFilledIndex > pinLength) {
            lastUnFilledIndex--
            val pin = listImages[lastUnFilledIndex]
            pin.setImageResource(unfilledRes)
            ImageViewCompat.setImageTintList(pin, ContextCompat.getColorStateList(context, unfilledTint))
            pin.setPadding(0, unfilledPadding, 0, unfilledPadding)
        }
    }

    open fun setAnimInDuration(value: Long) {
        animInDurationMillis = value
    }

    open fun setAnimOutDuration(value: Long) {
        animOutDurationMillis = value
    }

    open fun setAnimInDuration(value: Int) {
        animInDurationMillis = value.toLong()
    }

    open fun setAnimOutDuration(value: Int) {
        animOutDurationMillis = value.toLong()
    }

    open fun fillPin() {
        if (lastUnFilledIndex != pinsAmount) {
            animate(listImages[lastUnFilledIndex], filledRes, filledPadding, filledTint)
            lastUnFilledIndex++
        }
    }

    open fun unFillPin() {
        if (lastUnFilledIndex != 0) {
            lastUnFilledIndex--
            animate(listImages[lastUnFilledIndex], unfilledRes, unfilledPadding, unfilledTint)
        }
    }

    open fun clearPins() {
        if (lastUnFilledIndex > 0) {
            while (lastUnFilledIndex > 0) {
                lastUnFilledIndex--
                animate(listImages[lastUnFilledIndex], unfilledRes, unfilledPadding, unfilledTint)
            }
        } else {
            listImages.forEach { image ->
                animate(image, unfilledRes, unfilledPadding, unfilledTint)
            }
        }
    }
}