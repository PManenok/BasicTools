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
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.util.concurrent.Semaphore

class InputFieldView : ConstraintLayout {
    val TAG: String = InputFieldView::class.java.simpleName

    private var startDraw: Drawable? = null
    private var startTint: Int
    private var endTint: Int
    private val passwordToggleRes: Int = R.drawable.selector_password_toggle
    private val defaultPaddingLeft: Int
    private var currentPaddingLeft: Int
    val inputLayout: TextInputLayout
    val inputText: TextInputEditText
    val prefixText: MaterialTextView
    private val layoutText: MaterialTextView
    private var label: String = ""
    private var hint: String = ""
    private var endIconMode: Int = TextInputLayout.END_ICON_NONE
    private var inputType: Int = EditorInfo.TYPE_CLASS_TEXT
    private var prefix: String = ""
    private var hasStartDrawable: Boolean = false
    private var hideLabel: Boolean = false

    private val uncheckedResDef: Int = R.drawable.ic_check_box_unfilled
    private val checkedResDef: Int = R.drawable.ic_check_box_filled
    private var uncheckedDrawable: Drawable? = null
    private var checkedDrawable: Drawable? = null
    private var isChecked: Boolean = false
    private var startIconIsCheckable: Boolean = false
    private var checkedListener: StartIconCheckedListener? = null
    private val checkSemaphore: Semaphore = Semaphore(1)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
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
        layoutText = view.findViewById(R.id.v_field_editable_floating_label)
        startTint = ContextCompat.getColor(context, R.color.colorPrimary)
        endTint = startTint
        inputText.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus -> if (hasFocus) inputLayout?.error = null }
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
        inputType = typedArray.getInt(R.styleable.InputFieldView_android_inputType, EditorInfo.TYPE_CLASS_TEXT)
        // Max Lines
        val maxLines: Int = typedArray.getInt(R.styleable.InputFieldView_android_maxLines, 1)
        // Text Direction
        val textDirection: Int =
            typedArray.getInt(R.styleable.InputFieldView_android_textDirection, View.TEXT_DIRECTION_ANY_RTL)
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
        val endDraw =
            if (endDrawParam != null || (endIconMode == TextInputLayout.END_ICON_PASSWORD_TOGGLE && passwordToggleRes != -1))
                endDrawParam ?: ContextCompat.getDrawable(context, passwordToggleRes) else null
        // End Tint
        endTint = typedArray.getColor(R.styleable.InputFieldView_inputEndDrawableTint, endTint)

        // Hide label
        hideLabel = typedArray.getBoolean(R.styleable.InputFieldView_inputHideLabel, false)


        val checkedDraw: Drawable? =
            typedArray.getDrawable(R.styleable.InputFieldView_inputCheckedDrawable)
        val uncheckedDraw: Drawable? =
            typedArray.getDrawable(R.styleable.InputFieldView_inputUncheckedDrawable)

        typedArray.recycle()

        checkedDrawable = checkedDraw ?: ContextCompat.getDrawable(context, checkedResDef)
        uncheckedDrawable = uncheckedDraw ?: ContextCompat.getDrawable(context, uncheckedResDef)


        layoutText.visibility = if (hideLabel) View.INVISIBLE else View.VISIBLE

        inputText.setText(text)
        inputText.hint = hint
        inputText.textDirection = textDirection
        inputText.maxLines = maxLines
        setInputLabel(label)
        inputText.inputType = inputType
        inputText.isEnabled = editable
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

    interface StartIconCheckedListener {
        fun onCheckChanged(isChanged: Boolean)
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

    fun isChecked(): Boolean {
        return isChecked
    }

    fun setInputPrefix(prefix: String) {
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
            prefixText.height = inputText.height - 1
            var left = prefixText.width
            if (hasStartDrawable) {
                left -= (currentPaddingLeft - defaultPaddingLeft)
            }
            inputText.setPadding(
                left, inputText.paddingTop,
                inputText.paddingRight, inputText.paddingBottom
            )
        }
    }

    fun setText(text: String) {
        if (!inputText.text.toString().equals(text)) {
            inputText.setText(text)
        }
    }

    fun getText(): String {
        return inputText.text.toString()
    }

    fun getTextWithPrefix(): String {
        return prefix + inputText.text.toString()
    }

    fun setHideLabel(hide: Boolean) {
        hideLabel = hide
        layoutText.visibility = if (hideLabel) View.INVISIBLE else View.VISIBLE
    }

    fun setInputLabel(text: String) {
        if (!(layoutText.text?.toString() ?: "").equals(text)) {
            if (!isInEditMode)
                Log.i(TAG, "Is RTL direction ${resources.getBoolean(R.bool.is_rtl_direction)}")
            val hint = if (resources.getBoolean(R.bool.is_rtl_direction)) {
                "\u202B$text"
            } else text
            layoutText.text = hint
        }
    }

    fun setInputLabel(textId: Int) {
        val text = if (textId != -1) context.resources.getString(textId) else ""
        if (!(layoutText.text?.toString() ?: "").equals(text)) {
            if (!isInEditMode)
                Log.i(TAG, "Is RTL direction ${resources.getBoolean(R.bool.is_rtl_direction)}")
            val hint = if (resources.getBoolean(R.bool.is_rtl_direction)) {
                "\u202B$text"
            } else text
            layoutText.text = hint
        }
    }

    fun getHint(): String {
        return layoutText.text?.toString() ?: ""
    }

    fun setEditable(value: Boolean) {
        inputText.isEnabled = value
    }

    fun isEditable(): Boolean {
        return inputText.isEnabled
    }

    fun setMaxLines(value: Int) {
        inputText.maxLines = value
    }

    fun setEndIconMode(mode: Int) {
        if (endIconMode != mode) {
            endIconMode = mode
            inputLayout.endIconMode = mode
            inputLayout.setEndIconTintList(ColorStateList.valueOf(endTint))
        }
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

    private fun setStartDraw(startDraw: Drawable) {
        inputLayout.startIconDrawable = startDraw
        inputLayout.setStartIconTintList(ColorStateList.valueOf(startTint))
        hasStartDrawable = true
        currentPaddingLeft = 80 + defaultPaddingLeft
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

    interface TextListener {
        fun onTextChanged(text: String)
    }

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

    fun setTextListener(listener: TextListener?) {
        textListener = listener
        if (textListener != null) {
            inputText.addTextChangedListener(textWatcher)
        } else inputText.removeTextChangedListener(textWatcher)
    }

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
}