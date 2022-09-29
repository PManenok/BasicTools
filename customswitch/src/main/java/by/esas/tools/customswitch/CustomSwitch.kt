package by.esas.tools.customswitch

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import by.esas.tools.util.SwitchManager
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

/**
 * This CustomSwitch view is used when it is necessary to switch to the
 * opposite state - yes/no, on/off, open/close.
 * It consists of [switcher] (android SwitchMaterial) and [textInfoView] (MaterialTextView)
 * for showing more information about switcher and its functionality.
 *
 * You can change switcher style in XML or programmatically. To change switcher's style in XML
 * override [R.styleable.CustomSwitch] attributes.
 *
 * For changing switcher title programmatically use [setTitle] method. Also you can set
 * your title style via [setTitleStyle].
 *
 * Use [setSwitchThumbTint] and [setSwitchTrackTint] methods to change thumb and track
 * switcher's color. If you don't add your color CustomSwitch will use
 * default colors([defaultThumbColor], [defaultTrackColor]). You can set paddings for switcher
 * via [setSwitchPaddings], by default paddings are 0dp. Also you can
 * get/set switcher's isChecked state via [switcherIsChecked] methods.
 *
 * To set text to [textInfoView] use [setInfo] method. You can change the style of this text
 * via [setInfoStyle], alignment via [setInfoAlignment], paddings via [setInfoPaddings].
 *
 * Also this view has a [ISwitchHandler] for handling changing status of switcher. Set [handler]
 * via [setSwitchHandler] by default [handler] is null.
 *
 * CustomSwitch implements [SwitchManager.ISwitchView] interface. On switchOn method
 * CustomSwitch becomes enabled and on switchOff becomes disabled(see [setEnabled]).
 */

open class CustomSwitch : LinearLayout, SwitchManager.ISwitchView {
    val TAG: String = CustomSwitch::class.java.simpleName
    protected val switcher: SwitchMaterial
    protected val textInfoView: MaterialTextView

    protected open var handler: ISwitchHandler? = null
    protected val defaultPadding = 0

    protected val defaultTrackColor = R.color.default_switch_selector
    protected val defaultThumbColor = R.color.white

    init {
        val view = inflate(context, R.layout.v_switch, this)
        switcher = view.findViewById(R.id.v_switch_switcher)
        textInfoView = view.findViewById(R.id.v_switch_info)
        super.setOrientation(VERTICAL)
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

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(attrs)
    }
    /*endregion ################### Constructors ######################*/

    /*region ################### ISwitchView Interface ######################*/
    override fun switchOn() {
        setEnabled(true)
    }

    override fun switchOff() {
        setEnabled(false)
    }
    /*endregion ################### ISwitchView Interface ######################*/

    /*region ################### Switcher Settings ######################*/
    open fun switcherIsChecked(): Boolean {
        return switcher.isChecked
    }

    open fun switcherIsChecked(value: Boolean) {
        switcher.isChecked = value
    }

    open fun setSwitchHandler(switchHandler: ISwitchHandler) {
        handler = switchHandler
    }

    open fun setSwitchTrackTint(@ColorRes color: Int) {
        switcher.trackTintList = ContextCompat.getColorStateList(context, color)
    }

    open fun setSwitchThumbTint(@ColorRes color: Int) {
        switcher.thumbTintList = ContextCompat.getColorStateList(context, color)
    }

    open fun setSwitchPaddings(
        startPadding: Int,
        topPadding: Int,
        endPadding: Int,
        bottomPadding: Int
    ) {
        switcher.setPadding(startPadding, topPadding, endPadding, bottomPadding)
    }
    /*endregion ################### Switcher Settings ######################*/

    /*region ################### Switcher Title ######################*/
    open fun setTitleStyle(titleStyleId: Int) {
        if (titleStyleId != -1)
            TextViewCompat.setTextAppearance(switcher, titleStyleId)
    }

    open fun setTitle(text: String) {
        if (switcher.text.toString() != text)
            switcher.text = text
    }

    open fun getTitle(): String {
        return switcher.text.toString()
    }
    /*endregion ################### Switcher Title ######################*/

    /*region ################### Info View Title ######################*/

    open fun setInfo(text: String?) {
        if (text.isNullOrBlank()) {
            textInfoView.visibility = View.GONE
        } else {
            if (textInfoView.text.toString() != text) {
                textInfoView.text = text
            }
            textInfoView.visibility = View.VISIBLE
        }
    }

    open fun getInfo(): String {
        return textInfoView.text.toString()
    }

    open fun setInfoStyle(styleId: Int) {
        if (styleId != -1)
            TextViewCompat.setTextAppearance(textInfoView, styleId)
    }

