package by.esas.tools.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import by.esas.tools.checker.Checking
import by.esas.tools.dialog.databinding.DfMessageBinding
import by.esas.tools.dialog.simpleItemAdapter.SimpleItemAdapter
import by.esas.tools.dialog.simpleItemAdapter.SimpleItemModel
import by.esas.tools.dialog.BR

class MessageDialog<B : DfMessageBinding, E : Exception, EnumT : Enum<EnumT>> : BindingDialogFragment<B, E, EnumT> {
    override val TAG: String = MessageDialog::class.java.simpleName

    constructor(cancellable: Boolean) : super() {
        isCancelable = cancellable
    }

    constructor() : super() {
        isCancelable = true
    }

    override fun provideLayoutId(): Int {
        return R.layout.df_message
    }

    fun setDialogCallback(callback: MessageCallback) {
        this.callback = callback
    }

    fun provideCallback(): MessageCallback? {
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

    private var callback: MessageCallback? = null
    private var items: List<String> = emptyList()
    private var textAlignment: Int = View.TEXT_ALIGNMENT_TEXT_START
    private var adapter: SimpleItemAdapter =
        SimpleItemAdapter { position, item ->
            if (btnEnabled.get()) {
                disableControls()
                afterOk = true
                dismiss()
                provideCallback()?.onItemPicked(position, item.name, itemAction)
                enableControls()
            }
        }
    private var positiveAction: String? = null
    private var itemAction: String? = null

    var title = ObservableField<String>("")
    var message = ObservableField<String>("")
    var positiveBtnText = ObservableField<String>(resources.getString(R.string.common_ok_btn))
    var neutralBtnText = ObservableField<String>(resources.getString(R.string.common_cancel))
    var negativeBtnText = ObservableField<String>(resources.getString(R.string.common_cancel))

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
        outState.putInt("textAlignment", textAlignment)
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
            positiveBtnText.set(
                savedInstanceState.getString("positiveBtnText", resources.getString(R.string.common_ok_btn))
                    ?: resources.getString(R.string.common_ok_btn)
            )
            neutralBtnText.set(savedInstanceState.getString("neutralBtnText", "") ?: "")
            negativeBtnText.set(
                savedInstanceState.getString("negativeBtnText", resources.getString(R.string.common_cancel))
                    ?: resources.getString(R.string.common_cancel)
            )
            items = savedInstanceState.getStringArray("items").orEmpty().toList()
            textAlignment = savedInstanceState.getInt("textAlignment", View.TEXT_ALIGNMENT_TEXT_START)
            updateScreen()
            enableControls()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MessageDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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

    private fun updateScreen() {
        logger.logOrder("updateScreen")
        showMessage.set(!message.get().isNullOrBlank())
        adapter.cleanItems()
        val lastIndex = items.lastIndex
        if (lastIndex > -1) {
            adapter.addItems(items.mapIndexed { index, item ->
                SimpleItemModel(
                    shortName = "",
                    name = item,
                    isChoosed = false,
                    isLast = index == lastIndex,
                    textAlignment = textAlignment
                )
            })
            adapter.notifyDataSetChanged()
            showExtraDiv.set(showTitle.get() || showMessage.get())
        } else {
            showExtraDiv.set(false)
        }
        hasButtons.set(showPositiveBtn.get() || showNegativeBtn.get() || showNeutralBtn.get())
        showDiv1.set(showNegativeBtn.get() && (showNeutralBtn.get() || showPositiveBtn.get()))
        showDiv2.set(showNeutralBtn.get() && showPositiveBtn.get())
    }

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

    override fun onCancel(dialog: DialogInterface) {
        logger.logOrder("onCancel")
        afterOk = true
        provideCallback()?.onCancelled()
        super.onCancel(dialog)
    }

    fun setTextAlignment(alignment: Int) {
        textAlignment = alignment
    }

    fun setTitle(resId: Int) {
        logger.logOrder("setTitle resId = $resId")
        if (resId != -1)
            setTitle(resources.getString(resId))
        else setTitle("")
    }

    fun setTitle(value: String) {
        title.set(value)
        showTitle.set(value.isNotBlank())
    }

    fun setMessage(resId: Int) {
        logger.logOrder("setMessage resId = $resId")
        if (resId != -1)
            setMessage(resources.getString(resId))
        else setMessage("")
    }

    fun setMessage(value: String) {
        message.set(value)
        showMessage.set(value.isNotBlank())
    }

    fun setPositiveButton(resId: Int = R.string.common_ok_btn, actionName: String? = null) {
        logger.logOrder("setPositiveButton resId = $resId; actionName = $actionName")
        if (resId != -1)
            setPositiveButton(resources.getString(resId), actionName)
        else setPositiveButton("", actionName)
    }

    fun setPositiveButton(value: String, actionName: String? = null) {
        positiveAction = actionName
        positiveBtnText.set(value)
        showPositiveBtn.set(value.isNotBlank())
    }

    fun setNeutralButton(resId: Int) {
        logger.logOrder("setNeutralButton resId = $resId")
        if (resId != -1)
            setNeutralButton(resources.getString(resId))
        else setNeutralButton("")
    }

    fun setNeutralButton(value: String) {
        neutralBtnText.set(value)
        showNeutralBtn.set(value.isNotBlank())
    }

    fun setNegativeButton(resId: Int = R.string.common_cancel) {
        logger.logOrder("setNegativeButton resId = $resId")
        if (resId != -1)
            setNegativeButton(resources.getString(resId))
        else setNegativeButton("")
    }

    fun setNegativeButton(value: String) {
        negativeBtnText.set(value)
        showNegativeBtn.set(value.isNotBlank())
    }

    fun setItems(list: List<String>, actionName: String? = null, alignment: Int = View.TEXT_ALIGNMENT_TEXT_START) {
        textAlignment = alignment
        itemAction = actionName
        this.items = list
        updateScreen()
    }

    interface MessageCallback {
        fun onPositiveClick(actionName: String? = null) {}
        fun onNeutralClick() {}
        fun onNegativeClick() {}
        fun onCancelled() {}
        fun onItemPicked(position: Int, item: String, actionName: String?) {}
    }

    override fun provideSwitchableList(): List<View?> {
        TODO("Not yet implemented")
    }

    override fun provideValidationList(): List<Checking> {
        TODO("Not yet implemented")
    }

    override fun provideVariableId(): Int = BR.handler
}