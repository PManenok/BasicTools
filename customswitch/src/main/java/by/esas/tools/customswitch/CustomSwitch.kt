package by.esas.tools.customswitch

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

open class CustomSwitch : LinearLayout {
    val TAG: String = CustomSwitch::class.java.simpleName
    val switcher: SwitchMaterial
    val textView: MaterialTextView

    constructor(context: Context) : super(context)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
        val view = inflate(context, R.layout.v_switch, this)
        switcher = view.findViewById(R.id.v_switch_switcher)
        textView = view.findViewById(R.id.v_switch_info)
    }

    /*  Initialize attributes from XML file  */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSwitch)

        val title = typedArray.getString(R.styleable.CustomSwitch_switchTitle)
        val titleStyleId: Int = typedArray.getResourceId(R.styleable.CustomSwitch_switchTitleTextAppearance, -1)

        val info = typedArray.getString(R.styleable.CustomSwitch_switchInfo)
        val infoAlignment = typedArray.getInt(R.styleable.CustomSwitch_switchInfoAlignment, 0)
        val infoStyleId: Int = typedArray.getResourceId(R.styleable.CustomSwitch_switchInfoTextAppearance, -1)

        val startPadding = typedArray.getDimensionPixelSize(R.styleable.CustomSwitch_switchTitlePaddingStart, 0)
        val endPadding = typedArray.getDimensionPixelSize(R.styleable.CustomSwitch_switchTitlePaddingEnd, 0)
        val infoStartPadding = typedArray.getDimensionPixelSize(R.styleable.CustomSwitch_switchInfoPaddingStart, 0)
        val infoEndPadding = typedArray.getDimensionPixelSize(R.styleable.CustomSwitch_switchInfoPaddingEnd, 0)

        /*##########  Switcher Icon  ##########*/
        val thumbTint = typedArray.getResourceId(R.styleable.CustomSwitch_thumbTint, Color.WHITE)
        val trackTint = typedArray.getResourceId(R.styleable.CustomSwitch_trackTint, Color.GREEN)

        typedArray.recycle()

        switcher.apply {
            text = title ?: ""
            if (titleStyleId != -1)
                TextViewCompat.setTextAppearance(switcher, titleStyleId)
            setPadding(paddingLeft + startPadding, paddingTop, paddingRight + endPadding, paddingBottom)
            setThumbTintList(ContextCompat.getColorStateList(context, thumbTint))
            setTrackTintList(ContextCompat.getColorStateList(context, trackTint))
        }

        textView.apply {
            text = info ?: ""
            if (infoStyleId != -1)
                TextViewCompat.setTextAppearance(this, infoStyleId)
            textAlignment = infoAlignment
            setPadding(paddingLeft + infoStartPadding, paddingTop, paddingRight + infoEndPadding, paddingBottom)
        }
    }

    open fun setTitle(text: String) {
        if (!switcher.text.toString().equals(text)) {
            switcher.text = text
        }
    }

    open fun getTitle(): String {
        return switcher.text.toString()
    }

    open fun setInfo(text: String?) {
        if (text.isNullOrBlank()) {
            textView.visibility = View.GONE
        } else {
            if (!textView.text.toString().equals(text)) {
                textView.text = text
            }
            textView.visibility = View.VISIBLE
        }
    }

    open fun getInfo(): String {
        return textView.text.toString()
    }
}