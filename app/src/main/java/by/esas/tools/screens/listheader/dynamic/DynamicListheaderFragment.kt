package by.esas.tools.screens.listheader.dynamic

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainDynamicListheaderBinding
import by.esas.tools.inputfieldview.dpToPx
import by.esas.tools.listheader.ListHeader

class DynamicListheaderFragment() :
    AppFragment<DynamicListheaderVM, FMainDynamicListheaderBinding>() {
    override val fragmentDestinationId = R.id.dynamicListheaderFragment

    override fun provideLayoutId() = R.layout.f_main_dynamic_listheader

    override fun provideViewModel(): DynamicListheaderVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(DynamicListheaderVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fDynamicListheaderButtonCreate.setOnClickListener {
            binding.fDynamicListheaderContainer.removeAllViews()
            binding.fDynamicListheaderContainer.addView(createListheader())
        }

        binding.fDynamicListheaderButtonCreateDefault.setOnClickListener {
            binding.fDynamicListheaderContainer.removeAllViews()
            binding.fDynamicListheaderContainer.addView(createDefaultListheader())
        }
    }

    private fun createListheader(): ListHeader {
        val listHeader = ListHeader(requireContext())
        listHeader.setListTitle(binding.fDynamicListheaderTitle.text.toString())
        listHeader.setListActionText(binding.fDynamicListheaderActionText.text.toString())
        listHeader.setListTitleStyle(getStyle(binding.fDynamicListheaderSpinnerTitle.selectedItem.toString()))
        listHeader.setListActionStyle(getStyle(binding.fDynamicListheaderSpinnerActionText.selectedItem.toString()))
        listHeader.setArrowIconImage(getActionImage())
        listHeader.setArrowIconSize(getDimensInDp(binding.fDynamicListheaderImageSize))
        listHeader.setArrowIconTintResource(getActionImageTint())
        setListheaderPaddings(listHeader)
        setListheaderMargins(listHeader)

        listHeader.setDefaultContainerListener()
        listHeader.setArrowClickable(true)
        listHeader.setArrowListener {
            Toast.makeText(requireContext(), resources.getString(R.string.listheader_icon_click), Toast.LENGTH_SHORT).show()
        }

        listHeader.addChild(createTestTextView())
        listHeader.addChild(createTestTextView())
        listHeader.addChild(createTestTextView())

        return listHeader
    }

    private fun createDefaultListheader(): ListHeader {
        return ListHeader(requireContext()).apply {
            this.setDefaultValues()
            this.addChild(createTestTextView())
            this.addChild(createTestTextView())
            this.addChild(createTestTextView())
        }
    }

    private fun getStyle(style: String): Int {
        return when(style){
            resources.getString(R.string.style_1) -> R.style.CustomSwitcherTitleTextStyle
            resources.getString(R.string.style_2) -> R.style.CustomSwitcherTextStyleBold
            else -> R.style.CustomSwitcherTextStyleNormal
        }
    }

    private fun getActionImage(): Int {
        val checkedButtonId = binding.fDynamicListheaderActionImage.checkedRadioButtonId
        val checkedButton = binding.fDynamicListheaderActionImage.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicListheaderImageRadio1 -> R.drawable.ic_arrow_down
            binding.fDynamicListheaderImageRadio2 -> R.drawable.ic_add
            else -> R.drawable.ic_arrow_drop_down
        }
    }

    private fun getActionImageTint(): Int {
        val checkedButtonId = binding.fDynamicListheaderTintRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicListheaderTintRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicListheaderTintRadio1 -> R.color.orange
            binding.fDynamicListheaderTintRadio2 -> R.color.purple
            binding.fDynamicListheaderTintRadio3 -> R.color.red
            binding.fDynamicListheaderTintRadio4 -> R.color.green
            else -> R.color.yellow
        }
    }

    private fun setListheaderPaddings(listHeader: ListHeader) {
        listHeader.setupPaddings(
            getDimensInDp(binding.fDynamicListheaderPaddingLeft),
            getDimensInDp(binding.fDynamicListheaderPaddingTop),
            getDimensInDp(binding.fDynamicListheaderPaddingRight),
            getDimensInDp(binding.fDynamicListheaderPaddingBottom)
        )
    }

    private fun setListheaderMargins(listHeader: ListHeader) {
        listHeader.setChildrenMargins(
            getDimensInDp(binding.fDynamicListheaderMarginTop),
            getDimensInDp(binding.fDynamicListheaderPaddingBottom)
        )
    }

    private fun createTestTextView(): TextView {
        return TextView(requireContext()).apply {
            text = resources.getString(R.string.listheader_test_text)
        }
    }

    private fun getDimensInDp(editField: EditText): Int {
        val text = editField.text.toString()
        return if (text.isNullOrEmpty()) 0 else dpToPx(text.toInt()).toInt()
    }

}