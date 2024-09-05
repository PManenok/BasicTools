package by.esas.tools.listheader

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import by.esas.tools.util.SwitchManager
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt

/**
 * This view  can be used to show a list's header and list arrow or some action text button.
 * Also it can contain other views, and if default behavior is enabled then all top views'
 * visibility in this ListHeader will be managed by this ListHeader. In this case user should
 * be careful with usage of custom manage of visibility of top views, because it may be
 * override by ListHeader.
 *
 * To handle situation when user needs to handle visibility of
 * contained views there can be two ways:
 * 1. Disable default behaviour and set listeners to container or action views, then
 *    you can handle clicks in your way, but remember that setting opened state will use
 *    default behavior anyway, so you should not use it.
 * 2. Add a container as top view, that will contain all other views, then you can still use
 *    default behavior and only this top container's visibility will be managed by ListHeader
 */
open class ListHeader : LinearLayout, SwitchManager.ISwitchView {

    val TAG: String = ListHeader::class.java.simpleName
    protected val container: ConstraintLayout
    protected val actionContainer: FrameLayout
    protected val titleText: MaterialTextView
    protected val actionText: MaterialTextView
    protected val arrowIcon: AppCompatImageView

    protected val defIconInnerPadding = dpToPx(3)
    protected val defIconColor = Color.RED
    protected val defPadding = 0
    protected var defChildrenMarginTop: Int = 0
    protected var defChildrenMarginBottom: Int = 0
    protected var defViewBottomPadding: Int = 0
    protected var defIconDrawableRes: Int? = null
    protected var defOpened: Boolean = true

    protected var childrenMarginTop: Int = 0
    protected var childrenMarginBottom: Int = 0
    protected var viewBottomPadding: Int = 0
    protected var iconDrawableRes: Int? = null
    protected var opened: Boolean = true
    protected val openedListeners = mutableListOf<ListOpenedListener>()

    protected open val containerListener: OnClickListener = OnClickListener {
        setListContainerClickable(false)
        updateChildrenVisibility(!opened, true)
        setListContainerClickable(true)
    }

