package by.esas.tools.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.checker.Checking
import by.esas.tools.dialog.databinding.DfMessageBinding
import by.esas.tools.dialog.databinding.IDialogMessageBinding
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemAdapter
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemModel
import com.google.android.material.button.MaterialButton

/**
 * This dialog can be used to show some message or to let user pick items.
 *
 * User can set title, message, positive button, neutral button and negative button,
 * also items for recycler can be added.
 *
 * Title can be set via [setTitle], text can be passed by String or by resource id,
 * its textAppearance can be passed along with title itself.
 *
 * Message can be set via [setMessage], text can be passed by String or by resource id,
 * its textAppearance can be passed along with title itself.
 *
 * Positive button can be set via [setPositiveButton]. Text can be set by String or resource id. Along with text this button can have action,
 * that will be passed to message [callback] when positive button will be clicked.
 * To change button appearance user should pass [ButtonAppearance] as argument of [setPositiveButton].
 * Another way to change button appearance is to override [R.style.MessageDialogButtonStyle] in module
 * where dialog would be used.
 *
 * Neutral button can be set via [setNeutralButton]. Text can be set by String or resource id.
 * To change button appearance user should pass [ButtonAppearance] as argument of [setNeutralButton].
 * Another way to change button appearance is to override [R.style.MessageDialogButtonStyle] in module
 * where dialog would be used.
 *
 * Negative button can be set via [setNegativeButton]. Text can be set by String or resource id.
 * To change button appearance user should pass [ButtonAppearance] as argument of [setNegativeButton].
 * Another way to change button appearance is to override [R.style.MessageDialogButtonStyle] in module
 * where dialog would be used.
 *
 * This dialog use custom [SimpleItemAdapter] to show list of items. There is only possibility to change
 * item's text appearance by overriding [R.style.MessageDialogItemStyle], and via setting
 * text alignment [setTextAlignment].
 *
 * @see ButtonAppearance
 * @see SimpleItemAdapter
 */
open class MessageDialog<E : Exception, EnumT : Enum<EnumT>> : BindingDialogFragment<DfMessageBinding, E, EnumT> {
    override val TAG: String = MessageDialog::class.java.simpleName

    constructor(cancellable: Boolean) : super() {
        isCancelable = cancellable
    }

    constructor() : super() {
        isCancelable = true
    }

    //region settings methods

    override fun provideLayoutId(): Int {
        return R.layout.df_message
    }

    override fun provideSwitchableList(): List<View?> {
        return emptyList()
    }

    override fun provideValidationList(): List<Checking> {
        return emptyList()
    }

    override fun provideVariableId(): Int = BR.handler

    open fun setDialogCallback(callback: MessageCallback) {
        this.callback = callback
    }

    open fun provideCallback(): MessageCallback? {
        val frag = targetFragment
        val act = activity
        return when {
            frag is MessageCallback -> {
                frag
            }
            act is MessageCallback -> {
                act
            }
            else -> callback
        }
    }

    //endregion settings methods

    //region properties

    protected var callback: MessageCallback? = null
    protected var items: List<String> = emptyList()
    protected var itemTextAlignment: Int = View.TEXT_ALIGNMENT_TEXT_START
    protected var adapter: SimpleItemAdapter =
        SimpleItemAdapter.createCustom(IDialogMessageBinding::class.java) { position, item ->
            if (btnEnabled.get()) {
                disableControls()
                afterOk = true
                dismiss()
                provideCallback()?.onItemPicked(position, item.name, itemAction)
                enableControls()
            }
        }
    protected var positiveAction: String? = null
    protected var itemAction: String? = null

    protected var positiveAppearance: ButtonAppearance? = null
    protected var neutralAppearance: ButtonAppearance? = null
    protected var negativeAppearance: ButtonAppearance? = null
    protected var titleAppearanceResId: Int = R.style.MessageDialogTitleStyle
    protected var messageAppearanceResId: Int = R.style.MessageDialogTextStyle

    var title = ObservableField<String>("")
    var message = ObservableField<String>("")

