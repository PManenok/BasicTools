package by.esas.tools.inputfieldview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.util.concurrent.Semaphore

open class InputFieldView : ConstraintLayout {
    open val TAG: String = InputFieldView::class.java.simpleName

    val inputLayout: TextInputLayout
    val inputText: TextInputEditText
    val prefixText: MaterialTextView
    protected val labelText: MaterialTextView
    private val progress: ProgressBar

    private var startDraw: Drawable? = null
    protected var startTint: Int
    private var endDraw: Drawable? = null
    protected var endTint: Int
    protected val passwordToggleRes: Int = R.drawable.selector_password_toggle
    protected val defaultPaddingLeft: Int
    protected var currentPaddingLeft: Int
    protected var label: String = ""
    private var hint: String = ""
    private var endIconMode: Int = TextInputLayout.END_ICON_NONE
    protected var defaultInputType: Int = EditorInfo.TYPE_CLASS_TEXT
    protected var prefix: String = ""
    protected var hasStartDrawable: Boolean = false
    protected var hideLabel: Boolean = false
        set(value) {
            field = value
            labelText.visibility = if (hideLabel) View.GONE else View.VISIBLE
        }
    protected open val defaultLabelType: Int = LabelType.ON_LINE

    private val uncheckedResDef: Int = R.drawable.ic_check_box_unfilled
    private val checkedResDef: Int = R.drawable.ic_check_box_filled
    private var uncheckedDrawable: Drawable? = null
    private var checkedDrawable: Drawable? = null
    private var isChecked: Boolean = false
    private var startIconIsCheckable: Boolean = false
    private var checkedListener: StartIconCheckedListener? = null
    private val checkSemaphore: Semaphore = Semaphore(1)

    /*############################ TextListener ################################*/
    private var textListener: TextListener? = null
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            textListener?.onTextChanged(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }
    /*############################ TextListener End ################################*/

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

