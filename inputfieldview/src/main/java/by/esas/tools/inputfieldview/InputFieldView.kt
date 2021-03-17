package by.esas.tools.inputfieldview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat

open class InputFieldView : ConstraintLayout {
    open val TAG: String = InputFieldView::class.java.simpleName

    companion object {
        const val END_ICON_CUSTOM: Int = -1
        const val END_ICON_NONE: Int = 0
        const val END_ICON_PASSWORD_TOGGLE: Int = 1
        const val END_ICON_CLEAR_TEXT: Int = 2
        const val END_ICON_CHECKABLE: Int = 3
        const val END_ICON_ERROR: Int = 4

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

    /*################ Views ################*/
    protected var labelText: TextView? = null
    protected var editTextContainer: ViewGroup? = null
    protected var inputBox: BoxView? = null

    protected var inputContainer: ViewGroup? = null
    var inputText: EditText? = null
    var prefixTextView: TextView? = null

    protected var bottomContainer: FrameLayout? = null
    var errorTextView: TextView? = null
    protected var helpTextView: TextView? = null

    protected var startContainer: FrameLayout? = null
    protected var progressBar: ProgressBar? = null
    var startIconView: AppCompatImageView? = null
    var startCheckBox: AppCompatCheckBox? = null

    protected var endContainer: FrameLayout? = null
    var endIconView: AppCompatImageView? = null
    var endCheckBox: AppCompatCheckBox? = null
    /*################ Views END ################*/

    /*################ Parameters ################*/
    protected open val defaultErrorDrawableRes: Int = R.drawable.ic_baseline_error_24
    protected open val defaultErrorColor: Int = ContextCompat.getColor(context, R.color.colorInputError)
    protected open val defaultStrokeColor: Int = ContextCompat.getColor(context, R.color.colorTextHint)
    protected open val defaultFocusedStrokeColor: Int = ContextCompat.getColor(context, R.color.colorStrokeOutline)
    protected open val defaultBoxBgColor: Int = Color.TRANSPARENT
    protected open val defaultStrokeRadiusInPx: Int = dpToPx(4).toInt()
    protected open val defaultStrokeWidthInPx: Int = dpToPx(1).toInt()
    protected open val defaultPaddingTopInPx: Int = dpToPx(12).toInt()
    protected open val defaultPaddingBottomInPx: Int = dpToPx(12).toInt()
    protected open val defaultIsWrap: Boolean = false
    protected open val defaultHideErrorIcon: Boolean = false

    protected open var errorDrawableRes: Int = R.drawable.ic_baseline_error_24
    protected open var errorColor: Int = ContextCompat.getColor(context, R.color.colorInputError)
    protected open var strokeColor: Int = ContextCompat.getColor(context, R.color.colorTextHint)
    protected open var focusedStrokeColor: Int = ContextCompat.getColor(context, R.color.colorStrokeOutline)
    protected open var boxBgColor: Int = Color.TRANSPARENT
    protected open var strokeRadiusInPx: Float = dpToPx(4)
    protected open var strokeWidthInPx: Float = dpToPx(1)
    protected open var paddingTopInPx: Int = dpToPx(12).toInt()
    protected open var paddingBottomInPx: Int = dpToPx(12).toInt()
    protected open var isWrap: Boolean = false
    protected open var hideErrorIcon: Boolean = false

    protected open val inflateLayoutRes: Int = R.layout.v_input_field
    protected var labelStartMargin: Int = 0
    protected var labelStartPadding: Int = 0
    protected open val defaultIconsTint: Int = ContextCompat.getColor(context, R.color.colorPrimary)
    protected open val defaultInputType: Int = EditorInfo.TYPE_CLASS_TEXT
    protected open val defaultCheckBoxToggle: Int = R.drawable.selector_check_box_toggle
    protected var checkBoxToggle: Int = R.drawable.selector_check_box_toggle
    protected open val defaultMinHeight: Int = context.resources.getDimensionPixelSize(R.dimen.input_edit_text_default_min_height)
    protected var editTextMinHeight: Int = context.resources.getDimensionPixelSize(R.dimen.input_edit_text_default_min_height)

    //Label
    protected open val defaultLabelMaxLines: Int = Integer.MAX_VALUE
    protected var labelMaxLines: Int = Integer.MAX_VALUE
    protected open val defaultLabelExtraTopMargin: Int = 0
    protected var labelExtraTopMargin: Int = 0
    protected open val defaultLabelType: Int = LABEL_TYPE_ON_LINE
    protected var currentLabelType: Int = LABEL_TYPE_ON_LINE
    protected open val defaultLabelBg: Int = ContextCompat.getColor(context, R.color.colorBackground)
    //protected open var labelBg: Int = ContextCompat.getColor(context, R.color.colorBackground)

    //StartIcon
    protected var startTint: Int = ContextCompat.getColor(context, R.color.colorPrimary)
    var startIconClickListener: IconClickListener? = null
    var startCheckedListener: IconCheckedListener? = null
    protected var startDrawable: Drawable? = null
    protected open val defaultStartIconMode: Int = START_ICON_NONE
    private var startIconMode: Int = START_ICON_NONE
    protected var beforeProgressMode: Int = startIconMode

    //End icon
    protected var endTint: Int = ContextCompat.getColor(context, R.color.colorPrimary)
    var endIconClickListener: IconClickListener? = null
    var endCheckedListener: IconCheckedListener? = null

    //var clearInputType: Int = EditorInfo.TYPE_CLASS_TEXT
    protected var endDrawable: Drawable? = null
    protected open val defaultEndIconMode: Int = END_ICON_NONE
    private var endIconMode: Int = END_ICON_NONE
    protected open val defaultPasswordToggleRes: Int = R.drawable.selector_password_toggle
    protected var passwordToggleRes: Int = R.drawable.selector_password_toggle

    //Bottom text
    protected var previousEndIconMode: Int = END_ICON_NONE
    private var hasErrorText: Boolean = false
    private var hasHelpText: Boolean = false

    protected open val defaultShowBottomContainer: Boolean = true
    protected var showBottomContainer: Boolean = false
    /*################ Parameters END ################*/

    /*############################ Icons Click Listeners ################################*/
    protected open val clearTextClickListener: IconClickListener = object : IconClickListener {
        override fun onIconClick() {
            inputText?.setText("")
            if (endIconMode == END_ICON_CLEAR_TEXT) {
                endIconView?.visibility = View.INVISIBLE
            }
        }
    }
    protected open val endCheckClickListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            endCheckedListener?.onCheckChanged(isChecked)
        }
    protected open val startCheckClickListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            startCheckedListener?.onCheckChanged(isChecked)
        }
    protected open val passwordClickListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            inputText?.apply {
                transformationMethod = if (isChecked) {
                    null
                } else {
                    PasswordTransformationMethod.getInstance()
                }
            }
        }
    /* protected open val passwordClickListener: IconClickListener = object : IconClickListener {
         override fun onIconClick() {
             // Store the current cursor position
             inputText?.apply {
                 val selection: Int = selectionEnd
                 transformationMethod = if (hasPasswordTransformation()) {
                     endCheckBox?.isChecked = false
                     null
                 } else {
                     endCheckBox?.isChecked = true
                     PasswordTransformationMethod.getInstance()
                 }

                 // And restore the cursor position
                 if (selection >= 0)
                     setSelection(selection)
             }
         }
     }*/
    /*############################ Icons Click Listeners End ################################*/

    /*############################ TextListener ################################*/
    private var textListener: TextListener? = null
    protected open val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            textListener?.onTextChanged(s.toString())
            if (endIconMode == END_ICON_CLEAR_TEXT) {
                endIconView?.visibility = if (s?.isNotEmpty() == true) View.VISIBLE else View.INVISIBLE
            } else if (endIconMode == END_ICON_ERROR && previousEndIconMode == END_ICON_CLEAR_TEXT) {
                if (s?.isNotEmpty() == true) {
                    setEndIconAsClear()
                } else {
                    setEndIconAsError(true)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    /*############################ TextListener End ################################*/
    protected val labelPreDrawListener = ViewTreeObserver.OnPreDrawListener {
        return@OnPreDrawListener labelOnPreDraw()
    }

    protected open fun labelOnPreDraw(): Boolean {
        var draw: Boolean = true
        labelText?.let { label ->
            var topClip = 0
            val params = (inputContainer?.layoutParams as ConstraintLayout.LayoutParams?)
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

    protected open fun setLabelParams(label: TextView): Boolean {
        var draw: Boolean = true
        val params = (label.layoutParams as ConstraintLayout.LayoutParams)
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
                label.setPadding(labelStartPadding, label.paddingTop, label.paddingRight, label.paddingBottom)
            }
            if (params.leftMargin != labelStartMargin) {
                params.apply {
                    setMargins(labelStartMargin, topMargin, rightMargin, bottomMargin)
                }
            }
        }
        return draw
    }

    /*############################ Constructors ################################*/
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initialSetting()
        initAttrs(attrs)
    }

    /*############################ Constructors End ################################*/


    private fun initialSetting() {
        val view = inflate(context, inflateLayoutRes, this)
        labelText = view.findViewById<TextView>(R.id.v_input_field_label)
        editTextContainer = view.findViewById<ViewGroup>(R.id.v_input_field_edit_layout)
        inputBox = view.findViewById<BoxView>(R.id.v_input_field_layout_box)

        inputContainer = view.findViewById<ViewGroup>(R.id.v_input_field_layout_container)
        inputText = view.findViewById<EditText>(R.id.v_input_field_edit)
        prefixTextView = view.findViewById<TextView>(R.id.v_input_field_prefix)

        bottomContainer = view.findViewById<FrameLayout>(R.id.v_input_field_bottom_text_container)
        errorTextView = view.findViewById<TextView>(R.id.v_input_field_error)
        helpTextView = view.findViewById<TextView>(R.id.v_input_field_help)

        startContainer = view.findViewById<FrameLayout>(R.id.v_input_field_start_container)
        progressBar = view.findViewById<ProgressBar>(R.id.v_input_field_progress_bar)
        startIconView = view.findViewById<AppCompatImageView>(R.id.v_input_field_start_drawable)
        startCheckBox = view.findViewById<AppCompatCheckBox>(R.id.v_input_field_start_checkbox)

        endContainer = view.findViewById<FrameLayout>(R.id.v_input_field_end_container)
        endIconView = view.findViewById<AppCompatImageView>(R.id.v_input_field_end_drawable)
        endCheckBox = view.findViewById<AppCompatCheckBox>(R.id.v_input_field_password_toggle)

        startTint = ContextCompat.getColor(context, R.color.colorPrimary)
        endTint = startTint

        inputContainer?.isEnabled = false
        inputText?.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                inputBox?.setStrokeColor(
                    if (hasErrorText) errorColor else {
                        if (hasFocus) focusedStrokeColor else strokeColor
                    }
                )
                /*if (hasErrorText) {
                    if (hasFocus && previousEndIconMode == END_ICON_PASSWORD_TOGGLE) {
                        setEndIconMode(previousEndIconMode)
                    } else {
                        setEndIconMode(END_ICON_ERROR)
                    }
                }*/
                //inputBox?.isEnabled = hasFocus
                //if (hasFocus) setError(null)
            }

        inputText?.addTextChangedListener(textWatcher)
        labelText?.viewTreeObserver?.addOnPreDrawListener(labelPreDrawListener)
    }

    /*  Initialize attributes from XML file  */
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputFieldView)
        //Hiding error
        hideErrorIcon = typedArray.getBoolean(R.styleable.InputFieldView_inputHideErrorIcon, defaultHideErrorIcon)
        //Width style
        isWrap = typedArray.getBoolean(R.styleable.InputFieldView_inputIsWrap, defaultIsWrap)
        // Label
        val label = typedArray.getString(R.styleable.InputFieldView_inputLabel) ?: ""
        // Label Mode
        val labelType = typedArray.getInt(R.styleable.InputFieldView_inputLabelType, defaultLabelType)
        //Label BG color
        //labelBg = typedArray.getColor(R.styleable.InputFieldView_inputLabelBgColor, defaultLabelBg)
        // Label TextStyle
        val labelStyleId: Int = typedArray.getResourceId(R.styleable.InputFieldView_inputLabelTextStyle, -1)
        labelExtraTopMargin =
            typedArray.getDimensionPixelSize(R.styleable.InputFieldView_inputLabelExtraTopMargin, defaultLabelExtraTopMargin)
        // Label max lines for multy label type
        labelMaxLines = typedArray.getResourceId(R.styleable.InputFieldView_inputLabelMaxLines, defaultLabelMaxLines)

        //Box
        boxBgColor = typedArray.getColor(R.styleable.InputFieldView_inputBoxBgColor, defaultBoxBgColor)
        errorColor = typedArray.getColor(R.styleable.InputFieldView_inputErrorColor, defaultErrorColor)
        strokeColor = typedArray.getColor(R.styleable.InputFieldView_inputInactiveStrokeColor, defaultStrokeColor)
        focusedStrokeColor = typedArray.getColor(R.styleable.InputFieldView_inputActiveStrokeColor, defaultFocusedStrokeColor)

        strokeRadiusInPx = typedArray.getDimensionPixelSize(R.styleable.InputFieldView_inputStrokeRadius, defaultStrokeRadiusInPx).toFloat()
        strokeWidthInPx = typedArray.getDimensionPixelSize(R.styleable.InputFieldView_inputStrokeWidth, defaultStrokeWidthInPx).toFloat()

        // Text
        val text: String = typedArray.getString(R.styleable.InputFieldView_android_text) ?: ""
        // Hint
        val hint = typedArray.getString(R.styleable.InputFieldView_android_hint) ?: ""
        // Input Type
        val inputType = typedArray.getInt(R.styleable.InputFieldView_android_inputType, defaultInputType)
        // Max Lines
        val maxLines: Int = typedArray.getInt(R.styleable.InputFieldView_android_maxLines, 1)
        // Text Direction
        val textDirection: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            typedArray.getInt(R.styleable.InputFieldView_android_textDirection, View.TEXT_DIRECTION_ANY_RTL)
        } else {
            0
        }
        // Editable
        val editable: Boolean = typedArray.getBoolean(R.styleable.InputFieldView_inputEditable, true)
        // EditText min height
        editTextMinHeight = typedArray.getDimensionPixelSize(R.styleable.InputFieldView_inputEditViewMinHeight, defaultMinHeight)
        val editStyleId: Int = typedArray.getResourceId(R.styleable.InputFieldView_inputEditTextStyle, -1)

        paddingTopInPx = typedArray.getDimensionPixelSize(R.styleable.InputFieldView_inputPaddingTop, defaultPaddingTopInPx)
        paddingBottomInPx = typedArray.getDimensionPixelSize(R.styleable.InputFieldView_inputPaddingBottom, defaultPaddingBottomInPx)

        //val textCursor: Int = typedArray.getResourceId(R.styleable.InputFieldView_inputTextCursor, -1)

        // Prefix
        val prefix = typedArray.getString(R.styleable.InputFieldView_inputPrefix) ?: ""
        val prefixStyleId: Int = typedArray.getResourceId(R.styleable.InputFieldView_inputPrefixTextStyle, -1)

        // Start Icon Mode
        startIconMode = typedArray.getInt(R.styleable.InputFieldView_inputStartIconMode, defaultStartIconMode)
        // Start Drawable
        startDrawable = typedArray.getDrawable(R.styleable.InputFieldView_inputStartDrawable)
        // Start Tint
        startTint = typedArray.getColor(R.styleable.InputFieldView_inputStartDrawableTint, startTint)
        // Start Icon is Checkable

        // End Icon Mode
        endIconMode = typedArray.getInt(R.styleable.InputFieldView_inputEndIconMode, defaultEndIconMode)
        // End Drawable
        endDrawable = typedArray.getDrawable(R.styleable.InputFieldView_inputEndDrawable)
        // End Tint
        endTint = typedArray.getColor(R.styleable.InputFieldView_inputEndDrawableTint, endTint)

        // Password Toggle
        passwordToggleRes = typedArray.getResourceId(R.styleable.InputFieldView_inputPasswordToggle, defaultPasswordToggleRes)
        // Check Toggle
        checkBoxToggle = typedArray.getResourceId(R.styleable.InputFieldView_inputCheckToggle, defaultCheckBoxToggle)

        // Help
        val help = typedArray.getString(R.styleable.InputFieldView_inputHelp) ?: ""
        val helpStyleId: Int = typedArray.getResourceId(R.styleable.InputFieldView_inputHelpTextStyle, -1)
        // Error
        val errorStyleId: Int = typedArray.getResourceId(R.styleable.InputFieldView_inputErrorTextStyle, -1)
        errorDrawableRes = typedArray.getResourceId(R.styleable.InputFieldView_inputErrorIcon, defaultErrorDrawableRes)

        showBottomContainer = typedArray.getBoolean(R.styleable.InputFieldView_inputShowBottomContainer, defaultShowBottomContainer)

        typedArray.recycle()

        // Set new attributes
        if (isWrap) {
            val inputParams = inputText?.layoutParams as ConstraintLayout.LayoutParams?
            inputParams?.width = WRAP_CONTENT
            inputText?.layoutParams = inputParams
            val containerParams = editTextContainer?.layoutParams as ConstraintLayout.LayoutParams?
            containerParams?.width = WRAP_CONTENT
            editTextContainer?.layoutParams = containerParams
        }

        editTextContainer?.apply {
            minimumHeight = editTextMinHeight
        }
        /*(inputText?.layoutParams as ConstraintLayout.LayoutParams?)?.apply {
            setMargins(leftMargin, marginTopInPx, rightMargin, marginBottomInPx)
        }
        (prefixTextView?.layoutParams as ConstraintLayout.LayoutParams?)?.apply {
            setMargins(leftMargin, marginTopInPx, rightMargin, bottomMargin)
        }*/
        inputText?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }
        prefixTextView?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }

        if (labelStyleId != -1)
            labelText?.apply { TextViewCompat.setTextAppearance(this, labelStyleId) }
        if (helpStyleId != -1)
            helpTextView?.apply { TextViewCompat.setTextAppearance(this, helpStyleId) }
        if (errorStyleId != -1)
            errorTextView?.apply { TextViewCompat.setTextAppearance(this, errorStyleId) }
        if (editStyleId != -1)
            inputText?.apply { TextViewCompat.setTextAppearance(this, editStyleId) }
        if (prefixStyleId != -1)
            prefixTextView?.apply { TextViewCompat.setTextAppearance(this, prefixStyleId) }
        else if (editStyleId != -1)
            prefixTextView?.apply { TextViewCompat.setTextAppearance(this, editStyleId) }

        setInputLabel(label)
        setLabelType(labelType)
        setInputPrefix(prefix)
        inputText?.apply {
            this.inputType = inputType
            isEnabled = editable
            setText(text)
            this.hint = hint
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                this.textDirection = textDirection
            }
        }
        setMaxLines(maxLines)
        setHelp(help)
        setError(null)
        updateStartIcon()
        updateEndIcon()
        boxSettings()
    }

    open fun setDefaultValues() {
        isWrap = defaultIsWrap
        showBottomContainer = defaultShowBottomContainer
        errorDrawableRes = defaultErrorDrawableRes
        errorColor = defaultErrorColor
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
        //labelBg = defaultLabelBg
        startTint = defaultIconsTint
        endTint = defaultIconsTint
        labelExtraTopMargin = defaultLabelExtraTopMargin

        inputText?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }
        prefixTextView?.apply {
            setPadding(paddingLeft, paddingTopInPx, paddingRight, paddingBottomInPx)
        }
        setLabelType()
        setInputLabel("")
        setInputPrefix("")
        inputText?.apply {
            inputType = defaultInputType
            isEnabled = true
            setText("")
            hint = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textDirection = View.TEXT_DIRECTION_ANY_RTL
            }
        }
        setMaxLines(1)
        setHelp(null)
        setError(null)
        setStartIconMode()
        setEndIconMode()
        boxSettings()
    }


    /*############### Label settings ################*/
    open fun updateLabelState() {
        labelText?.let { label ->
            when (currentLabelType) {
                LABEL_TYPE_ON_TOP, LABEL_TYPE_ON_TOP_MULTI, LABEL_TYPE_ON_TOP_NO_START -> {
                    label.maxLines = if (currentLabelType != LABEL_TYPE_ON_TOP_MULTI) 1 else labelMaxLines
                    label.visibility = View.VISIBLE
                    //label.background = null
                    label.invalidate()
                }
                LABEL_TYPE_ON_LINE, LABEL_TYPE_ON_LINE_MULTI -> {
                    label.maxLines = if (currentLabelType != LABEL_TYPE_ON_LINE_MULTI) 1 else labelMaxLines
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

    open fun hideLabel() {
        labelText?.let { label ->
            label.background = null
            label.visibility = View.INVISIBLE
            (inputContainer?.layoutParams as ConstraintLayout.LayoutParams?)?.apply {
                setMargins(leftMargin, 0, rightMargin, bottomMargin)
            }
        }
    }

    open fun showLabel() {
        if (currentLabelType == LABEL_TYPE_HIDE)
            currentLabelType = defaultLabelType
        updateLabelState()
    }

    fun setLabelType(labelType: Int = defaultLabelType) {
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

    fun getInputLabel(): String {
        return labelText?.text?.toString() ?: ""
    }
    /*############### Label settings END ################*/


    /*############### Prefix settings ################*/
    open fun setInputPrefix(prefix: String) {
        prefixTextView?.text = prefix
    }

    open fun getTextWithPrefix(): String {
        return prefixTextView?.text?.toString() ?: "" + inputText?.text?.toString() ?: ""
    }
    /*############### Prefix settings END ################*/


    /*############### Input settings ################*/
    open fun setText(text: String?) {
        if (!(inputText?.text?.toString() ?: "").equals(text ?: "")) {
            inputText?.setText(text)
        }
    }

    open fun getText(): String {
        return inputText?.text?.toString() ?: ""
    }

    fun setTextListener(listener: TextListener?) {
        textListener = listener
        /* if (textListener != null) {
             inputText.addTextChangedListener(textWatcher)
         } else inputText.removeTextChangedListener(textWatcher)*/
    }

    fun isEditable(value: Boolean) {
        inputText?.isEnabled = value
    }

    fun isEditable(): Boolean {
        return inputText?.isEnabled ?: false
    }

    fun setMaxLines(value: Int) {
        inputText?.maxLines = value
    }

    fun setInputType(inputType: Int = defaultInputType) {
        inputText?.inputType = inputType
    }
    /*############### Input settings END ################*/

    /*############### End Icon settings ################*/
    protected open fun updateEndIcon() {
        if (endIconMode != END_ICON_PASSWORD_TOGGLE) {
            inputText?.transformationMethod = null
        }
        when (endIconMode) {
            END_ICON_CLEAR_TEXT -> {
                setEndIconAsClear()
            }
            END_ICON_PASSWORD_TOGGLE -> {
                endContainer?.setOnClickListener {
                    endCheckBox?.performClick()
                    //endCheckedListener?.onCheckChanged(endCheckBox?.isChecked ?: false)
                }
                if (isInputTypePassword(inputText)) {
                    // By default set the input to be disguised.
                    inputText?.transformationMethod = PasswordTransformationMethod.getInstance()
                    endCheckBox?.isChecked = false
                }
                endCheckBox?.apply {
                    setOnCheckedChangeListener(passwordClickListener)
                    if (endDrawable == null)
                        setButtonDrawable(passwordToggleRes)
                    else
                        buttonDrawable = endDrawable
                    if (hasErrorText)
                        CompoundButtonCompat.setButtonTintList(this, ColorStateList.valueOf(errorColor))
                    else
                        CompoundButtonCompat.setButtonTintList(this, ColorStateList.valueOf(endTint))
                    visibility = View.VISIBLE
                }
                endIconView?.visibility = View.INVISIBLE
                endContainer?.visibility = View.VISIBLE
            }
            END_ICON_CHECKABLE -> {
                endContainer?.setOnClickListener {
                    endCheckBox?.performClick()
                    //endCheckedListener?.onCheckChanged(endCheckBox?.isChecked ?: false)
                }
                endCheckBox?.apply {
                    if (endDrawable == null)
                        setButtonDrawable(checkBoxToggle)
                    else {
                        buttonDrawable = endDrawable
                    }
                    setOnCheckedChangeListener(endCheckClickListener)
                    CompoundButtonCompat.setButtonTintList(this, ColorStateList.valueOf(endTint))
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
                        endIconClickListener?.onIconClick()
                    }
                    endIconView?.apply {
                        setImageDrawable(endDrawable)
                        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(endTint))
                        visibility = View.VISIBLE
                    }
                    endCheckBox?.visibility = View.INVISIBLE
                    endContainer?.visibility = View.VISIBLE
                }
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
            clearTextClickListener.onIconClick()
        }
        endIconView?.apply {
            if (endDrawable == null)
                setImageResource(R.drawable.ic_input_field_clear_default)
            else
                setImageDrawable(endDrawable)
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(endTint))
            visibility = if (inputText?.text?.isNotEmpty() == true) View.VISIBLE else View.INVISIBLE
        }
        endCheckBox?.visibility = View.INVISIBLE
        endContainer?.visibility = View.VISIBLE
    }

    protected open fun setEndIconAsError(forcefully: Boolean = false) {
        if (!hideErrorIcon) {
            if (forcefully || endIconView?.visibility == View.INVISIBLE) {
                endContainer?.setOnClickListener {}
                endIconView?.apply {
                    setImageResource(errorDrawableRes)
                    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(errorColor))
                }
            }
            endIconView?.visibility = View.VISIBLE
            endCheckBox?.visibility = View.INVISIBLE
            endContainer?.visibility = View.VISIBLE
        }
    }

    fun setEndIconMode(mode: Int = defaultEndIconMode) {
        if (endIconMode != mode) {
            endIconMode = mode
            updateEndIcon()
        }
    }

    fun setEndIconDrawable(@DrawableRes endIcon: Int) {
        setEndIconDrawable(ContextCompat.getDrawable(context, endIcon))
    }

    fun setEndIconDrawable(endIcon: Drawable?) {
        this.endDrawable = endIcon
        updateEndIcon()
    }

    fun isEndChecked(checked: Boolean) {
        if (endIconMode == END_ICON_CHECKABLE)
            endCheckBox?.isChecked = checked
    }

    fun isEndChecked(): Boolean {
        return if (endIconMode == END_ICON_CHECKABLE) endCheckBox?.isChecked ?: false else false
    }

    fun setEndIconTintRes(@ColorRes tintColor: Int) {
        setEndIconTint(ContextCompat.getColor(context, tintColor))
    }

    fun setEndIconTint(tintColor: Int) {
        if (startTint != tintColor) {
            startTint = tintColor
            updateEndIcon()
        }
    }
    /*############### End Icon settings End ############*/


    /*############### Start Icon settings ################*/

    protected open fun updateStartIcon() {
        if (startIconMode != START_ICON_DEFAULT || startIconMode != START_ICON_PROGRESS)
            startContainer?.isClickable = true
        when (startIconMode) {
            START_ICON_CHECKABLE -> {
                startContainer?.setOnClickListener {
                    startCheckedListener?.onCheckChanged(startCheckBox?.isChecked ?: false)
                }
                startCheckBox?.apply {
                    if (startDrawable == null)
                        setButtonDrawable(checkBoxToggle)
                    else
                        buttonDrawable = startDrawable
                    setOnCheckedChangeListener(startCheckClickListener)
                    CompoundButtonCompat.setButtonTintList(this, ColorStateList.valueOf(startTint))
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
                        startIconClickListener?.onIconClick()
                    }
                    startIconView?.apply {
                        setImageDrawable(startDrawable)
                        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(startTint))
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
                        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(startTint))
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
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        val wrapDrawable: Drawable = DrawableCompat.wrap(this.indeterminateDrawable)
                        DrawableCompat.setTint(wrapDrawable, startTint)
                        this.indeterminateDrawable = DrawableCompat.unwrap(wrapDrawable)
                    } else {
                        this.indeterminateDrawable?.setColorFilter(startTint, PorterDuff.Mode.SRC_IN)
                    }
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

    fun setStartIconMode(startMode: Int = defaultStartIconMode) {
        startIconMode = startMode
        updateStartIcon()
    }

    fun setStartIconDrawable(@DrawableRes startDraw: Int) {
        setStartIconDrawable(ContextCompat.getDrawable(context, startDraw))
    }

    fun setStartIconDrawable(startDraw: Drawable?) {
        startDrawable = startDraw
        updateStartIcon()
    }

    fun isStartChecked(checked: Boolean) {
        if (startIconMode == START_ICON_CHECKABLE)
            startCheckBox?.isChecked = checked
    }

    fun isStartChecked(): Boolean {
        return if (startIconMode == START_ICON_CHECKABLE) startCheckBox?.isChecked ?: false else false
    }

    fun isInProgress(value: Boolean) {
        if (startIconMode != START_ICON_PROGRESS)
            beforeProgressMode = startIconMode
        startIconMode = if (value) START_ICON_PROGRESS else beforeProgressMode
        updateStartIcon()
    }

    fun isInProgress(): Boolean {
        return startIconMode == START_ICON_PROGRESS
    }

    fun setStartIconTintRes(@ColorRes tintColor: Int) {
        setStartIconTint(ContextCompat.getColor(context, tintColor))
    }

    fun setStartIconTint(tintColor: Int) {
        if (startTint != tintColor) {
            startTint = tintColor
            updateStartIcon()
        }
    }
    /*############### Start Icon settings End ############*/


    /*############### Bottom text settings ################*/

    protected open fun updateBottomTextPosition() {
        when {
            hasErrorText -> {
                inputBox?.apply {
                    setStrokeColor(errorColor)
                }
                previousEndIconMode = endIconMode
                if (endIconMode == END_ICON_PASSWORD_TOGGLE)
                    updateEndIcon()
                else
                    setEndIconMode(END_ICON_ERROR)
                bottomContainer?.visibility = View.VISIBLE
                errorTextView?.visibility = View.VISIBLE
                helpTextView?.visibility = View.GONE
            }
            hasHelpText -> {
                inputBox?.apply {
                    setStrokeColor(if (inputText?.isFocused == true) focusedStrokeColor else strokeColor)
                    //ViewCompat.setBackgroundTintList(this, boxStrokeTintList)
                }
                if (endIconMode == END_ICON_ERROR)
                    setEndIconMode(previousEndIconMode)
                else if (endIconMode == END_ICON_PASSWORD_TOGGLE)
                    updateEndIcon()
                bottomContainer?.visibility = View.VISIBLE
                errorTextView?.visibility = View.GONE
                helpTextView?.visibility = View.VISIBLE
            }
            else -> {
                inputBox?.apply {
                    setStrokeColor(if (inputText?.isFocused == true) focusedStrokeColor else strokeColor)
                    //ViewCompat.setBackgroundTintList(this, boxStrokeTintList)
                }
                if (endIconMode == END_ICON_ERROR)
                    setEndIconMode(previousEndIconMode)
                else if (endIconMode == END_ICON_PASSWORD_TOGGLE)
                    updateEndIcon()
                bottomContainer?.visibility = if (showBottomContainer) View.INVISIBLE else View.GONE
            }
        }
    }

    /* Helper */
    fun setHelp(text: String?) {
        hasHelpText = !text.isNullOrBlank()
        helpTextView?.text = text
        updateBottomTextPosition()
    }

    fun setHelp(textId: Int) {
        if (textId != -1) {
            setHelp(context.resources.getString(textId))
        } else {
            setHelp(null)
        }
    }
    /* Helper end */

    /* Error */
    fun setError(text: String?) {
        hasErrorText = !text.isNullOrBlank()
        errorTextView?.text = text
        updateBottomTextPosition()
    }

    fun setError(textId: Int) {
        if (textId != -1) {
            setError(context.resources.getString(textId))
        } else {
            setError(null)
        }
    }

    open fun setErrorException(error: Exception?) {
        setError(error?.message)
    }

    /* Error End*/
    /*################### Bottom text settings End######################*/
    protected open fun boxSettings() {
        inputBox?.apply {
            setStrokeWidthInPx(strokeWidthInPx)
            setBgColor(boxBgColor)
            setStrokeRadiusInPx(strokeRadiusInPx)
        }
    }

    protected fun hasPasswordTransformation(): Boolean {
        return (inputText?.transformationMethod is PasswordTransformationMethod)
    }

    interface TextListener {
        fun onTextChanged(text: String)
    }

    interface IconCheckedListener {
        fun onCheckChanged(isChanged: Boolean)
    }

    interface IconClickListener {
        fun onIconClick()
    }

    protected fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}