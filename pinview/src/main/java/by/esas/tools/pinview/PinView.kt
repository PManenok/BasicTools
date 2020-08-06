package by.esas.tools.pinview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView


class PinView : LinearLayout {
    val TAG: String = PinView::class.java.simpleName
    private var lastUnFilledIndex: Int = 0
    private val listImages: ArrayList<AppCompatImageView> = arrayListOf()
    private var animInDuration: Long = 200L
    private var animOutDuration: Long = 200L
    private val animInRes: Int = android.R.anim.fade_in
    private val animOutRes: Int = android.R.anim.fade_out

    private var pinsAmount: Int = 0
    private var filledRes: Int = R.drawable.ic_pin_filled_24dp
    private var unfilledRes: Int = R.drawable.ic_pin_unfilled_24dp

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
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinView)
        // Pins Amount
        pinsAmount = typedArray.getInt(R.styleable.PinView_pinAmount, 0)

        animInDuration = typedArray.getInt(R.styleable.PinView_pinInAnimationDuration, 200).toLong()
        animOutDuration = typedArray.getInt(R.styleable.PinView_pinOutAnimationDuration, 200).toLong()

        filledRes = typedArray.getResourceId(R.styleable.PinView_pinFilledIcon, R.drawable.ic_pin_filled_24dp)

        unfilledRes = typedArray.getResourceId(R.styleable.PinView_pinUnfilledIcon, R.drawable.ic_pin_unfilled_24dp)
        typedArray.recycle()

        addPins()
    }

    private fun addPins() {
        for (ind in 0 until pinsAmount) {
            val pin = AppCompatImageView(context)
            pin.setImageResource(unfilledRes)
            val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val padding: Int = resources.getDimension(R.dimen.pin_icon_padding).toInt()
            pin.setPadding(padding, padding, padding, padding)
            addView(pin, params)
            listImages.add(pin)
        }
    }

    private fun animate(pin: AppCompatImageView, imageRes: Int) {
        val animationOut = AnimationUtils.loadAnimation(context, animOutRes)
        animationOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                pin.setImageResource(imageRes)
                val animationIn = AnimationUtils.loadAnimation(context, animInRes)
                animationIn.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {}
                })
                animationIn.duration = animInDuration
                pin.startAnimation(animationIn)
            }
        })
        animationOut.duration = animOutDuration
        pin.startAnimation(animationOut)
    }

    fun setAnimInDuration(value: Long) {
        animInDuration = value
    }

    fun setAnimOutDuration(value: Long) {
        animOutDuration = value
    }

    fun setAnimInDuration(value: Int) {
        animInDuration = value.toLong()
    }

    fun setAnimOutDuration(value: Int) {
        animOutDuration = value.toLong()
    }

    fun fillPin() {
        animate(listImages[lastUnFilledIndex], filledRes)
        lastUnFilledIndex++
    }

    fun unFillPin() {
        lastUnFilledIndex--
        animate(listImages[lastUnFilledIndex], unfilledRes)
    }

    fun clearPins() {
        if (lastUnFilledIndex > 0) {
            while (lastUnFilledIndex > 0) {
                lastUnFilledIndex--
                animate(listImages[lastUnFilledIndex], unfilledRes)
            }
        } else {
            listImages.forEach { image ->
                animate(image, unfilledRes)
            }
        }
    }
}