    init {
        val view = inflate(context, R.layout.v_field_editable, this)
        inputLayout = view.findViewById<TextInputLayout>(R.id.v_field_editable_layout)
        inputText = view.findViewById<TextInputEditText>(R.id.v_field_editable_input)
        defaultPaddingLeft = inputText.paddingLeft
        currentPaddingLeft = defaultPaddingLeft
        prefixText = view.findViewById<MaterialTextView>(R.id.v_field_editable_prefix_text)
        labelText = view.findViewById(R.id.v_field_editable_floating_label)
        startTint = ContextCompat.getColor(context, R.color.colorPrimary)
        endTint = startTint
        inputText.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus -> if (hasFocus) inputLayout?.error = null }
        progress = view.findViewById(R.id.v_field_editable_progress)
    }

    /*  Initialize attributes from XML file  */
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputFieldView)
        // Label
        label = typedArray.getString(R.styleable.InputFieldView_inputLabel) ?: ""
        // Text
        val text: String = typedArray.getString(R.styleable.InputFieldView_android_text) ?: ""
        // Hint
        hint = typedArray.getString(R.styleable.InputFieldView_android_hint) ?: ""
        // End Icon Mode
        endIconMode = typedArray.getInt(R.styleable.InputFieldView_inputEndIconMode, TextInputLayout.END_ICON_NONE)
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
        val editable: Boolean =
            typedArray.getBoolean(R.styleable.InputFieldView_inputEditable, true)
        // Prefix
        val prefix = typedArray.getString(R.styleable.InputFieldView_inputPrefix) ?: ""

        // Start Drawable
        startDraw = typedArray.getDrawable(R.styleable.InputFieldView_inputStartDrawable)
        // Start Tint
        startTint =
            typedArray.getColor(R.styleable.InputFieldView_inputStartDrawableTint, startTint)
        // Start Icon is Checkable
        startIconIsCheckable =
            typedArray.getBoolean(R.styleable.InputFieldView_inputStartIconIsCheckable, false)

        // End Drawable
        val endDrawParam: Drawable? =
            typedArray.getDrawable(R.styleable.InputFieldView_inputEndDrawable)
        endDraw = if (endDrawParam != null || (endIconMode == TextInputLayout.END_ICON_PASSWORD_TOGGLE && passwordToggleRes != -1))
            endDrawParam ?: ContextCompat.getDrawable(context, passwordToggleRes) else null
        // End Tint
        endTint = typedArray.getColor(R.styleable.InputFieldView_inputEndDrawableTint, endTint)

        // Hide label
        hideLabel = typedArray.getBoolean(R.styleable.InputFieldView_inputHideLabel, false)


        val checkedDraw: Drawable? =
            typedArray.getDrawable(R.styleable.InputFieldView_inputCheckedDrawable)
        val uncheckedDraw: Drawable? =
            typedArray.getDrawable(R.styleable.InputFieldView_inputUncheckedDrawable)

        // End Icon Mode
        val labelType = typedArray.getInt(R.styleable.InputFieldView_inputLabelType, defaultLabelType)
        //Label BG color
        val labelBg = typedArray
            .getColor(R.styleable.InputFieldView_inputLabelBgColor, ContextCompat.getColor(context, R.color.colorBackground))

        typedArray.recycle()

        checkedDrawable = checkedDraw ?: ContextCompat.getDrawable(context, checkedResDef)
        uncheckedDrawable = uncheckedDraw ?: ContextCompat.getDrawable(context, uncheckedResDef)

        labelText.setBackgroundColor(labelBg)
        labelText.visibility = if (hideLabel) View.GONE else View.VISIBLE

        inputText.setText(text)
        inputText.hint = hint
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            inputText.textDirection = textDirection
        }
        inputText.maxLines = maxLines
        setInputLabel(label)
        inputText.inputType = inputType
        inputText.isEnabled = editable
        if (startIconIsCheckable) {
            setCheckableStartIcon()
        } else if (startDraw != null)
            setStartDraw(startDraw!!)

        inputLayout.endIconMode = endIconMode
        inputLayout.setEndIconTintList(ColorStateList.valueOf(endTint))
        if (endDraw != null) {
            inputLayout.endIconDrawable = endDraw
        }
        setInputPrefix(prefix)
        setLabelType(labelType)
    }

    fun setDefaultValues() {
        inputText.maxLines = 1
        inputText.inputType = defaultInputType
        inputText.isEnabled = true
        if (startIconIsCheckable) {
            setCheckableStartIcon()
        } else if (startDraw != null)
            setStartDraw(startDraw!!)

        setInputPrefix(prefix)
        inputLayout.endIconMode = endIconMode
        inputLayout.setEndIconTintList(ColorStateList.valueOf(endTint))
        if (endDraw != null) {
            inputLayout.endIconDrawable = endDraw
        }
    }

    /*############################ Getters ################################*/
    open fun getText(): String {
        return inputText.text.toString()
    }

    fun getInputLabel(): String {
        return labelText.text?.toString() ?: ""
    }

    fun isEditable(): Boolean {
        return inputText.isEnabled
    }

    open fun getTextWithPrefix(): String {
        return prefix + inputText.text.toString()
    }

    fun isChecked(): Boolean {
        return isChecked
    }
    /*############################ Getters End ################################*/


    /*############################ Setters ################################*/
    open fun setText(text: String) {
        if (!inputText.text.toString().equals(text)) {
            inputText.setText(text)
        }
    }

    fun setTextListener(listener: TextListener?) {
        textListener = listener
        if (textListener != null) {
            inputText.addTextChangedListener(textWatcher)
        } else inputText.removeTextChangedListener(textWatcher)
    }

    open fun setInputPrefix(prefix: String) {
        this.prefix = prefix
        if (prefix.isBlank()) {
            prefixText.visibility = View.INVISIBLE
            prefixText.text = prefix
            inputText.setPadding(
                defaultPaddingLeft, inputText.paddingTop,
                inputText.paddingRight, inputText.paddingBottom
            )
        } else {
            prefixText.visibility = View.VISIBLE
            prefixText.text = prefix
            prefixText.setPadding(
                currentPaddingLeft, 0, 0, 0
            )
            inputText.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            prefixText.height = inputText.measuredHeight - 1
            prefixText.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            var left = prefixText.measuredWidth
            if (hasStartDrawable) {
                left -= (currentPaddingLeft - defaultPaddingLeft)
            }
            inputText.post {
                inputText.setPadding(
                    left, inputText.paddingTop,
                    inputText.paddingRight, inputText.paddingBottom
                )
            }
        }
    }

    open fun setLabelType(labelType: Int) {
        when (labelType) {
            LabelType.ON_TOP -> {
                labelText.background = null
                inputLayout.isHintEnabled = false
                inputLayout.minimumHeight = resources.getDimensionPixelOffset(R.dimen.input_text_layout_height_without_hint)
                labelText.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                val prefixParams = (prefixText.layoutParams as ConstraintLayout.LayoutParams)
                prefixParams.setMargins(prefixParams.leftMargin, 0, prefixParams.rightMargin, prefixParams.bottomMargin)
                if (!hideLabel) {
                    val params: ConstraintLayout.LayoutParams = inputLayout.layoutParams as ConstraintLayout.LayoutParams
                    params.setMargins(params.leftMargin, labelText.measuredHeight, params.rightMargin, params.bottomMargin)
                }
            }
            LabelType.ON_LINE -> {
                if (hideLabel) {
                    val params: ConstraintLayout.LayoutParams = inputLayout.layoutParams as ConstraintLayout.LayoutParams
                    params.setMargins(params.leftMargin, 0, params.rightMargin, params.bottomMargin)
                }
            }
            else -> {
            }
        }
    }

    open fun setInputLabel(text: String) {
        if (!(labelText.text?.toString() ?: "").equals(text)) {
            if (!isInEditMode)
                Log.i(TAG, "Is RTL direction ${resources.getBoolean(R.bool.is_rtl_direction)}")
            val hint = if (resources.getBoolean(R.bool.is_rtl_direction)) {
                "\u202B$text"
            } else text
            labelText.text = hint
        }
    }

    open fun setInputLabel(textId: Int) {
        val text = if (textId != -1) context.resources.getString(textId) else ""
        if (!(labelText.text?.toString() ?: "").equals(text)) {
            if (!isInEditMode)
                Log.i(TAG, "Is RTL direction ${resources.getBoolean(R.bool.is_rtl_direction)}")
            val hint = if (resources.getBoolean(R.bool.is_rtl_direction)) {
                "\u202B$text"
            } else text
            labelText.text = hint
        }
    }

    fun setEditable(value: Boolean) {
        inputText.isEnabled = value
    }

    fun setMaxLines(value: Int) {
        inputText.maxLines = value
    }

    fun setInputType(inputType: Int = defaultInputType) {
        inputText.inputType = inputType
    }

    fun setEndIconMode(mode: Int) {
        if (endIconMode != mode) {
            endIconMode = mode
            inputLayout.endIconMode = mode
            inputLayout.setEndIconTintList(ColorStateList.valueOf(endTint))
        }
    }

    fun setEndIconDrawable(@DrawableRes endIcon: Int) {
        setEndIconDrawable(ContextCompat.getDrawable(context, endIcon))
    }

    fun setEndIconDrawable(endIcon: Drawable?) {
        inputLayout.endIconDrawable = endIcon
    }

    fun setEndIconTintRes(@ColorRes tintColor: Int) {
        endTint = ContextCompat.getColor(context, tintColor)
        inputLayout.setEndIconTintList(ColorStateList.valueOf(endTint))
    }

    fun setEndIconTint(tintColor: Int) {
        endTint = tintColor
        inputLayout.setEndIconTintList(ColorStateList.valueOf(endTint))
    }

    private fun setCheckableStartIcon() {
        (if (isChecked) checkedDrawable else uncheckedDrawable)?.let { setStartDraw(it) }
        inputLayout.setStartIconOnClickListener { view ->
            if (checkSemaphore.tryAcquire()) {
                view.isClickable = false
                isChecked = !isChecked
                (if (isChecked) checkedDrawable else uncheckedDrawable)?.let { setStartDraw(it) }
                //inputLayout.startIconDrawable = ContextCompat.getDrawable(context, if (isChecked) checkedRes else uncheckedRes)
                checkedListener?.onCheckChanged(isChecked)
                view.isClickable = true
                checkSemaphore.release()
            }
        }
    }

    fun setOnCheckedListener(listener: StartIconCheckedListener?) {
        this.checkedListener = listener
    }

    fun setChecked(checked: Boolean) {
        if (isChecked != checked) {
            isChecked = checked
            inputLayout.startIconDrawable = if (isChecked) checkedDrawable else uncheckedDrawable
            checkedListener?.onCheckChanged(isChecked)
        }
    }

    fun setFirstIconCheckable(checkable: Boolean) {
        setFirstIconCheckable(checkable, false)
    }

    fun setFirstIconCheckable(checkable: Boolean, isChecked: Boolean) {
        startIconIsCheckable = checkable
        if (startIconIsCheckable) {
            this.isChecked = isChecked
            setCheckableStartIcon()
        } else {
            this.isChecked = false
            if (startDraw != null)
                setStartDraw(startDraw!!)
            else {
                inputLayout.startIconDrawable = null
                hasStartDrawable = false
                currentPaddingLeft = defaultPaddingLeft
            }
        }
    }

    private fun setStartDraw(startDraw: Drawable) {
        inputLayout.startIconDrawable = startDraw
        inputLayout.setStartIconTintList(ColorStateList.valueOf(startTint))
        hasStartDrawable = true
        currentPaddingLeft = 80 + defaultPaddingLeft
    }

    /* Error */
    fun setError(text: String?) {
        val errorText = if (text.isNullOrBlank()) null else text
        inputLayout.error = errorText
        inputLayout.isErrorEnabled = true
    }

    fun setError(textId: Int) {
        if (textId != -1) {
            setError(context.resources.getString(textId))
        } else {
            setError(null)
        }
    }

    fun setErrorException(error: Exception?) {
        if (error != null)
            setError(error.message)
        else {
            setError(null)
        }
    }
    /* Error End*/

    /*############################ Setters End ################################*/

    /*############################ Progress ################################*/
    fun isInProgress(value: Boolean) {
        progress.visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

    fun isInProgress(): Boolean {
        return progress.visibility == View.VISIBLE
    }
    /*############################ Progress End ################################*/


    interface TextListener {
        fun onTextChanged(text: String)
    }

    interface StartIconCheckedListener {
        fun onCheckChanged(isChanged: Boolean)
    }

    class LabelType {
        companion object {
            const val ON_TOP: Int = 0
            const val ON_LINE: Int = 1
        }
    }
}