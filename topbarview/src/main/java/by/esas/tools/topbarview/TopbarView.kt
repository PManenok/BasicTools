package by.esas.tools.topbarview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt

open class TopbarView : LinearLayout {
    val TAG: String = TopbarView::class.java.simpleName
    val dividerView: View
    val containerView: ConstraintLayout
    val navIconView: AppCompatImageView
    val actionIconView: AppCompatImageView
    val startActionView: MaterialTextView
    val endActionView: MaterialTextView
    val titleView: MaterialTextView

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
        val view = inflate(context, R.layout.v_top_bar, this)
        dividerView = view.findViewById(R.id.v_top_bar_divider)
        containerView = view.findViewById(R.id.v_top_bar_container)
        navIconView = view.findViewById(R.id.v_top_bar_back)
        startActionView = view.findViewById(R.id.v_top_bar_left_action)
        titleView = view.findViewById(R.id.v_top_bar_title)
        endActionView = view.findViewById(R.id.v_top_bar_right_action)
        actionIconView = view.findViewById(R.id.v_top_bar_right_action_icon)
    }

    protected val defDividerHeight = dpToPx(1)
    protected val defContainerHeight = dpToPx(35)
    protected val defDividerColor = Color.parseColor("#D9E1EE")
    protected val defIconColor = Color.RED
    protected val defIconsPadding = dpToPx(5).toInt()
    protected val defPadding: Int = 0

    protected val defShowNavIcon = false
    protected val defNavIconRes = -1
    protected val defNavIconSize = 0
    protected val defShowActionIcon = false
    protected val defActionIconRes = -1
    protected val defActionIconSize = 0

    protected var showNavIcon: Boolean = false
    protected var navIconRes: Int = -1
    protected var navIconSize: Int = 0
    protected var navIconTint: Int = defIconColor
    protected var navIconPadding: Int = defIconsPadding

    //заменила в названиях showEndIcon, endIconRes, endIconSize, endIconTint, endIconPadding - end на Action.
    // Т.к. у нас есть navIcon и ActionIcon, а start и end относятся к текстовым полям.
    // и в сочетании endIcon созвалась путаница - это название относится к текстовому полю или к icon
    protected var showActionIcon: Boolean = false
    protected var actionIconRes: Int = -1
    protected var actionIconSize: Int = 0
    protected var actionIconTint: Int = defIconColor
    protected var actionIconPadding: Int = defIconsPadding

    protected fun initAttrs(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopbarView)

        // container variables
        val topPadding = typedArray.getDimensionPixelSize(
            R.styleable.TopbarView_barContainerPaddingTop,
            defPadding
        )
        val bottomPadding = typedArray.getDimensionPixelSize(
            R.styleable.TopbarView_barContainerPaddingBottom,
            defPadding
        )
        val startPadding = typedArray.getDimensionPixelSize(
            R.styleable.TopbarView_barContainerPaddingStart,
            defPadding
        )
        val endPadding = typedArray.getDimensionPixelSize(
            R.styleable.TopbarView_barContainerPaddingEnd,
            defPadding
        )
        val containerHeight =
            typedArray.getDimensionPixelSize(
                R.styleable.TopbarView_barContainerHeight,
                defContainerHeight.toInt()
            )

        // back arrow variables
        navIconRes = typedArray.getResourceId(R.styleable.TopbarView_barNavIcon, defNavIconRes)
        navIconSize =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barNavIconSize, defNavIconSize)
        navIconTint = typedArray.getColor(R.styleable.TopbarView_barNavIconTint, defIconColor)
        navIconPadding = typedArray
            .getDimensionPixelSize(R.styleable.TopbarView_barNavIconPadding, defIconsPadding)
        showNavIcon = typedArray.getBoolean(R.styleable.TopbarView_barShowNavIcon, defShowNavIcon)

        // start action variables
        val startActionText = typedArray.getString(R.styleable.TopbarView_barStartActionText)
        val startActionStyle: Int =
            typedArray.getResourceId(R.styleable.TopbarView_barStartActionTextAppearance, -1)
        val startActionPaddingTop =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barStartActionPaddingTop, 0)
        val startActionPaddingBottom =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barStartActionPaddingBottom, 0)
        val startActionPaddingStart =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barStartActionPaddingStart, 0)
        val startActionPaddingEnd =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barStartActionPaddingEnd, 0)

        // title variables
        val title = typedArray.getString(R.styleable.TopbarView_barTitleText)
        val titleStyleId: Int =
            typedArray.getResourceId(R.styleable.TopbarView_barTitleTextAppearance, -1)
        val titlePaddingStart =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barTitlePaddingStart, 0)
        val titlePaddingEnd =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barTitlePaddingEnd, 0)

        // end action variables
        val endActionText = typedArray.getString(R.styleable.TopbarView_barEndActionText)
        val endActionStyle: Int =
            typedArray.getResourceId(R.styleable.TopbarView_barEndActionTextAppearance, -1)
        val endActionPaddingTop =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barEndActionPaddingTop, 0)
        val endActionPaddingBottom =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barEndActionPaddingBottom, 0)
        val endActionPaddingStart =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barEndActionPaddingStart, 0)
        val endActionPaddingEnd =
            typedArray.getDimensionPixelSize(R.styleable.TopbarView_barEndActionPaddingEnd, 0)

        // end action icon variables
        actionIconRes = typedArray.getResourceId(R.styleable.TopbarView_barActionIcon, defActionIconRes)
        actionIconSize = typedArray.getDimensionPixelSize(
            R.styleable.TopbarView_barActionIconSize,
            defActionIconSize
        )
        actionIconTint = typedArray.getColor(R.styleable.TopbarView_barActionIconTint, defIconColor)
        actionIconPadding = typedArray
            .getDimensionPixelSize(R.styleable.TopbarView_barActionIconPadding, defIconsPadding)
        showActionIcon =
            typedArray.getBoolean(R.styleable.TopbarView_barShowActionIcon, defShowActionIcon)

        //divider variables
        val showDiv = typedArray.getBoolean(R.styleable.TopbarView_barShowDivider, false)
        val divHeight =
            typedArray.getDimension(R.styleable.TopbarView_barDividerHeight, defDividerHeight)
        val dividerColor =
            typedArray.getColor(R.styleable.TopbarView_barDividerColor, defDividerColor)

        typedArray.recycle()

        val titleWidthIsWrap = !startActionText.isNullOrBlank() || !endActionText.isNullOrBlank()

        setContainerPaddings(startPadding, topPadding, endPadding, bottomPadding)

        setContainerHeight(containerHeight)

        if (startActionText.isNullOrBlank()) {
            startActionView.visibility = View.GONE
        } else {
            startActionView.apply {
                text = startActionText
                if (startActionStyle != -1)
                    TextViewCompat.setTextAppearance(this, startActionStyle)
                setPadding(
                    paddingLeft + startActionPaddingStart,
                    paddingTop + startActionPaddingTop,
                    paddingRight + startActionPaddingEnd,
                    paddingBottom + startActionPaddingBottom
                )
            }
        }

        updateNavigationIcon()

        if (endActionText.isNullOrBlank()) {
            endActionView.visibility = View.GONE
        } else {
            endActionView.apply {
                text = endActionText
                if (endActionStyle != -1)
                    TextViewCompat.setTextAppearance(this, endActionStyle)
                setPadding(
                    paddingLeft + endActionPaddingStart,
                    paddingTop + endActionPaddingTop,
                    paddingRight + endActionPaddingEnd,
                    paddingBottom + endActionPaddingBottom
                )
            }
        }

        updateActionIcon()

        titleView.apply {
            text = title
            if (titleStyleId != -1)
                TextViewCompat.setTextAppearance(this, titleStyleId)
            setPadding(
                paddingLeft + titlePaddingStart,
                paddingTop,
                paddingRight + titlePaddingEnd,
                paddingBottom
            )
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            val params = layoutParams as ConstraintLayout.LayoutParams
            if (titleWidthIsWrap)
                getLayoutParamsForWrappingWidth(params)
            else
                getLayoutParamsForZeroWidth(params)
            layoutParams = params
        }

        dividerView.setBackgroundColor(dividerColor)
        setDividerHeight(divHeight.toInt())
        dividerView.visibility = if (showDiv) View.VISIBLE else View.GONE
    }

    /*region ############################ Title ################################*/
    fun setTitle(text: String?) {
        titleView.text = text
    }

    fun setTitle(textRes: Int?) {
        if (textRes != null)
            titleView.setText(textRes)
    }

    fun isTitleSingleLine(value: Boolean) {
        titleView.isSingleLine = value
    }

    fun setTitlePaddings(paddingStart: Int, paddingEnd: Int) {
        titleView.setPadding(
            paddingLeft + paddingStart,
            paddingTop,
            paddingRight + paddingEnd,
            paddingBottom
        )
    }

    fun setEllipsize(truncateAt: TextUtils.TruncateAt) {
        titleView.ellipsize = truncateAt
    }

    fun setDefaultTitleParams() {
        titleView.apply {
            setPadding(
                paddingLeft + defPadding,
                paddingTop,
                paddingRight + defPadding,
                paddingBottom
            )
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            val params = layoutParams as ConstraintLayout.LayoutParams
            getLayoutParamsForZeroWidth(params)
            layoutParams = params
        }
    }

    protected open fun getLayoutParamsForWrappingWidth(params: ConstraintLayout.LayoutParams): ConstraintLayout.LayoutParams {
        return params.apply {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            constrainedWidth = true
            startToEnd = -1
            endToStart = -1
            startToStart = R.id.v_top_bar_container
            endToEnd = R.id.v_top_bar_container
        }
    }

    protected open fun getLayoutParamsForZeroWidth(params: ConstraintLayout.LayoutParams): ConstraintLayout.LayoutParams {
        return params.apply {
            width = 0
            constrainedWidth = false
            startToStart = -1
            endToEnd = -1
            startToEnd = navIconView.id
            endToStart = actionIconView.id
        }
    }
    /*endregion ############################ Title ################################*/

    /*region ############################ Divider ################################*/
    fun setDividerVisibility(value: Boolean) {
        dividerView.visibility = if (value) View.VISIBLE else View.GONE
    }

    fun setDividerHeight(dividerHeight: Int) {
        dividerView.layoutParams.apply {
            height = dividerHeight
        }
    }

    fun setDividerColor(color: Int) {
        dividerView.setBackgroundColor(color)
    }
    /*endregion ############################ Divider ################################*/

    /*region ############################ Icons ################################*/
    protected open fun updateIconVisibility(icon: View, showIcon: Boolean, hasActionText: Boolean) {
        icon.visibility = when {
            !hasActionText && showIcon -> View.VISIBLE
            hasActionText -> View.GONE
            else -> View.INVISIBLE
        }
    }

    protected open fun updateIconSize(iconSize: Int, icon: AppCompatImageView) {
        val size: Int = if (iconSize == 0) dpToPx(32).roundToInt() else iconSize
        val params = icon.layoutParams
        if (params != null && (params.width != size || params.height != size)) {
            params.width = size
            params.height = size
            icon.layoutParams = params
        }
    }

    open fun setButtonsEnabled(value: Boolean) {
        navIconView.isClickable = value
        startActionView.isClickable = value
        actionIconView.isClickable = value
        endActionView.isClickable = value
    }

    /*############################ Start Action View ################################*/
    open fun setStartActionListener(listener: OnClickListener) {
        navIconView.setOnClickListener(listener)
        startActionView.setOnClickListener(listener)
    }

    fun setStartActionViewVisibility(value: Boolean) {
        if (value) {
            startActionView.visibility = View.VISIBLE
        } else {
            startActionView.visibility = View.GONE
        }
    }

    /*############################ End Action View ################################*/
    open fun setEndActionListener(listener: OnClickListener) {
        actionIconView.setOnClickListener(listener)
        endActionView.setOnClickListener(listener)
    }

    fun setEndActionViewVisibility(value: Boolean) {
        if (value) {
            endActionView.visibility = View.VISIBLE
        } else {
            endActionView.visibility = View.GONE
        }
    }

    /*############################ Navigation Icon ################################*/
    fun setNavIconImage(resId: Int) {
        navIconRes = resId
        updateNavigationIcon()
    }

    open fun setNavIconVisibility(value: Boolean) {
        showNavIcon = value
        val actionTextIsVisible = startActionView.visibility == View.VISIBLE
        updateIconVisibility(navIconView, value, actionTextIsVisible)
    }

    protected open fun updateNavigationIcon() {
        navIconView.apply {
            if (navIconRes != -1) {
                updateIconSize(navIconSize, this)
                setImageResource(navIconRes)
                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(navIconTint))
                setPadding(navIconPadding, navIconPadding, navIconPadding, navIconPadding)
                setNavIconVisibility(showNavIcon)
            } else {
                this.visibility = View.GONE
            }
        }
    }

    /*############################ Action Icon ################################*/
    fun setActionIconImage(resId: Int) {
        actionIconRes = resId
        updateActionIcon()
    }

    open fun setActionIconVisibility(value: Boolean) {
        showActionIcon = value
        val actionTextIsVisible = endActionView.visibility == View.VISIBLE
        updateIconVisibility(actionIconView, value, actionTextIsVisible)
    }

    open fun updateActionIcon() {
        actionIconView.apply {
            if (actionIconRes != -1) {
                updateIconSize(actionIconSize, this)
                setImageResource(actionIconRes)
                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(actionIconTint))
                setPadding(actionIconPadding, actionIconPadding, actionIconPadding, actionIconPadding)
                setActionIconVisibility(showActionIcon)
            } else {
                this.visibility = View.GONE
            }
        }
    }
    /*endregion ############################ Icons ################################*/

    /*region ############################ Container ################################*/

    fun setContainerPaddings(
        startPadding: Int,
        topPadding: Int,
        endPadding: Int,
        bottomPadding: Int
    ) {
        containerView.setPadding(
            paddingLeft + startPadding,
            paddingTop + topPadding,
            paddingRight + endPadding,
            paddingBottom + bottomPadding
        )
    }

    fun setContainerHeight(containerHeight: Int) {
        containerView.layoutParams.apply {
            height = containerHeight
        }
    }

    /*endregion ############################ Container ################################*/

    /*region ############################ Other ################################*/
    open fun setDefaultValues() {
        navIconRes = defNavIconRes
        navIconSize = defNavIconSize
        navIconTint = defIconColor
        navIconPadding = defIconsPadding
        showNavIcon = defShowNavIcon

        actionIconRes = defActionIconRes
        actionIconSize = defActionIconSize
        actionIconTint = defIconColor
        actionIconPadding = defIconsPadding
        showActionIcon = defShowActionIcon

        setContainerPaddings(defPadding, defPadding, defPadding, defPadding)
        setContainerHeight(defContainerHeight.toInt())

        startActionView.visibility = View.GONE
        updateNavigationIcon()

        endActionView.visibility = View.GONE
        updateActionIcon()

        setTitle("")
        setDefaultTitleParams()

        dividerView.setBackgroundColor(defDividerColor)
        setDividerHeight(defDividerHeight.toInt())
        setDividerVisibility(false)
    }

    private fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
    /*endregion ############################ Other ################################*/
}
