/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.inputfieldview

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.KeyListener
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import by.esas.tools.util_ui.SwitchManager
import kotlin.math.roundToInt

open class InputFieldView : ConstraintLayout, by.esas.tools.util_ui.SwitchManager.ISwitchView {

    open val TAG: String = InputFieldView::class.java.simpleName

    val STATE_ERROR = intArrayOf(R.attr.state_error)

    companion object {

        const val END_ICON_CUSTOM: Int = -1
        const val END_ICON_NONE: Int = 0
        const val END_ICON_PASSWORD_TOGGLE: Int = 1
        const val END_ICON_CLEAR_TEXT: Int = 2
        const val END_ICON_CHECKABLE: Int = 3
        const val END_ICON_ERROR: Int = 4
        const val END_ICON_TEXT: Int = 5

        const val START_ICON_CUSTOM: Int = -1
        const val START_ICON_NONE: Int = 0
        const val START_ICON_DEFAULT: Int = 1
        const val START_ICON_CHECKABLE: Int = 2
        const val START_ICON_PROGRESS: Int = 3

        const val LABEL_TYPE_ON_TOP: Int = 0
        const val LABEL_TYPE_ON_LINE: Int = 1
        const val LABEL_TYPE_HIDE: Int = 2
        const val LABEL_TYPE_ON_TOP_MULTI: Int = 3
        const val LABEL_TYPE_ON_LINE_MULTI: Int = 4
        const val LABEL_TYPE_ON_TOP_NO_START: Int = 5
    }

    /*region ################ Views ################*/

    var inputText: EditText? = null
    var prefixTextView: TextView? = null
    var errorTextView: TextView? = null
    var startIconView: AppCompatImageView? = null
    var startCheckBox: AppCompatCheckBox? = null
    var endIconView: AppCompatImageView? = null
    var endCheckBox: AppCompatCheckBox? = null
    var endText: TextView? = null
    var inputClickView: View? = null
    var inputBox: BoxView? = null

    protected var labelText: TextView? = null
    protected var editTextContainer: ViewGroup? = null
    protected var inputContainer: ViewGroup? = null
    protected var bottomContainer: FrameLayout? = null
    protected var helpTextView: TextView? = null
    protected var startContainer: FrameLayout? = null
    protected var progressBar: ProgressBar? = null
    protected var endContainer: FrameLayout? = null

    /*endregion ################ Views END ################*/

    /*region ################ Parameters ################*/

    protected open val inflateLayoutRes: Int = R.layout.v_input_field
    protected open var labelStartMargin: Int = 0
    protected open var labelStartPadding: Int = 0
    protected open var hideErrorText: Boolean = true

    /**
     * A variable shows whether the [inputText] will be enable or disable all the time.
     * If [inputIsEnableable] is false, then [inputText] will not be enabled
     * after the InputFieldView will be enabled in [enableView] method
     */
    protected open var inputIsEnableable: Boolean = true
    protected open var iconsSize: Int = 0

    //Default
    protected open val defaultErrorDrawableRes: Int = R.drawable.ic_input_field_error_24
    protected open val defaultStrokeColor: Int = R.color.color_box_stroke_selector
    protected open val defaultFocusedStrokeColor = R.color.colorStrokeOutlineDefault
    protected open val defaultBoxBgColor: Int = Color.TRANSPARENT
    protected open val defaultStrokeRadiusInPx: Int = dpToPx(4).toInt()
    protected open val defaultStrokeWidthInPx: Int = dpToPx(1).toInt()
    protected open val defaultTextStyle = R.style.AppTheme_TextAppearance
    protected open val defaultInputTextStyle = R.style.AppTheme_TextInputLayout_Default

    /**
     * A variable indicates whether the stroke and error colors will match on error mode by default
     */
    protected open val defaultPaddingTopInPx: Int = dpToPx(12).toInt()
    protected open val defaultPaddingBottomInPx: Int = dpToPx(12).toInt()
    protected open val defaultIsWrap: Boolean = false
    protected open val defaultHideErrorIcon: Boolean = false
    protected open val defaultCheckBoxToggle: Int = R.drawable.selector_input_filed_check_box_toggle
    protected open val defaultIconsTint = R.color.default_selector_color
    protected open val defaultInputType: Int = EditorInfo.TYPE_CLASS_TEXT
    protected open val defaultMinHeight: Int = context.resources
        .getDimensionPixelSize(R.dimen.input_edit_text_default_min_height)
    protected open val defaultIconsPadding: Int = dpToPx(4).roundToInt()
    protected open val defaultInputBoxVisibility: Int = View.VISIBLE

    protected open var errorDrawableRes: Int = R.drawable.ic_input_field_error_24
    protected open var strokeColor: Int = R.color.color_box_stroke_selector
    protected open var focusedStrokeColor: Int = R.color.colorStrokeOutlineDefault
    protected open var boxBgColor: Int = Color.TRANSPARENT
    protected open var strokeRadiusInPx: Float = dpToPx(4)
    protected open var strokeWidthInPx: Float = dpToPx(1)
    protected open var paddingTopInPx: Int = dpToPx(12).toInt()
    protected open var paddingBottomInPx: Int = dpToPx(12).toInt()

    /**
     * A variable indicates that inputText and editTextContainer have wrap content width
     */
    protected open var isWrap: Boolean = false
    protected open var hideErrorIcon: Boolean = false
    protected open var checkBoxToggle: Int = R.drawable.selector_input_filed_check_box_toggle
    protected open var editTextMinHeight: Int =
        context.resources.getDimensionPixelSize(R.dimen.input_edit_text_default_min_height)