    open fun setInfoAlignment(infoAlignment: Int) {
        textInfoView.textAlignment = infoAlignment
    }

    open fun setInfoPaddings(
        startPadding: Int,
        topPadding: Int,
        endPadding: Int,
        bottomPadding: Int
    ) {
        textInfoView.setPadding(startPadding, topPadding, endPadding, bottomPadding)
    }
    /*endregion ################### Info View Title ######################*/

    /*region ################### Switch View Settings ######################*/

    override fun isEnabled(): Boolean {
        return switcher.isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        switcher.isEnabled = enabled
    }

    open fun setDefaultValue() {
        setSwitchPaddings(defaultPadding, defaultPadding, defaultPadding, defaultPadding)
        setInfoPaddings(defaultPadding, defaultPadding, defaultPadding, defaultPadding)
        setInfoAlignment(TEXT_ALIGNMENT_INHERIT)

        switcher.apply {
            thumbTintList = ContextCompat.getColorStateList(context, defaultThumbColor)
            trackTintList = ContextCompat.getColorStateList(context, defaultTrackColor)
        }
        setupSwitchCheckedListener()
    }

    /*  Initialize attributes from XML file  */
    protected fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSwitch)

        val title = typedArray.getString(R.styleable.CustomSwitch_switchTitle)
        val titleStyleId: Int =
            typedArray.getResourceId(R.styleable.CustomSwitch_switchTitleTextAppearance, -1)

        val info = typedArray.getString(R.styleable.CustomSwitch_switchInfo)
        val infoAlignment =
            typedArray.getInt(R.styleable.CustomSwitch_switchInfoAlignment, TEXT_ALIGNMENT_INHERIT)
        val infoStyleId: Int =
            typedArray.getResourceId(R.styleable.CustomSwitch_switchInfoTextAppearance, -1)

        val startPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.CustomSwitch_switchTitlePaddingStart,
                defaultPadding
            )
        val topPadding = typedArray.getDimensionPixelSize(
            R.styleable.CustomSwitch_switchTitlePaddingTop,
            defaultPadding
        )
        val endPadding = typedArray.getDimensionPixelSize(
            R.styleable.CustomSwitch_switchTitlePaddingEnd,
            defaultPadding
        )
        val bottomPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.CustomSwitch_switchTitlePaddingBottom,
                defaultPadding
            )

        val infoStartPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.CustomSwitch_switchInfoPaddingStart,
                defaultPadding
            )
        val infoTopPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.CustomSwitch_switchInfoPaddingTop,
                defaultPadding
            )
        val infoEndPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.CustomSwitch_switchInfoPaddingEnd,
                defaultPadding
            )
        val infoBottomPadding =
            typedArray.getDimensionPixelSize(
                R.styleable.CustomSwitch_switchInfoPaddingBottom,
                defaultPadding
            )

        /*##########  Switcher Icon  ##########*/
        val thumbTint =
            typedArray.getResourceId(R.styleable.CustomSwitch_thumbTint, defaultThumbColor)
        val trackTint = typedArray.getResourceId(R.styleable.CustomSwitch_trackTint, defaultTrackColor)

        typedArray.recycle()

        switcher.apply {
            text = title ?: ""
            if (titleStyleId != -1)
                TextViewCompat.setTextAppearance(switcher, titleStyleId)
            setPadding(
                paddingLeft + startPadding,
                paddingTop + topPadding,
                paddingRight + endPadding,
                paddingBottom + bottomPadding
            )
            thumbTintList = ContextCompat.getColorStateList(context, thumbTint)
            trackTintList = ContextCompat.getColorStateList(context, trackTint)
        }
        setupSwitchCheckedListener()

        textInfoView.apply {
            text = info ?: ""
            if (infoStyleId != -1)
                TextViewCompat.setTextAppearance(textInfoView, infoStyleId)
            textAlignment = infoAlignment
            setPadding(
                paddingLeft + infoStartPadding,
                paddingTop + infoTopPadding,
                paddingRight + infoEndPadding,
                paddingBottom + infoBottomPadding
            )
        }
    }

    protected open fun setupSwitchCheckedListener() {
        switcher.setOnCheckedChangeListener { _, isChecked ->
            val preparingResult = doPreparingForSwitch(isChecked)
            preparingResult?.let { preparing ->
                if (preparing)
                    handler?.onSwitchChange(isChecked)
                else
                    switcher.toggle()
            }
        }
    }

    protected open fun doPreparingForSwitch(isChecked: Boolean): Boolean? {
        handler?.let { switchHandler ->
            return when (isChecked) {
                true -> switchHandler.prepareToSwitchOn()
                false -> switchHandler.prepareToSwitchOff()
            }
        }
        return null
    }
    /*region ################### Switch View Settings ######################*/
}
