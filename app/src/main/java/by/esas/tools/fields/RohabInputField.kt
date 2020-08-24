package by.esas.tools.fields

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.core.text.BidiFormatter
import by.esas.tools.R
import by.esas.tools.getLocale
import by.esas.tools.inputfieldview.InputFieldView

class RohabInputField : InputFieldView {
    override val TAG: String = RohabInputField::class.java.simpleName
    //val logger: ILogger<AppErrorStatusEnum> = LoggerImpl()

    override val defaultLabelType: Int = LabelType.ON_TOP
    private var formattedText: String = ""

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
        //logger.setTag(TAG)
    }

    /*  Initialize attributes from XML file  */
    private fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RohabInputField)
        // Label
        label = typedArray.getString(R.styleable.RohabInputField_inputLabel) ?: ""
        // Hint
        formattedText = typedArray.getString(R.styleable.RohabInputField_inputHintFormattedText) ?: ""

        typedArray.recycle()

        labelText.setText("")
        if (formattedText.isNotBlank()) {
            setInputLabel(label, formattedText)
        } else setInputLabel(label)
    }

    fun setInputLabel(text: String, formattedText: String) {
        val formatter: BidiFormatter = BidiFormatter.getInstance()
        val hintTxt = String.format(text, formatter.unicodeWrap(formattedText))
        setInputLabel(hintTxt)
    }

    fun setInputLabel(text: String, formattedId: Int) {
        val formatter: BidiFormatter = BidiFormatter.getInstance()
        val formattedText = if (formattedId != -1) context.resources.getString(formattedId) else ""
        val hintTxt = String.format(text, formatter.unicodeWrap(formattedText))
        setInputLabel(hintTxt)
    }

    override fun setInputLabel(text: String) {
        if (!(labelText.text?.toString() ?: "").equals(text)) {
            /*if (!isInEditMode)
                logger.log("Is RTL direction ${resources.getBoolean(R.bool.is_rtl_direction)}")*/
            var hint = text
            hint = if (hint.contains("(") && hint.contains(")")) {
                val beforeBrace = text.substringBefore("(")
                val afterFirstBrace = text.substringAfter("(")
                val betweenBraces = afterFirstBrace.substringBefore(")")
                val afterSecondBrace = afterFirstBrace.substringAfter(")", "")
                val builder = StringBuilder("")
                builder.append(beforeBrace.toUpperCase(getLocale(context))).append("(")
                    .append(betweenBraces).append(")").append(afterSecondBrace.toUpperCase(
                        getLocale(
                            context
                        )
                    ))
                builder.toString()
            } else hint.toUpperCase(getLocale(context))
            labelText.text = hint//HtmlCompat.fromHtml(hint,HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    override fun setInputLabel(textId: Int) {
        val text = if (textId != -1) context.resources.getString(textId) else ""
        setInputLabel(text)
    }

    fun setInputLabel(textId: Int, formattedText: String) {
        val text = if (textId != -1) context.resources.getString(textId) else ""
        val formatter: BidiFormatter = BidiFormatter.getInstance()
        val hintTxt = String.format(text, formatter.unicodeWrap(formattedText))
        setInputLabel(hintTxt)
    }

    fun setInputLabel(textId: Int, formattedId: Int) {
        val text = if (textId != -1) context.resources.getString(textId) else ""
        val formattedText = if (formattedId != -1) context.resources.getString(formattedId) else ""
        val formatter: BidiFormatter = BidiFormatter.getInstance()
        val hintTxt = String.format(text, formatter.unicodeWrap(formattedText))
        setInputLabel(hintTxt)
    }
}