    //Label
    protected open val defaultLabelMaxLines: Int = Integer.MAX_VALUE
    protected open val defaultLabelExtraTopMargin: Int = 0
    protected open val defaultLabelType: Int = LABEL_TYPE_ON_LINE
    protected var labelMaxLines: Int = Integer.MAX_VALUE
    protected var labelExtraTopMargin: Int = 0
    protected var currentLabelType: Int = LABEL_TYPE_ON_LINE
    protected val labelPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        return@OnPreDrawListener labelOnPreDraw()
    }
    protected var labelStyleId: Int = -1

    //StartIcon
    protected var startClickListener: IconClickListener? = null
    var startCheckedListener: IconCheckedListener? = null
    protected var startTint: Int = R.color.default_selector_color
    protected var startDrawable: Drawable? = null
    protected open val defaultStartIconMode: Int = START_ICON_NONE
    protected var beforeProgressMode: Int = START_ICON_NONE
    protected var startIconMode: Int = START_ICON_NONE

    //End icon
    protected var endClickListener: IconClickListener? = null
    var endCheckedListener: IconCheckedListener? = null
    protected open val defaultEndIconMode: Int = END_ICON_NONE
    protected open val defaultPasswordToggleRes: Int =
        R.drawable.selector_input_filed_password_toggle
    protected var endDrawable: Drawable? = null
    protected var endTint: Int = R.color.default_selector_color
    protected var passwordToggleRes: Int = R.drawable.selector_input_filed_password_toggle
    protected var previousEndIconMode: Int = END_ICON_NONE
    protected var endIconMode: Int = END_ICON_NONE

    //End Text
    protected var inputEndTextStyleId = R.style.AppTheme_TextInputLayout_Default

    //Bottom text
    protected open val defaultShowBottomContainer: Boolean = true
    protected var showBottomContainer: Boolean = false
    protected var hasErrorText: Boolean = false
    protected var hasHelpText: Boolean = false

    //ActionEditor
    protected var editorActionId = EditorInfo.IME_ACTION_DONE
    protected var editorActionListener: EditorActionListener? = null

    /*endregion ################ Parameters END ################*/

    /*region ############################ Icons Click Listeners ################################*/

    protected open val endCheckClickListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            endCheckedListener?.onCheckChanged(isChecked)
        }
    protected open val startCheckClickListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            startCheckedListener?.onCheckChanged(isChecked)
        }
    protected open val passwordClickListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            inputText?.apply {
                transformationMethod = if (isChecked) {
                    null
                } else {
                    PasswordTransformationMethod.getInstance()
                }
            }
            endCheckedListener?.onCheckChanged(isChecked)
        }

    /*endregion ############################ Icons Click Listeners End ################################*/

    /*region ############################ TextWatcher ################################*/

    protected open val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (hasErrorText && hideErrorText) {
                setError(null)
            }
            if (endIconMode == END_ICON_CLEAR_TEXT) {
                endIconView?.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.INVISIBLE
            } else if (endIconMode == END_ICON_ERROR && previousEndIconMode == END_ICON_CLEAR_TEXT) {
                if (s?.isNotEmpty() == true) {
                    setEndIconAsClear()
                } else {
                    setEndIconAsError(true)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    /*endregion ############################ TextWatcher End ################################*/

    /*region ############################ Constructors ################################*/

    constructor(context: Context) : super(context) {
        initialSetting()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialSetting()
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initialSetting()
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initialSetting()
        initAttrs(attrs)
    }

    /*endregion ############################ Constructors End ################################*/

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val state = super.onCreateDrawableState(extraSpace + 1)
        if (hasErrorText) {
            mergeDrawableStates(state, STATE_ERROR)
            setBoxStrokeStateColor(STATE_ERROR)
        } else setBoxStrokeDefaultStateColor()

        return state
    }

    /*region ############### ISwitchView interface methods ################*/

    override fun switchOn() {
        enableView()
    }

    override fun switchOff() {
        disableView()
    }

    /*endregion ############### ISwitchView interface methods ################*/

    /*region ############### Label settings ################*/

    open fun hideLabel() {
        labelText?.let { label ->
            label.background = null
            label.visibility = View.INVISIBLE
            (inputContainer?.layoutParams as LayoutParams?)?.apply {
                setMargins(leftMargin, 0, rightMargin, bottomMargin)
            }
        }
    }

    open fun showLabel() {
        if (currentLabelType == LABEL_TYPE_HIDE)
            currentLabelType = defaultLabelType
        updateLabelState()
    }

    open fun setLabelType(labelType: Int = defaultLabelType) {
        currentLabelType = labelType
        updateLabelState()
    }

    open fun setInputLabel(text: String?) {
        if (!text.isNullOrBlank()) {
            labelText?.let { label ->
                val isRTL: Boolean = resources.getBoolean(R.bool.input_is_rtl_direction)
                if (!(label.text?.toString() ?: "").equals(text)) {
                    if (!isInEditMode)
                        Log.i(TAG, "Is RTL direction $isRTL")
                    val hint = if (isRTL) "\u202B$text" else text
                    label.text = hint
                }
                if (label.visibility != View.VISIBLE || currentLabelType == LABEL_TYPE_ON_TOP_MULTI || currentLabelType == LABEL_TYPE_ON_LINE_MULTI)
                    updateLabelState()
            }
        } else
            hideLabel()
    }

    open fun setInputLabel(textId: Int?) {
        setInputLabel(if (textId != null && textId != -1) context.resources.getString(textId) else "")
    }

    open fun getInputLabel(): String {
        return labelText?.text?.toString() ?: ""
    }

    protected open fun updateLabelState() {
        labelText?.let { label ->
            when (currentLabelType) {
                LABEL_TYPE_ON_TOP, LABEL_TYPE_ON_TOP_MULTI, LABEL_TYPE_ON_TOP_NO_START -> {
                    label.maxLines =
                        if (currentLabelType != LABEL_TYPE_ON_TOP_MULTI) 1 else labelMaxLines
                    label.visibility = View.VISIBLE
                    //label.background = null
                    label.invalidate()
                }

                LABEL_TYPE_ON_LINE, LABEL_TYPE_ON_LINE_MULTI -> {
                    label.maxLines =
                        if (currentLabelType != LABEL_TYPE_ON_LINE_MULTI) 1 else labelMaxLines
                    label.visibility = View.VISIBLE
                    //label.setBackgroundColor(labelBg)
                    label.invalidate()
                }
                // as if default is LABEL_TYPE_HIDE
                else -> {
                    hideLabel()
                }
            }
        }
    }

    protected open fun setLabelColor(color: Int) {
        labelText?.setTextColor(color)
    }

    open fun setLabelStyle(styleId: Int) {
        if (styleId != -1) {
            labelStyleId = styleId
            labelText?.apply { TextViewCompat.setTextAppearance(this, labelStyleId) }
        }
    }

    /*endregion ############### Label settings END ################*/

    /*region ############### Prefix settings ################*/

    open fun setInputPrefix(prefix: String) {
        prefixTextView?.text = prefix
    }

    open fun getTextWithPrefix(): String {
        return prefixTextView?.text?.toString() ?: "" + inputText?.text?.toString() ?: ""
    }

    open fun setPrefixStyle(styleId: Int) {
        if (styleId != -1)
            prefixTextView?.apply { TextViewCompat.setTextAppearance(this, styleId) }
    }

    /*endregion ############### Prefix settings END ################*/

    /*region ############### Input settings ################*/

    open fun setText(text: String?) {
        if (!(inputText?.text?.toString() ?: "").equals(text ?: "")) {
            inputText?.setText(text)
        }
    }

    open fun getText(): String {
        return inputText?.text?.toString() ?: ""
    }

    open fun clearInputText() {
        inputText?.setText("")
        if (endIconMode == END_ICON_CLEAR_TEXT)
            endIconView?.visibility = View.INVISIBLE
    }

    open fun setInputIsEnableableValue(value: Boolean) {
        inputIsEnableable = value
    }

    open fun getInputIsEnableableValue(): Boolean {
        return inputIsEnableable
    }

    /**
     * Method shows if inputText field is enabled or not.
     */
    open fun isInputEnabled(): Boolean {
        return inputText?.keyListener != null
    }

    /**
     * Method makes inputText field enabled.
     */
    open fun enableInput() {
        if (inputText?.tag != null)
            inputText?.keyListener = inputText?.tag as KeyListener
    }

    /**
     * Method makes inputText field disabled.
     */
    open fun disableInput() {
        inputText?.setText(inputText?.text.toString().trim())
        if (inputText?.keyListener != null) {
            inputText?.tag = inputText?.keyListener
            inputText?.keyListener = null
        }
    }

    /**
     * Method makes InputFieldView enabled. It means that inputText, start and end icons become enabled,
     * so client can edit text in input text and start and end icons are clickable.
     */
    open fun enableView() {
        if (inputIsEnableable)
            enableInput()
        clickViewEnable()
        startIconEnable()
        endIconEnable()
    }

    /**
     * Method makes InputFieldView disabled. It means that inputText, start and end icons become disabled,
     * so client can't edit text in input text and start and end icons are not clickable.
     */
    open fun disableView() {
        disableInput()
        clickViewDisable()
        startIconDisable()
        endIconDisable()
    }

    open fun setMaxLines(value: Int) {
        inputText?.maxLines = value
    }

    open fun setInputMaxLength(length: Int) {
        inputText?.filters = arrayOf(InputFilter.LengthFilter(length))
    }

    open fun setInputType(inputType: Int = defaultInputType) {
        inputText?.inputType = inputType
    }

    open fun setInputStyle(inputStyle: Int) {
        if (inputStyle != -1)
            inputText?.apply { TextViewCompat.setTextAppearance(this, inputStyle) }
    }

    /**
     * Method setups default input field view setting.
     */
    open fun setDefaultValues() {
        isWrap = defaultIsWrap
        showBottomContainer = defaultShowBottomContainer
        errorDrawableRes = defaultErrorDrawableRes
        strokeColor = defaultStrokeColor
        focusedStrokeColor = defaultFocusedStrokeColor
        boxBgColor = defaultBoxBgColor
        strokeRadiusInPx = defaultStrokeRadiusInPx.toFloat()
        strokeWidthInPx = defaultStrokeWidthInPx.toFloat()
        labelMaxLines = defaultLabelMaxLines
        editTextMinHeight = defaultMinHeight
        checkBoxToggle = defaultCheckBoxToggle
        passwordToggleRes = defaultPasswordToggleRes
        paddingTopInPx = defaultPaddingTopInPx
        paddingBottomInPx = defaultPaddingBottomInPx
        startTint = defaultIconsTint
        endTint = defaultIconsTint
        labelExtraTopMargin = defaultLabelExtraTopMargin

        inputClickView?.visibility = View.INVISIBLE
        inputText?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }
        prefixTextView?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }
        setupIconsSize(0)
        setIconsPaddings(defaultIconsPadding)
        setLabelType()
        setInputLabel("")
        setInputPrefix("")
        inputText?.apply {
            inputType = defaultInputType
            isEnabled = true
            setText("")
            hint = ""
            imeOptions = editorActionId
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textDirection = View.TEXT_DIRECTION_ANY_RTL
            }
        }
        enableView()
        setMaxLines(1)
        setHelp(null)
        setError(null)
        setupStartIconMode()
        setupEndIconMode()
        boxSettings()
        inputBoxVisibility(defaultInputBoxVisibility)
    }

    /*endregion ############### Input settings END ################*/

    open fun setInputClickViewEnabled(value: Boolean) {
        inputClickView?.visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

    open fun getInputClickViewEnabled(): Boolean {
        return inputClickView?.visibility == View.VISIBLE
    }

    open fun setClickViewListener(listener: OnClickListener?) {
        inputClickView?.setOnClickListener(listener)
    }

    open fun setClickViewLongListener(listener: OnLongClickListener?) {
        inputClickView?.setOnLongClickListener(listener)
    }

    open fun clickViewEnable() {
        inputClickView?.isClickable = true
        //CHECK inputClickView?.isEnabled = true
    }

    open fun clickViewDisable() {
        inputClickView?.isClickable = false
    }

    /*region ############### Input Click view settings ################*/

    /*endregion ############### Input Click view settings END ################*/

    /*region ############### Icons settings ################*/
    open fun setIconsPaddings(iconsPadding: Int) {
        startContainer?.setPadding(iconsPadding, iconsPadding, iconsPadding, iconsPadding)
        endContainer?.setPadding(iconsPadding, iconsPadding, iconsPadding, iconsPadding)
    }

    open fun setupIconsSize(iconsSizeValue: Int) {
        if (iconsSize != iconsSizeValue) {
            iconsSize = iconsSizeValue
            setContainerSize(startContainer, iconsSize)
            setContainerSize(endContainer, iconsSize)
        }
    }

    protected open fun setContainerSize(container: FrameLayout?, containerSize: Int) {
        val size: Int = if (containerSize == 0) dpToPx(32).roundToInt() else containerSize
        val layoutParams = container?.layoutParams
        if (layoutParams != null && (layoutParams.width != size || layoutParams.height != size)) {
            layoutParams.width = containerSize
            layoutParams.height = containerSize
            container.layoutParams = layoutParams
        }
    }

    protected open fun setCheckBoxTint(checkBox: AppCompatCheckBox, tintColor: Int) {
        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(
                checkBox,
                ContextCompat.getColorStateList(context, tintColor)
            )
        } else {
            checkBox.backgroundTintList = ContextCompat.getColorStateList(context, tintColor)
        }
    }
    /*endregion ############### Icons settings ################*/

    /*region ############### End Icon settings ################*/
    open fun setupEndIconMode(mode: Int = defaultEndIconMode) {
        endIconMode = mode
        updateEndIcon()
    }

    open fun setEndIconDrawable(@DrawableRes endIcon: Int) {
        setEndIconDrawable(ContextCompat.getDrawable(context, endIcon))
    }

    open fun setEndIconDrawable(endIcon: Drawable?) {
        this.endDrawable = endIcon
        updateEndIcon()
    }

    open fun isEndChecked(checked: Boolean) {
        if (endIconMode == END_ICON_CHECKABLE || endIconMode == END_ICON_PASSWORD_TOGGLE)
            endCheckBox?.isChecked = checked
    }

    open fun isEndChecked(): Boolean {
        return if (endIconMode == END_ICON_CHECKABLE || endIconMode == END_ICON_PASSWORD_TOGGLE) endCheckBox?.isChecked
            ?: false else false
    }

    open fun setEndIconTintRes(@ColorRes tintColor: Int) {
        endTint = tintColor
        updateEndIcon()
    }

    fun endIconEnable() {
        endContainer?.isClickable = true
        endCheckBox?.isClickable = true
    }

    fun endIconDisable() {
        endContainer?.isClickable = false
        endCheckBox?.isClickable = false
    }

    open fun setEndIconCheckListener(checkedListener: IconCheckedListener) {
        endCheckedListener = checkedListener
    }

    open fun setEndIconClickListener(clickListener: IconClickListener) {
        endClickListener = clickListener
    }

    /**
     * Method sets end icon settings such as icons click listener, components visibility,
     * resources depending on the end icon type.
     */
    protected open fun updateEndIcon() {
        if (endIconMode != END_ICON_PASSWORD_TOGGLE) {
            inputText?.transformationMethod = null
        }
        if (endIconMode != END_ICON_TEXT) setContainerSize(endContainer, iconsSize)
        when (endIconMode) {
            END_ICON_CLEAR_TEXT -> {
                setEndIconAsClear()
            }

            END_ICON_PASSWORD_TOGGLE -> {
                endContainer?.setOnClickListener {
                    endCheckBox?.performClick()
                }
                if (isInputTypePassword(inputText)) {
                    // By default set the input to be disguised.
                    inputText?.transformationMethod = PasswordTransformationMethod.getInstance()
                    endCheckBox?.isChecked = false
                }
                endCheckBox?.apply {
                    setOnCheckedChangeListener(passwordClickListener)
                    setBackgroundResource(passwordToggleRes)
                    setCheckBoxTint(this, endTint)
                    visibility = View.VISIBLE
                }
                endIconView?.visibility = View.INVISIBLE
                endContainer?.visibility = View.VISIBLE
            }

            END_ICON_CHECKABLE -> {
                endContainer?.setOnClickListener {
                    endCheckBox?.performClick()
                }
                endCheckBox?.apply {
                    setBackgroundResource(checkBoxToggle)
                    setOnCheckedChangeListener(endCheckClickListener)
                    setCheckBoxTint(this, endTint)
                    visibility = View.VISIBLE
                }
                endIconView?.visibility = View.INVISIBLE
                endContainer?.visibility = View.VISIBLE
            }

            END_ICON_CUSTOM -> {
                if (endDrawable == null) {
                    endContainer?.visibility = View.GONE
                } else {
                    endContainer?.setOnClickListener {
                        endClickListener?.onIconClick()
                    }
                    endIconView?.apply {
                        setImageDrawable(endDrawable)
                        ImageViewCompat.setImageTintList(
                            this,
                            ContextCompat.getColorStateList(context, endTint)
                        )
                        visibility = View.VISIBLE
                    }
                    endCheckBox?.visibility = View.INVISIBLE
                    endContainer?.visibility = View.VISIBLE
                }
            }

            END_ICON_TEXT -> {
                setEndIconAsText()
            }

            END_ICON_ERROR -> {
                setEndIconAsError()
            }

            else -> {
                endIconView?.visibility = View.INVISIBLE
                endCheckBox?.visibility = View.INVISIBLE
                endContainer?.visibility = View.GONE
            }
        }
    }

    protected open fun setEndIconAsClear() {
        endContainer?.setOnClickListener {
            endClickListener?.onIconClick()
            if (isInputEnabled())
                clearInputText()
        }
        endIconView?.apply {
            if (endDrawable == null)
                setImageResource(R.drawable.ic_input_field_clear_default_24)
            else
                setImageDrawable(endDrawable)
            ImageViewCompat.setImageTintList(
                this,
                ContextCompat.getColorStateList(context, endTint)
            )
            visibility = if (inputText?.text?.isNotEmpty() == true) View.VISIBLE else View.INVISIBLE
        }
        endCheckBox?.visibility = View.INVISIBLE
        endContainer?.visibility = View.VISIBLE
    }

    protected open fun setEndIconAsText() {
        setContainerSize(endContainer, -2)
        endText?.setOnClickListener {
            endClickListener?.onIconClick()
        }
        endIconView?.visibility = View.GONE
        endCheckBox?.visibility = View.GONE
        endContainer?.visibility = View.VISIBLE
    }

    /**
     * Method sets required icons settings for error mode. If hideErrorIcon value is false
     * method sets visibility of endIconView, endCheckBox, endContainer and
     * if argument is true or end icon is invisible sets empty clickListener
     * and update end icon image and tint.
     */
    protected open fun setEndIconAsError(forcefully: Boolean = false) {
        if (!hideErrorIcon) {
            if (forcefully || endIconView?.visibility == View.INVISIBLE) {
                endContainer?.setOnClickListener {}
                endIconView?.apply {
                    setImageResource(errorDrawableRes)
                    ImageViewCompat.setImageTintList(
                        this,
                        ContextCompat.getColorStateList(context, endTint)
                    )
                }
            }
            endIconView?.visibility = View.VISIBLE
            endCheckBox?.visibility = View.INVISIBLE
            endContainer?.visibility = View.VISIBLE
        }
    }
    /*endregion ############### End Icon settings End ############*/

    /*region ############### End Text settings ############*/
    open fun setEndText(text: String?) {
        if (!text.isNullOrBlank()) {
            endText?.let { endText ->
                val isRTL: Boolean = resources.getBoolean(R.bool.input_is_rtl_direction)
                TextViewCompat.setTextAppearance(endText, inputEndTextStyleId)
                if ((endText.text?.toString() ?: "") != text) {
                    val hint = if (isRTL) "\u202B$text" else text
                    endText.text = hint
                }
            }
        } else
            hideEndText()
    }

    open fun setEndTextStyle(styleId: Int) {
        if (styleId != -1) {
            inputEndTextStyleId = styleId
            endText?.apply { TextViewCompat.setTextAppearance(this, inputEndTextStyleId) }
        }
    }

    open fun hideEndText() {
        endText?.let { endText ->
            endText.background = null
            endText.visibility = View.INVISIBLE
        }
    }
    /*endregion ############### End Text settings ############*/

    /*region ############### Start Icon settings ################*/
    open fun setupStartIconMode(startMode: Int = defaultStartIconMode) {
        startIconMode = startMode
        updateStartIcon()
    }

    open fun setStartIconDrawable(@DrawableRes startDraw: Int) {
        setStartIconDrawable(ContextCompat.getDrawable(context, startDraw))
    }

    open fun setStartIconDrawable(startDraw: Drawable?) {
        startDrawable = startDraw
        updateStartIcon()
    }

    open fun isStartChecked(checked: Boolean) {
        if (startIconMode == START_ICON_CHECKABLE)
            startCheckBox?.isChecked = checked
    }

    open fun isStartChecked(): Boolean {
        return if (startIconMode == START_ICON_CHECKABLE) startCheckBox?.isChecked
            ?: false else false
    }

    open fun isInProgress(value: Boolean) {
        if (startIconMode != START_ICON_PROGRESS)
            beforeProgressMode = startIconMode
        startIconMode = if (value) START_ICON_PROGRESS else beforeProgressMode
        updateStartIcon()
    }

    open fun isInProgress(): Boolean {
        return startIconMode == START_ICON_PROGRESS
    }

    open fun setStartIconTintRes(@ColorRes tintColor: Int) {
        startTint = tintColor
        updateStartIcon()
    }

    open fun startIconEnable() {
        startContainer?.isClickable = true
        startCheckBox?.isClickable = true
    }

    open fun startIconDisable() {
        startContainer?.isClickable = false
        startCheckBox?.isClickable = false
    }

    open fun setStartIconCheckListener(checkedListener: IconCheckedListener) {
        startCheckedListener = checkedListener
    }

    open fun setStartIconClickListener(listener: IconClickListener) {
        startClickListener = listener
    }

    protected open fun setStartProgressBarTint(progressBar: ProgressBar, colorRes: Int) {
        progressBar.indeterminateDrawable?.setTintList(
            ContextCompat.getColorStateList(
                context,
                colorRes
            )
        )
    }

    /**
     * Method sets start icon settings and visibility of startIconView, startCheckBox, progressBar,
     * startContainer depending on the start icon type.
     */
    protected open fun updateStartIcon() {
        if (startIconMode != START_ICON_DEFAULT || startIconMode != START_ICON_PROGRESS)
            startContainer?.isClickable = true
        when (startIconMode) {
            START_ICON_CHECKABLE -> {
                startContainer?.setOnClickListener {
                    startCheckBox?.performClick()
                }
                startCheckBox?.apply {
                    //if (startDrawable == null)
                    setBackgroundResource(checkBoxToggle)
                    //else buttonDrawable = startDrawable
                    setOnCheckedChangeListener(startCheckClickListener)
                    setCheckBoxTint(this, startTint)
                    visibility = View.VISIBLE
                }

                startIconView?.visibility = View.INVISIBLE
                progressBar?.visibility = View.INVISIBLE
                startContainer?.visibility = View.VISIBLE
            }

            START_ICON_CUSTOM -> {
                if (startDrawable == null) {
                    startContainer?.visibility = View.GONE
                } else {
                    startContainer?.setOnClickListener {
                        startClickListener?.onIconClick()
                    }
                    startIconView?.apply {
                        setImageDrawable(startDrawable)
                        ImageViewCompat.setImageTintList(
                            this,
                            ContextCompat.getColorStateList(context, startTint)
                        )
                        this.visibility = View.VISIBLE
                    }

                    progressBar?.visibility = View.INVISIBLE
                    startCheckBox?.visibility = View.INVISIBLE
                    startContainer?.visibility = View.VISIBLE
                }
            }

            START_ICON_DEFAULT -> {
                if (startDrawable == null) {
                    startContainer?.visibility = View.GONE
                } else {
                    startContainer?.isClickable = false
                    startIconView?.apply {
                        setImageDrawable(startDrawable)
                        ImageViewCompat.setImageTintList(
                            this,
                            ContextCompat.getColorStateList(context, startTint)
                        )
                        this.visibility = View.VISIBLE
                    }
                    progressBar?.visibility = View.INVISIBLE
                    startCheckBox?.visibility = View.INVISIBLE
                    startContainer?.visibility = View.VISIBLE
                }
            }

            START_ICON_PROGRESS -> {
                startContainer?.isClickable = false
                progressBar?.apply {
                    setStartProgressBarTint(this, startTint)
                    this.visibility = View.VISIBLE
                }
                startIconView?.visibility = View.INVISIBLE
                startCheckBox?.visibility = View.INVISIBLE
                startContainer?.visibility = View.VISIBLE
            }

            else -> {
                startIconView?.visibility = View.INVISIBLE
                startCheckBox?.visibility = View.INVISIBLE
                progressBar?.visibility = View.INVISIBLE
                startContainer?.visibility = View.GONE
            }
        }
    }

    /*endregion ############### Start Icon settings End ############*/

    /*region############### Bottom text settings ################*/
    /* Helper */
    open fun setHelp(text: String?) {
        hasHelpText = !text.isNullOrBlank()
        helpTextView?.text = text
        updateBottomTextPosition()
    }

    open fun setHelp(textId: Int) {
        if (textId != -1) {
            setHelp(context.resources.getString(textId))
        } else {
            setHelp(null)
        }
    }

    open fun setHelpStyle(helpId: Int) {
        if (helpId != -1)
            helpTextView?.apply { TextViewCompat.setTextAppearance(this, helpId) }
    }
    /* Helper end */

    /* Error */
    /**
     * SetError method setup errorText, label and start icon required colors and bottom text
     * depending on the text argument.
     */
    open fun setError(text: String?) {
        hasErrorText = !text.isNullOrBlank()
        errorTextView?.text = text
        updateErrorState()
    }

    protected open fun updateErrorState() {
        refreshDrawableState()
        updateBottomTextPosition()
    }

    open fun setError(textId: Int) {
        if (textId != -1) {
            setError(context.resources.getString(textId))
        } else {
            setError(null)
        }
    }

    open fun setErrorException(error: Exception?) {
        setError(error?.message)
    }

    open fun setHideErrorTextMode(value: Boolean) {
        hideErrorText = value
    }

    open fun setErrorStyle(errorStyle: Int) {
        if (errorStyle != -1)
            errorTextView?.apply { TextViewCompat.setTextAppearance(this, errorStyle) }
    }
    /* Error End*/

    /**
     * Depending on the bottom text type update stroke color in inputBox,
     * previous and current end icon mode, visibility of bottom container,
     * errorTextView and helpTextView.
     */
    protected open fun updateBottomTextPosition() {
        when {
            hasErrorText -> {
                if (endIconMode == END_ICON_PASSWORD_TOGGLE || endIconMode == END_ICON_TEXT)
                    updateEndIcon()
                else if (endIconMode != END_ICON_ERROR) {
                    previousEndIconMode = endIconMode
                    setupEndIconMode(END_ICON_ERROR)
                }
                bottomContainer?.visibility = View.VISIBLE
                errorTextView?.visibility = View.VISIBLE
                helpTextView?.visibility = View.GONE
            }

            hasHelpText -> {
                if (endIconMode == END_ICON_ERROR)
                    setupEndIconMode(previousEndIconMode)
                else if (endIconMode == END_ICON_PASSWORD_TOGGLE)
                    updateEndIcon()
                bottomContainer?.visibility = View.VISIBLE
                errorTextView?.visibility = View.GONE
                helpTextView?.visibility = View.VISIBLE
            }

            else -> {
                if (endIconMode == END_ICON_ERROR)
                    setupEndIconMode(previousEndIconMode)
                else if (endIconMode == END_ICON_PASSWORD_TOGGLE || endIconMode == END_ICON_TEXT)
                    updateEndIcon()
                bottomContainer?.visibility = if (showBottomContainer) View.INVISIBLE else View.GONE
            }
        }
    }
    /*endregion ################### Bottom text settings End ######################*/

    /*region ############### TextWatcher settings ################*/
    open fun addTextWatcher(textWatcher: TextWatcher) {
        removeTextWatcher(textWatcher)
        inputText?.addTextChangedListener(textWatcher)
    }

    open fun removeTextWatcher(textWatcher: TextWatcher) {
        inputText?.removeTextChangedListener(textWatcher)
    }

    open fun addDefaultTextWatcher() {
        removeDefaultTextWatcher()
        inputText?.addTextChangedListener(textWatcher)
    }

    open fun removeDefaultTextWatcher() {
        inputText?.removeTextChangedListener(textWatcher)
    }
    /*endregion ############### TextWatcher settings ################*/

    /*region ################### Box View Settings ######################*/
    protected open fun boxSettings() {
        inputBox?.apply {
            setStrokeWidthInPx(strokeWidthInPx)
            setBgColor(boxBgColor)
            setStrokeRadiusInPx(strokeRadiusInPx)
        }
    }

    open fun inputBoxVisibility(): Int {
        return inputBox?.visibility ?: View.GONE
    }

    open fun inputBoxVisibility(visibility: Int) {
        inputBox?.visibility = visibility
    }

    open fun setBoxStrokeColorRes(color: Int) {
        if (strokeColor != color) {
            strokeColor = color
            refreshDrawableState()
        }
    }

    open fun setBoxFocusableColorRes(color: Int) {
        if (focusedStrokeColor != color) {
            focusedStrokeColor = color
            refreshDrawableState()
        }
    }

    protected open fun setBoxStrokeStateColor(state: IntArray) {
        getBoxColorStrokeForState(state, strokeColor)?.let { inputBox?.setStrokeColor(it) }
    }

    protected open fun setBoxStrokeDefaultStateColor() {
        ContextCompat.getColorStateList(context, strokeColor)
            ?.let { inputBox?.setStrokeColor(it.defaultColor) }
    }

    protected open fun getBoxColorStrokeForState(state: IntArray, colorSelector: Int): Int? {
        return ContextCompat.getColorStateList(context, colorSelector)?.let {
            it.getColorForState(
                state,
                it.defaultColor
            )
        }
    }

    /*endregion ################### Box View Settings ######################*/

    /*region ################### Editor Action Settings ######################*/

    open fun setOnEditorActionId(actionId: Int) {
        if (actionId != -1) {
            editorActionId = actionId
            inputText?.imeOptions = actionId
        }
    }

    open fun setupEditorActionListener(actionListener: EditorActionListener) {
        editorActionListener = actionListener
    }

    /*endregion ################### Editor Action Settings ######################*/

    /*region ################### Other ######################*/

    /**
     * Method sets label params depending on the label type. Return Boolean result of setLabelParams method
     * or return false if current margin top doesn't equal new margin top value.
     */
    protected open fun labelOnPreDraw(): Boolean {
        var draw = true
        labelText?.let { label ->
            var topClip = 0
            val params = (inputContainer?.layoutParams as LayoutParams?)
            val currTopMargin: Int = when (currentLabelType) {
                LABEL_TYPE_ON_TOP, LABEL_TYPE_ON_TOP_MULTI, LABEL_TYPE_ON_TOP_NO_START -> {
                    topClip = 0
                    label.height + labelExtraTopMargin
                }

                LABEL_TYPE_ON_LINE_MULTI -> {
                    topClip = label.width
                    label.height + labelExtraTopMargin - resources.getDimensionPixelOffset(R.dimen.input_container_top_margin_default)
                }

                LABEL_TYPE_ON_LINE -> {
                    topClip = label.width
                    resources.getDimensionPixelOffset(R.dimen.input_container_top_margin_default) + labelExtraTopMargin
                }

                else -> {
                    topClip = 0
                    params?.topMargin ?: 0
                }
            }
            draw = setLabelParams(label)
            inputBox?.setTopClipInPx(topClip.toFloat())
            if (params?.topMargin != currTopMargin) {
                params?.apply {
                    setMargins(this.leftMargin, currTopMargin, this.rightMargin, this.bottomMargin)
                }
                inputContainer?.layoutParams = params
                draw = false
            }
        }
        return draw
    }

    /**
     * Method sets label margins and paddings depending on the label type.
     */
    protected open fun setLabelParams(label: TextView): Boolean {
        var draw = true
        val params = (label.layoutParams as LayoutParams)
        if (currentLabelType == LABEL_TYPE_ON_TOP_NO_START) {
            if (labelStartMargin == 0)
                labelStartMargin = params.leftMargin
            if (labelStartPadding == 0)
                labelStartPadding = label.paddingLeft
            if (label.paddingLeft != 0) {
                draw = false
                label.setPadding(0, label.paddingTop, label.paddingRight, label.paddingBottom)
            }
            if (params.leftMargin != 0) {
                params.apply {
                    setMargins(0, topMargin, rightMargin, bottomMargin)
                }
            }
        } else {
            if (labelStartMargin == 0)
                labelStartMargin = params.leftMargin
            if (labelStartPadding == 0)
                labelStartPadding = label.paddingLeft
            if (label.paddingLeft != labelStartPadding) {
                draw = false
                label.setPadding(
                    labelStartPadding,
                    label.paddingTop,
                    label.paddingRight,
                    label.paddingBottom
                )
            }
            if (params.leftMargin != labelStartMargin) {
                params.apply {
                    setMargins(labelStartMargin, topMargin, rightMargin, bottomMargin)
                }
            }
        }
        return draw
    }

    protected fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    private fun initialSetting() {
        val view = inflate(context, inflateLayoutRes, this)
        labelText = view.findViewById(R.id.v_input_field_label)
        editTextContainer = view.findViewById(R.id.v_input_field_edit_layout)
        inputBox = view.findViewById(R.id.v_input_field_layout_box)
        inputClickView = view.findViewById(R.id.v_input_field_layout_container_click_handler)

        inputContainer = view.findViewById(R.id.v_input_field_layout_container)
        inputText = view.findViewById(R.id.v_input_field_edit)
        prefixTextView = view.findViewById(R.id.v_input_field_prefix)

        bottomContainer = view.findViewById(R.id.v_input_field_bottom_text_container)
        errorTextView = view.findViewById(R.id.v_input_field_error)
        helpTextView = view.findViewById(R.id.v_input_field_help)

        startContainer = view.findViewById(R.id.v_input_field_start_container)
        progressBar = view.findViewById(R.id.v_input_field_progress_bar)
        startIconView = view.findViewById(R.id.v_input_field_start_drawable)
        startCheckBox = view.findViewById(R.id.v_input_field_start_checkbox)

        endContainer = view.findViewById(R.id.v_input_field_end_container)
        endIconView = view.findViewById(R.id.v_input_field_end_drawable)
        endCheckBox = view.findViewById(R.id.v_input_field_password_toggle)
        endText = view.findViewById(R.id.v_input_field_end_text)

        startTint = R.color.default_selector_color
        endTint = startTint

        inputContainer?.isEnabled = false
        inputText?.onFocusChangeListener =
            OnFocusChangeListener { _, isFocused ->
                if (isFocused)
                    inputBox?.setStrokeColor(ContextCompat.getColor(context, focusedStrokeColor))
                else
                    setBoxStrokeDefaultStateColor()
            }

        inputText?.addTextChangedListener(textWatcher)
        inputText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == editorActionId) {
                editorActionListener?.onActionClick()
                true
            } else {
                false
            }
        }
        labelText?.viewTreeObserver?.addOnPreDrawListener(labelPreDrawListener)
    }

    /*  Initialize attributes from XML file  */
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputFieldView)

        /*##########  Common  ##########*/
        paddingTopInPx =
            typedArray.getDimensionPixelSize(
                R.styleable.InputFieldView_inputPaddingTop,
                defaultPaddingTopInPx
            )
        paddingBottomInPx = typedArray
            .getDimensionPixelSize(
                R.styleable.InputFieldView_inputPaddingBottom,
                defaultPaddingBottomInPx
            )
        showBottomContainer = typedArray
            .getBoolean(
                R.styleable.InputFieldView_inputShowBottomContainer,
                defaultShowBottomContainer
            )
        //Width style
        isWrap = typedArray.getBoolean(R.styleable.InputFieldView_inputIsWrap, defaultIsWrap)
        val enableClick =
            typedArray.getBoolean(R.styleable.InputFieldView_inputClickViewEnabled, false)
        //set start and end icons to be checked
        val isStartCheckedValue =
            typedArray.getBoolean(R.styleable.InputFieldView_inputIsStartChecked, false)
        val isEndCheckedValue =
            typedArray.getBoolean(R.styleable.InputFieldView_inputIsEndChecked, false)

        /*##########  Label  ##########*/
        val label = typedArray.getString(R.styleable.InputFieldView_inputLabel) ?: ""
        val labelType =
            typedArray.getInt(R.styleable.InputFieldView_inputLabelType, defaultLabelType)
        labelStyleId =
            typedArray.getResourceId(
                R.styleable.InputFieldView_inputLabelTextStyle,
                defaultTextStyle
            )
        labelExtraTopMargin = typedArray
            .getDimensionPixelSize(
                R.styleable.InputFieldView_inputLabelExtraTopMargin,
                defaultLabelExtraTopMargin
            )
        // Label max lines for multy label type
        labelMaxLines = typedArray.getResourceId(
            R.styleable.InputFieldView_inputLabelMaxLines,
            defaultLabelMaxLines
        )

        /*##########  Input Text  ##########*/
        val editStyleId: Int =
            typedArray.getResourceId(
                R.styleable.InputFieldView_inputEditTextStyle,
                defaultInputTextStyle
            )
        val hint = typedArray.getString(R.styleable.InputFieldView_android_hint) ?: ""
        val text: String = typedArray.getString(R.styleable.InputFieldView_android_text) ?: ""
        val inputType =
            typedArray.getInt(R.styleable.InputFieldView_android_inputType, defaultInputType)
        val editable: Boolean =
            typedArray.getBoolean(R.styleable.InputFieldView_inputEditable, true)
        val maxLines: Int = typedArray.getInt(R.styleable.InputFieldView_android_maxLines, 1)
        val textDirection: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            typedArray.getInt(
                R.styleable.InputFieldView_android_textDirection,
                View.TEXT_DIRECTION_ANY_RTL
            )
        } else 0
        editTextMinHeight = typedArray
            .getDimensionPixelSize(
                R.styleable.InputFieldView_inputEditViewMinHeight,
                defaultMinHeight
            )
        inputIsEnableable =
            typedArray.getBoolean(R.styleable.InputFieldView_inputIsEnableable, true)

        /*##########  Prefix  ##########*/
        val prefix = typedArray.getString(R.styleable.InputFieldView_inputPrefix) ?: ""
        val prefixStyleId: Int =
            typedArray.getResourceId(R.styleable.InputFieldView_inputPrefixTextStyle, -1)

        /*##########  Icons  ##########*/
        passwordToggleRes =
            typedArray.getResourceId(
                R.styleable.InputFieldView_inputPasswordToggle,
                defaultPasswordToggleRes
            )
        checkBoxToggle = typedArray.getResourceId(
            R.styleable.InputFieldView_inputCheckToggle,
            defaultCheckBoxToggle
        )
        iconsSize =
            typedArray.getDimensionPixelSize(R.styleable.InputFieldView_inputIconsSize, 0)
        val iconsPadding = typedArray
            .getDimensionPixelSize(
                R.styleable.InputFieldView_inputIconsPadding,
                defaultIconsPadding
            )

        /*##########  Start Icon  ##########*/
        val startDrawableRes: Int =
            typedArray.getResourceId(R.styleable.InputFieldView_inputStartDrawable, -1)
        if (startDrawableRes != -1) startDrawable =
            ContextCompat.getDrawable(context, startDrawableRes)
        startTint =
            typedArray.getResourceId(R.styleable.InputFieldView_inputStartDrawableTint, startTint)
        startIconMode =
            typedArray.getInt(R.styleable.InputFieldView_inputStartIconMode, defaultStartIconMode)

        /*##########  End Icon  ##########*/
        val endDrawableRes: Int =
            typedArray.getResourceId(R.styleable.InputFieldView_inputEndDrawable, -1)
        if (endDrawableRes != -1) endDrawable = ContextCompat.getDrawable(context, endDrawableRes)

        endTint = typedArray.getResourceId(R.styleable.InputFieldView_inputEndDrawableTint, endTint)
        endIconMode =
            typedArray.getInt(R.styleable.InputFieldView_inputEndIconMode, defaultEndIconMode)

        /*##########  End Text  ##########*/
        val endText = typedArray.getString(R.styleable.InputFieldView_inputEndText) ?: ""
        inputEndTextStyleId =
            typedArray.getResourceId(R.styleable.InputFieldView_inputEndTextStyle, defaultTextStyle)

        /*##########  Help  ##########*/
        val help = typedArray.getString(R.styleable.InputFieldView_inputHelp) ?: ""
        val helpStyleId: Int =
            typedArray.getResourceId(
                R.styleable.InputFieldView_inputHelpTextStyle,
                defaultTextStyle
            )

        /*##########  Error  ##########*/
        val errorText = typedArray.getString(R.styleable.InputFieldView_inputError) ?: ""
        val errorStyleId: Int =
            typedArray.getResourceId(R.styleable.InputFieldView_inputErrorTextStyle, -1)
        errorDrawableRes = typedArray.getResourceId(
            R.styleable.InputFieldView_inputErrorIcon,
            defaultErrorDrawableRes
        )
        hideErrorIcon = typedArray.getBoolean(
            R.styleable.InputFieldView_inputHideErrorIcon,
            defaultHideErrorIcon
        )

        /*##########  Box  ##########*/
        strokeColor = typedArray.getResourceId(
            R.styleable.InputFieldView_inputStrokeColor,
            defaultStrokeColor
        )
        focusedStrokeColor =
            typedArray.getColor(
                R.styleable.InputFieldView_inputFocusedStrokeColor,
                defaultFocusedStrokeColor
            )
        boxBgColor =
            typedArray.getColor(R.styleable.InputFieldView_inputBoxBgColor, defaultBoxBgColor)
        strokeRadiusInPx =
            typedArray.getDimensionPixelSize(
                R.styleable.InputFieldView_inputStrokeRadius,
                defaultStrokeRadiusInPx
            )
                .toFloat()
        strokeWidthInPx =
            typedArray.getDimensionPixelSize(
                R.styleable.InputFieldView_inputStrokeWidth,
                defaultStrokeWidthInPx
            )
                .toFloat()
        val inputBoxVisibility = typedArray.getInt(
            R.styleable.InputFieldView_inputBoxVisibility,
            defaultInputBoxVisibility
        )

        //ActionEditor
        editorActionId = typedArray.getInt(R.styleable.InputFieldView_android_imeOptions, -1)

        typedArray.recycle()

        setupIconsSize(iconsSize)
        setIconsPaddings(iconsPadding)
        setEndText(endText)

        //make inputClickView visible/invisible
        inputClickView?.visibility = if (enableClick) View.VISIBLE else View.INVISIBLE

        // Set new attributes
        if (isWrap) {
            val inputParams = inputText?.layoutParams as LayoutParams?
            inputParams?.width = WRAP_CONTENT
            inputText?.layoutParams = inputParams
            val containerParams = editTextContainer?.layoutParams as LayoutParams?
            containerParams?.width = WRAP_CONTENT
            editTextContainer?.layoutParams = containerParams
        }

        editTextContainer?.apply {
            minimumHeight = editTextMinHeight
        }
        inputText?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }
        prefixTextView?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }

        if (prefixStyleId != -1)
            prefixTextView?.apply { TextViewCompat.setTextAppearance(this, prefixStyleId) }
        else if (editStyleId != -1)
            prefixTextView?.apply { TextViewCompat.setTextAppearance(this, editStyleId) }

        setInputLabel(label)
        setLabelType(labelType)
        setLabelStyle(labelStyleId)
        setInputStyle(editStyleId)
        setInputPrefix(prefix)
        inputText?.apply {
            this.inputType = inputType
            isEnabled = editable
            setText(text)
            this.hint = hint
            imeOptions = editorActionId
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                this.textDirection = textDirection
            }
        }
        setMaxLines(maxLines)
        setHelp(help)
        setHelpStyle(helpStyleId)
        setError(errorText)
        setErrorStyle(errorStyleId)
        updateStartIcon()
        updateEndIcon()
        boxSettings()
        inputBoxVisibility(inputBoxVisibility)
        //set start and end icons to be checked
        isStartChecked(isStartCheckedValue)
        isEndChecked(isEndCheckedValue)
        if (endIconMode == END_ICON_PASSWORD_TOGGLE) {
            inputText?.apply {
                transformationMethod = if (isEndCheckedValue) {
                    null
                } else {
                    PasswordTransformationMethod.getInstance()
                }
            }
        }
    }

    /*endregion ################### Other ######################*/

    /*region ################### Interfaces ######################*/

    interface IconCheckedListener {

        fun onCheckChanged(isChanged: Boolean)
    }

    interface IconClickListener {

        fun onIconClick()
    }

    interface EditorActionListener {

        fun onActionClick()
    }

    /*endregion ################### Interfaces End ######################*/
}
