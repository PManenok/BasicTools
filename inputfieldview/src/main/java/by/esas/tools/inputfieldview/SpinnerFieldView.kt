/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.inputfieldview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView

class SpinnerFieldView : ConstraintLayout {
    val TAG: String = SpinnerFieldView::class.java.simpleName

    private var endTint: Int
    private val defaultPaddingLeft: Int
    val inputLayout: TextInputLayout
    val inputText: AppCompatAutoCompleteTextView
    val prefixText: MaterialTextView
    private val hintText: MaterialTextView

    private var hideLabel: Boolean = false

    private var hint: String = ""
    private val inputType: Int = EditorInfo.TYPE_NULL
    var adapter: ArrayAdapter<*> = ArrayAdapter<String>(context, R.layout.i_field_spinner_drop_down)

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
        val view = inflate(context, R.layout.v_field_spinner, this)
        inputLayout = view.findViewById(R.id.v_field_spinner_layout)
        inputText = view.findViewById(R.id.v_field_spinner_input)
        defaultPaddingLeft = inputText.paddingLeft
        prefixText = view.findViewById<MaterialTextView>(R.id.v_field_spinner_prefix_text)
        hintText = view.findViewById(R.id.v_field_spinner_floating_label)
        endTint = ContextCompat.getColor(context, R.color.colorPrimary)
    }

    /*  Initialize attributes from XML file  */
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpinnerFieldView)
        // Hint
        hint = typedArray.getString(R.styleable.SpinnerFieldView_android_hint) ?: ""

        // Start Drawable
        var startDraw: Drawable? = null
            //typedArray.getDrawable(R.styleable.SpinnerFieldView_spinnerStartDrawable)
        val startDrawableRes: Int = typedArray.getResourceId(R.styleable.SpinnerFieldView_spinnerStartDrawable, -1)
        if (startDrawableRes != -1) {
            startDraw = ContextCompat.getDrawable(context, startDrawableRes)
        }
        val startTint: Int =
            typedArray.getColor(R.styleable.SpinnerFieldView_spinnerStartDrawableTint, endTint)

        // End Drawable
        val dropDown: Int = typedArray.getInt(
            R.styleable.SpinnerFieldView_spinnerDropDownLayout,
            R.layout.i_field_spinner_drop_down
        )

        // End Drawable
        var endDrawParam: Drawable? = null
            //typedArray.getDrawable(R.styleable.SpinnerFieldView_spinnerEndDrawable)
        val endDrawableRes: Int = typedArray.getResourceId(R.styleable.SpinnerFieldView_spinnerEndDrawable, -1)
        if (endDrawableRes != -1) {
            endDrawParam = ContextCompat.getDrawable(context, startDrawableRes)
        }

        endTint = typedArray.getColor(R.styleable.SpinnerFieldView_spinnerEndDrawableTint, endTint)
        // Hide label
        hideLabel = typedArray.getBoolean(R.styleable.SpinnerFieldView_spinnerHideLabel, false)


        val enabled = typedArray.getBoolean(R.styleable.SpinnerFieldView_spinnerEnabled, false)
        typedArray.recycle()


        hintText.visibility = if (hideLabel) View.INVISIBLE else View.VISIBLE
        setHint(hint)
        inputText.isEnabled = enabled

        adapter.setDropDownViewResource(dropDown)
        inputText.setDropDownBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_spinner_drop_down))
        inputText.setAdapter(adapter)

        if (startDraw != null)
            setStartDraw(startDraw, startTint)

        inputLayout.setEndIconTintList(ColorStateList.valueOf(endTint))
        if (endDrawParam != null) {
            inputLayout.endIconDrawable = endDrawParam
        }
    }

    fun <T> setSpinnerAdapter(adapter: ArrayAdapter<T>) {
        this.adapter = adapter
        inputText.setAdapter(adapter)
    }

    fun setText(text: String) {
        if (!inputText.text.toString().equals(text)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                inputText.setText(text, false)
            } else {
                inputText.setText("")
            }
        }
    }

    fun getText(): String {
        return inputText.text.toString()
    }

    fun setHideLabel(hide: Boolean) {
        hideLabel = hide
        hintText.visibility = if (hideLabel) View.INVISIBLE else View.VISIBLE
    }

    fun setHint(text: String) {
        if (!(hintText.text?.toString() ?: "").equals(text)) {
            if (!isInEditMode)
                Log.i(TAG, "Is RTL direction ${resources.getBoolean(R.bool.input_is_rtl_direction)}")
            val hint = if (resources.getBoolean(R.bool.input_is_rtl_direction)) {
                "\u202B$text"
            } else text
            hintText.text = hint
        }
    }

    fun setHint(textId: Int) {
        val text = if (textId != -1) context.resources.getString(textId) else ""
        if (!(hintText.text?.toString() ?: "").equals(text)) {
            if (!isInEditMode)
                Log.i(TAG, "Is RTL direction ${resources.getBoolean(R.bool.input_is_rtl_direction)}")
            val hint = if (resources.getBoolean(R.bool.input_is_rtl_direction)) {
                "\u202B$text"
            } else text
            hintText.text = hint
        }
    }

    fun getHint(): String {
        return hintText.text?.toString() ?: ""
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

    private fun setStartDraw(startDraw: Drawable, startTint: Int) {
        inputLayout.startIconDrawable = startDraw
        inputLayout.setStartIconTintList(ColorStateList.valueOf(startTint))
    }

    fun spinnerEnabled(value: Boolean) {
        inputText.isEnabled = value
    }

    fun spinnerEnabled(): Boolean {
        return inputText.isEnabled
    }

    fun disable() {
        //this.alpha = 0.55f
        inputLayout.isEndIconVisible = false
    }

    fun enable() {
        //this.alpha = 1f
        inputLayout.isEndIconVisible = true
    }

    // Error
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