    /*region ############### Constructors ###############*/

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        initAttrs(attrs)
    }

    /*endregion ############### Constructors ###############*/

    init {
        this.orientation = VERTICAL
        val view = inflate(context, R.layout.v_list_header, this)
        container = view.findViewById(R.id.v_list_header_container)
        titleText = view.findViewById(R.id.v_list_header_title)
        actionContainer = view.findViewById(R.id.v_list_header_action_container)
        actionText = view.findViewById(R.id.v_list_header_action)
        arrowIcon = view.findViewById(R.id.v_list_header_icon)
    }

    /*  Initialize attributes from XML file  */
    protected open fun initAttrs(attrs: AttributeSet?) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListHeader)

        val defaultBehaviorEnabled = typedArray.getBoolean(R.styleable.ListHeader_listDefaultBehaviorEnabled, true)
        val containerIsClickable = typedArray.getBoolean(R.styleable.ListHeader_listContainerClickable, false)
        val topPadding = typedArray.getDimensionPixelSize(R.styleable.ListHeader_listContainerPaddingTop, defPadding)
        val bottomPadding =
            typedArray.getDimensionPixelSize(R.styleable.ListHeader_listContainerPaddingBottom, defPadding)
        val startPadding =
            typedArray.getDimensionPixelSize(R.styleable.ListHeader_listContainerPaddingStart, defPadding)
        val endPadding = typedArray.getDimensionPixelSize(R.styleable.ListHeader_listContainerPaddingEnd, defPadding)

        childrenMarginTop =
            typedArray.getDimensionPixelSize(R.styleable.ListHeader_listChildrenMarginTop, defChildrenMarginTop)
        childrenMarginBottom =
            typedArray.getDimensionPixelSize(R.styleable.ListHeader_listChildrenMarginBottom, defChildrenMarginBottom)

        /*##########  Title  ##########*/
        val title = typedArray.getString(R.styleable.ListHeader_listTitle)
        val titleStyleId: Int = typedArray.getResourceId(R.styleable.ListHeader_listTitleTextAppearance, -1)

        /*##########  Action  ##########*/
        val action = typedArray.getString(R.styleable.ListHeader_listAction)
        val actionStyleId: Int = typedArray.getResourceId(R.styleable.ListHeader_listActionTextAppearance, -1)

        /*##########  Icon  ##########*/
        val iconSize = typedArray.getDimensionPixelSize(R.styleable.ListHeader_listArrowSize, 0)
        iconDrawableRes = typedArray.getResourceId(R.styleable.ListHeader_listArrowIcon, -1).takeIf { it != -1 }
        val iconTint = typedArray.getColor(R.styleable.ListHeader_listArrowTint, defIconColor)
        val iconInnerPadding =
            typedArray.getDimensionPixelSize(R.styleable.ListHeader_listArrowInnerPadding, defIconInnerPadding.toInt())
        opened = typedArray.getBoolean(R.styleable.ListHeader_listIsOpen, true)

        typedArray.recycle()

        if (defaultBehaviorEnabled)
            container.setOnClickListener(containerListener)
        container.isClickable = containerIsClickable
        setupPaddings(startPadding, topPadding, endPadding, bottomPadding)

        setupTextView(titleText, title, titleStyleId)
        setupTextView(actionText, action, actionStyleId)

        setupArrowIcon(iconDrawableRes, iconTint, iconSize, iconInnerPadding)

        setupActionVisibility()

        if (defaultBehaviorEnabled)
            addOnPreDrawListener()
    }

    /*region ############### setups ###############*/

    private fun addOnPreDrawListener() {
        this.viewTreeObserver.addOnPreDrawListener {
            var draw = true
            if (!checkChildrenVisibility(opened)) {
                updateChildrenVisibility(opened, true)
                draw = false
            }
            return@addOnPreDrawListener draw
        }
    }

    open fun setupPaddings(startPadding: Int, topPadding: Int, endPadding: Int, bottomPadding: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            titleText.setPadding(titleText.paddingStart, topPadding, titleText.paddingEnd, bottomPadding)
        }

        container.setPadding(startPadding, container.paddingTop, endPadding, container.paddingBottom)
        (container.layoutParams as LayoutParams).apply {
            bottomMargin = if (opened) childrenMarginTop else 0
        }

        viewBottomPadding = this.paddingBottom
        val viewPadding =
            if (opened) viewBottomPadding + childrenMarginBottom else viewBottomPadding
        this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, viewPadding)
    }

    open fun setChildrenMargins(marginTop: Int, marginBottom: Int) {
        childrenMarginTop = marginTop
        childrenMarginBottom = marginBottom
        (container.layoutParams as LayoutParams).apply {
            bottomMargin = if (opened) childrenMarginTop else 0
        }
        val viewPadding =
            if (opened) viewBottomPadding + childrenMarginBottom else viewBottomPadding
        this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, viewPadding)
    }

    protected open fun setupTextView(view: TextView, text: String?, style: Int) {
        view.apply {
            this.text = text ?: ""
            if (style != -1)
                TextViewCompat.setTextAppearance(this, style)
        }
    }

    protected open fun setupActionVisibility() {
        if (actionText.text.isNullOrBlank()) {
            arrowIcon.visibility = if (iconDrawableRes != null) View.VISIBLE else View.GONE
            actionText.visibility = View.GONE
        } else {
            arrowIcon.visibility = View.GONE
            actionText.visibility = View.VISIBLE
        }
    }

    /*endregion ############### setups ###############*/

    /*region ############### Setting functions ###############*/

    open fun enableView() {
        super.setEnabled(true)

        container.isEnabled = true
        actionText.isEnabled = true
        arrowIcon.isEnabled = true
    }

    open fun disableView() {
        super.setEnabled(false)

        container.isEnabled = false
        actionText.isEnabled = false
        arrowIcon.isEnabled = false
    }

    open fun addChild(view: View) {
        super.addView(view)
    }

    open fun setListState(isOpen: Boolean) {
        updateChildrenVisibility(isOpen, false)
    }

    open fun getListState(): Boolean {
        return opened
    }

    protected open fun checkChildrenVisibility(value: Boolean): Boolean {
        var correct: Boolean = true
        val visibilityValue = if (value) View.VISIBLE else View.GONE
        this@ListHeader.children.forEach {
            if (it.id != R.id.v_list_header_container) {
                correct = correct && it.visibility == visibilityValue
            }
        }
        return correct
    }

    protected open fun updateChildrenVisibility(value: Boolean, forceUpdate: Boolean) {
        if (opened != value || forceUpdate) {
            if (opened != value) {
                opened = value
                notifyListeners(value)
            }
            if (arrowIcon.visibility == View.VISIBLE) {
                arrowIcon.isEnabled = value
            }

            (container.layoutParams as LayoutParams).apply {
                bottomMargin = if (value) childrenMarginTop else 0
            }
            val viewPadding =
                if (value) viewBottomPadding + childrenMarginBottom else viewBottomPadding
            this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, viewPadding)

            this@ListHeader.children.forEach {
                if (it.id != R.id.v_list_header_container) {
                    it.visibility = if (value) View.VISIBLE else View.GONE
                }
            }
        }
    }

    open fun setDefaultValues() {
        childrenMarginTop = defChildrenMarginTop
        childrenMarginBottom = defChildrenMarginBottom
        opened = defOpened
        iconDrawableRes = defIconDrawableRes
        viewBottomPadding = defViewBottomPadding
        openedListeners.clear()

        setDefaultContainerListener()
        setListContainerClickable(true)

        setupPaddings(defPadding, defPadding, defPadding, defPadding)

        setupTextView(titleText, "title", -1)
        setupTextView(actionText, "action", -1)

        setupArrowIcon(defIconDrawableRes, defIconColor, 0, defIconInnerPadding.toInt())

        setupActionVisibility()
        addOnPreDrawListener()
    }

    /*endregion ############### Setting functions ###############*/

    /*region ############### List Container ################*/

    open fun setListContainerClickable(isClickable: Boolean) {
        container.isClickable = isClickable
    }

    open fun setContainerListener(listener: OnClickListener?) {
        container.setOnClickListener(listener)
    }

    open fun setDefaultContainerListener() {
        container.setOnClickListener(containerListener)
    }

    /*endregion ############### List Container ################*/

    /*region ############### List Action ###############*/

    open fun setListActionClickable(isClickable: Boolean) {
        actionText.isClickable = isClickable
    }

    open fun setActionListener(listener: OnClickListener?) {
        actionText.setOnClickListener(listener)
    }

    open fun setListActionText(text: String?) {
        if (actionText.text.toString() != text) {
            actionText.text = text
            setupActionVisibility()
        }
    }

    open fun setListActionText(textRes: Int) {
        setListActionText(resources.getString(textRes))
    }

    open fun getListActionText(): String {
        return actionText.text.toString()
    }

    open fun setListActionStyle(styleId: Int) {
        if (styleId != -1)
            TextViewCompat.setTextAppearance(actionText, styleId)
    }
    /*endregion ############### List Action############### */

    /*region ############### List Title ############### */

    open fun setListTitle(text: String) {
        if (titleText.text.toString() != text) {
            titleText.text = text
        }
    }

    open fun setListTitle(textRes: Int) {
        setListTitle(resources.getString(textRes))
    }

    open fun getListTitle(): String? {
        return titleText.text.toString()
    }

    open fun setListTitleStyle(styleId: Int) {
        if (styleId != -1)
            TextViewCompat.setTextAppearance(titleText, styleId)
    }
    /*endregion ############### List Title ############### */

    /*region ############### Arrow Icon ############### */

    open fun setArrowClickable(isClickable: Boolean) {
        arrowIcon.isClickable = isClickable
    }

    open fun setArrowListener(listener: OnClickListener?) {
        arrowIcon.setOnClickListener(listener)
    }

    open fun setArrowIcon(imageRes: Int) {
        iconDrawableRes = imageRes
        arrowIcon.setImageResource(imageRes)
        setupActionVisibility()
    }

    /**
     * Set arrow icon size in pixels
     * */
    open fun setArrowIconSize(iconSize: Int) {
        updateIconSize(iconSize, arrowIcon)
    }

    open fun setArrowIconTint(color: Int) {
        ImageViewCompat.setImageTintList(arrowIcon, ColorStateList.valueOf(color))
    }

    open fun setArrowIconTintResource(@ColorRes color: Int) {
        val parsedColor = ContextCompat.getColor(context, color)
        setArrowIconTint(parsedColor)
    }

    protected open fun updateIconSize(iconSize: Int, icon: AppCompatImageView) {
        val size: Int = if (iconSize == 0) dpToPx(24).roundToInt() else iconSize
        val params = icon.layoutParams
        if (params != null && (params.width != size || params.height != size)) {
            params.width = size
            params.height = size
            icon.layoutParams = params
        }
    }

    protected open fun setupArrowIcon(iconDrawableRes: Int?, iconTint: Int, iconSize: Int, iconInnerPadding: Int) {
        arrowIcon.apply {
            if (iconDrawableRes != null) {
                updateIconSize(iconSize, this)
                setImageResource(iconDrawableRes)
                ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(iconTint))
                setPadding(iconInnerPadding, iconInnerPadding, iconInnerPadding, iconInnerPadding)
                isEnabled = opened
                this.visibility = View.VISIBLE
            } else {
                this.visibility = View.GONE
            }
        }
    }
    /*endregion ############### Arrow Icon ############### */

    /*region ############### List Opened Listener ############### */
    open fun notifyListeners(isOpened: Boolean) {
        openedListeners.forEach { it.onListStateChanged(isOpened) }
    }

    open fun addOpenedListener(listener: ListOpenedListener) {
        if (!openedListeners.contains(listener))
            openedListeners.add(listener)
    }

    open fun removeOpenedListener(listener: ListOpenedListener) {
        if (openedListeners.contains(listener))
            openedListeners.remove(listener)
    }

    interface ListOpenedListener {

        fun onListStateChanged(isOpen: Boolean)
    }
    /*endregion ############### List Opened Listener ############### */

    /*region ############### ISwitchView Interface ############### */
    override fun switchOn() {
        enableView()
    }

    override fun switchOff() {
        disableView()
    }
    /*endregion ############### ISwitchView Interface ############### */

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }
}