    //At the initiation dialog does not has context yet
    var positiveBtnText = ObservableField<String>("OK")
    var neutralBtnText = ObservableField<String>("Cancel")
    var negativeBtnText = ObservableField<String>("Cancel")

    val showTitle = ObservableBoolean(false)
    val showMessage = ObservableBoolean(false)
    val showPositiveBtn = ObservableBoolean(false)
    val showNeutralBtn = ObservableBoolean(false)
    val showNegativeBtn = ObservableBoolean(false)
    val showDiv1 = ObservableBoolean(false)
    val showDiv2 = ObservableBoolean(false)
    val hasButtons = ObservableBoolean(false)
    val showExtraDiv = ObservableBoolean(false)
    val btnEnabled = ObservableBoolean(false)

    //endregion properties

    //region save state methods

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isCancelable", isCancelable)
        outState.putBoolean("btnEnabled", btnEnabled.get())
        outState.putString("positiveAction", positiveAction)
        outState.putString("itemAction", itemAction)
        outState.putString("title", title.get())
        outState.putString("message", message.get())
        outState.putString("positiveBtnText", positiveBtnText.get())
        outState.putString("neutralBtnText", neutralBtnText.get())
        outState.putString("negativeBtnText", negativeBtnText.get())
        outState.putBoolean("showPositiveBtn", showPositiveBtn.get())
        outState.putBoolean("showNeutralBtn", showNeutralBtn.get())
        outState.putBoolean("showNegativeBtn", showNegativeBtn.get())
        outState.putBoolean("showTitle", showTitle.get())
        outState.putBoolean("showMessage", showMessage.get())
        outState.putStringArray("items", items.toTypedArray())
        outState.putInt("textAlignment", itemTextAlignment)
        outState.putInt("title_textAppearance", titleAppearanceResId)
        outState.putInt("message_textAppearance", messageAppearanceResId)
        saveButtonState("positive", positiveAppearance, outState)
        saveButtonState("neutral", neutralAppearance, outState)
        saveButtonState("negative", negativeAppearance, outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            isCancelable = savedInstanceState.getBoolean("isCancelable", true)
            btnEnabled.set(savedInstanceState.getBoolean("btnEnabled", true))
            showPositiveBtn.set(savedInstanceState.getBoolean("showPositiveBtn", false))
            showNeutralBtn.set(savedInstanceState.getBoolean("showNeutralBtn", false))
            showNegativeBtn.set(savedInstanceState.getBoolean("showNegativeBtn", false))
            showTitle.set(savedInstanceState.getBoolean("showTitle", false))
            showMessage.set(savedInstanceState.getBoolean("showMessage", false))
            showNegativeBtn.set(savedInstanceState.getBoolean("showNegativeBtn", false))
            positiveAction = savedInstanceState.getString("positiveAction", null)
            itemAction = savedInstanceState.getString("itemAction", null)
            title.set(savedInstanceState.getString("title", "") ?: "")
            message.set(savedInstanceState.getString("message", "") ?: "")
            positiveBtnText.set(savedInstanceState.getString("positiveBtnText", "") ?: "")
            neutralBtnText.set(savedInstanceState.getString("neutralBtnText", "") ?: "")
            negativeBtnText.set(savedInstanceState.getString("negativeBtnText", "") ?: "")
            items = savedInstanceState.getStringArray("items").orEmpty().toList()
            itemTextAlignment = savedInstanceState.getInt("textAlignment", View.TEXT_ALIGNMENT_TEXT_START)
            titleAppearanceResId = savedInstanceState.getInt("title_textAppearance", -1)
            messageAppearanceResId = savedInstanceState.getInt("message_textAppearance", -1)
            positiveAppearance = restoreButtonState("positive", savedInstanceState)
            neutralAppearance = restoreButtonState("neutral", savedInstanceState)
            negativeAppearance = restoreButtonState("negative", savedInstanceState)
            updateScreen()
            enableControls()
        }
    }

    protected open fun saveButtonState(tag: String, appearance: ButtonAppearance?, outState: Bundle) {
        if (appearance != null) {
            outState.putBoolean("${tag}_hasAppearance", true)
            outState.putInt("${tag}_textAppearance", appearance.textAppearanceResId)
            outState.putInt("${tag}_backgroundColor", appearance.backgroundColorResId)
            outState.putBoolean("${tag}_textAllCaps", appearance.textAllCaps)
        } else {
            outState.putBoolean("${tag}_hasAppearance", false)
        }
    }

    protected open fun restoreButtonState(tag: String, savedState: Bundle): ButtonAppearance? {
        return if (savedState.getBoolean("${tag}_hasAppearance", false)) {
            ButtonAppearance(
                textAppearanceResId = savedState.getInt("${tag}_textAppearance", -1),
                backgroundColorResId = savedState.getInt("${tag}_backgroundColor", -1),
                textAllCaps = savedState.getBoolean("${tag}_textAllCaps", false)
            )
        } else {
            null
        }
    }


    //endregion save state methods

    //region lifecycle methods

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MessageCallback) {
            callback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MessageDialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateScreen()

        val manager = LinearLayoutManager(this.context)
        binding.dfMessageRecycler.layoutManager = manager
        binding.dfMessageRecycler.adapter = adapter
        binding.dfMessageRecycler.setHasFixedSize(true)

        enableControls()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCancel(dialog: DialogInterface) {
        logger.logOrder("onCancel")
        afterOk = true
        provideCallback()?.onCancelled()
        super.onCancel(dialog)
    }

    //endregion lifecycle methods

    //region helping methods

    protected open fun updateScreen() {
        logger.logOrder("updateScreen")
        showMessage.set(!message.get().isNullOrBlank())
        updateAdapter()
        hasButtons.set(showPositiveBtn.get() || showNegativeBtn.get() || showNeutralBtn.get())
        showDiv1.set(showNegativeBtn.get() && (showNeutralBtn.get() || showPositiveBtn.get()))
        showDiv2.set(showNeutralBtn.get() && showPositiveBtn.get())
        updateAppearance()
    }

    protected open fun updateAdapter() {
        adapter.cleanItems()
        val lastIndex = items.lastIndex
        if (lastIndex > -1) {
            adapter.addItems(items.mapIndexed { index, item ->
                SimpleItemModel(
                    shortName = "",
                    name = item,
                    isChoosed = false,
                    isLast = index == lastIndex,
                    textAlignment = itemTextAlignment
                )
            })
            adapter.notifyDataSetChanged()
            showExtraDiv.set(showTitle.get() || showMessage.get())
        } else {
            showExtraDiv.set(false)
        }
    }

    protected open fun updateAppearance() {
        updateButtonAppearance(binding.dfMessagePositiveButton, positiveAppearance)
        updateButtonAppearance(binding.dfMessageNeutralBtn, neutralAppearance)
        updateButtonAppearance(binding.dfMessageNegativeBtn, negativeAppearance)
        if (titleAppearanceResId != -1)
            TextViewCompat.setTextAppearance(binding.dfMessageTitle, titleAppearanceResId)
        if (messageAppearanceResId != -1)
            TextViewCompat.setTextAppearance(binding.dfMessageMessage, messageAppearanceResId)
    }

    protected open fun updateButtonAppearance(btnView: MaterialButton, appearance: ButtonAppearance?) {
        if (appearance != null) {
            btnView.apply {
                TextViewCompat.setTextAppearance(this, appearance.textAppearanceResId)
                isAllCaps = appearance.textAllCaps
                setBackgroundColor(ContextCompat.getColor(requireContext(), appearance.backgroundColorResId))
            }
        }
    }

    override fun disableControls() {
        super.disableControls()
        logger.logOrder("disableControls")
        btnEnabled.set(false)
    }

    override fun enableControls() {
        super.enableControls()
        logger.logOrder("enableControls")
        btnEnabled.set(true)
    }

    //endregion helping methods

    //region clicks

    fun onPositiveClick() {
        disableControls()
        logger.logOrder("onPositiveClick")
        afterOk = true
        dismiss()
        provideCallback()?.onPositiveClick(positiveAction)
        enableControls()
    }

    fun onNeutralClick() {
        disableControls()
        logger.logOrder("onNeutralClick")
        afterOk = true
        dismiss()
        provideCallback()?.onNeutralClick()
        enableControls()
    }

    fun onNegativeClick() {
        disableControls()
        logger.logOrder("onNegativeClick")
        afterOk = true
        dismiss()
        provideCallback()?.onNegativeClick()
        enableControls()
    }

    //endregion clicks

    //region setters

    open fun setTextAlignment(alignment: Int) {
        itemTextAlignment = alignment
    }

    open fun setTitle(resId: Int, appearanceResId: Int = -1) {
        logger.logOrder("setTitle resId = $resId appearanceResId = $appearanceResId")
        if (resId != -1)
            setTitle(resources.getString(resId), appearanceResId)
        else setTitle("", appearanceResId)
    }

    open fun setTitle(value: String, appearanceResId: Int = -1) {
        title.set(value)
        showTitle.set(value.isNotBlank())
        titleAppearanceResId = appearanceResId
    }

    open fun setMessage(resId: Int, appearanceResId: Int = -1) {
        logger.logOrder("setMessage resId = $resId appearanceResId = $appearanceResId")
        if (resId != -1)
            setMessage(resources.getString(resId), appearanceResId)
        else setMessage("", appearanceResId)
    }

    open fun setMessage(value: String, appearanceResId: Int = -1) {
        message.set(value)
        showMessage.set(value.isNotBlank())
        messageAppearanceResId = appearanceResId
    }

    open fun setPositiveButton(resId: Int, actionName: String? = null, appearance: ButtonAppearance? = null) {
        logger.logOrder("setPositiveButton resId = $resId; actionName = $actionName")
        if (resId != -1)
            setPositiveButton(resources.getString(resId), actionName, appearance)
        else setPositiveButton("", actionName, appearance)
    }

    open fun setPositiveButton(value: String, actionName: String? = null, appearance: ButtonAppearance? = null) {
        positiveAction = actionName
        positiveBtnText.set(value)
        showPositiveBtn.set(value.isNotBlank())
        positiveAppearance = appearance
    }

    open fun setNeutralButton(resId: Int, appearance: ButtonAppearance? = null) {
        logger.logOrder("setNeutralButton resId = $resId")
        if (resId != -1)
            setNeutralButton(resources.getString(resId), appearance)
        else setNeutralButton("", appearance)
    }

    open fun setNeutralButton(value: String, appearance: ButtonAppearance? = null) {
        neutralBtnText.set(value)
        showNeutralBtn.set(value.isNotBlank())
        neutralAppearance = appearance
    }

    open fun setNegativeButton(resId: Int, appearance: ButtonAppearance? = null) {
        logger.logOrder("setNegativeButton resId = $resId")
        if (resId != -1)
            setNegativeButton(resources.getString(resId), appearance)
        else setNegativeButton("", appearance)
    }

    open fun setNegativeButton(value: String, appearance: ButtonAppearance? = null) {
        negativeBtnText.set(value)
        showNegativeBtn.set(value.isNotBlank())
        negativeAppearance = appearance
    }

    open fun setItems(list: List<String>, actionName: String? = null, alignment: Int = View.TEXT_ALIGNMENT_TEXT_START) {
        itemTextAlignment = alignment
        itemAction = actionName
        this.items = list
        updateScreen()
    }

    //endregion setters

    interface MessageCallback {
        fun onPositiveClick(actionName: String? = null) {}
        fun onNeutralClick() {}
        fun onNegativeClick() {}
        fun onCancelled() {}
        fun onItemPicked(position: Int, item: String, actionName: String?) {}
    }

    open class ButtonAppearance(
        val textAppearanceResId: Int,
        val backgroundColorResId: Int,
        val textAllCaps: Boolean
    )
}