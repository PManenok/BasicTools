package by.esas.tools.screens.listheader.dynamic

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainDynamicListheaderBinding
import by.esas.tools.listheader.ListHeader
import by.esas.tools.utils.getDimensInDp

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
        listHeader.setListTitle(binding.fDynamicListheaderTitle.getText())
        listHeader.setListActionText(binding.fDynamicListheaderActionText.getText())
        listHeader.setActionListener {
            Toast.makeText(requireContext(), resources.getString(R.string.listheader_action_click), Toast.LENGTH_SHORT)
                .show()
        }
        listHeader.setListTitleStyle(getStyle(binding.fDynamicListheaderSpinnerTitle.selectedItem.toString()))
        listHeader.setListActionStyle(getStyle(binding.fDynamicListheaderSpinnerActionText.selectedItem.toString()))
        listHeader.setArrowIcon(getActionImages().first)
        listHeader.setArrowIconSize(getDimensInDp(binding.fDynamicListheaderImageSize))
        listHeader.setArrowIconTintResource(getActionImageTint())
        listHeader.addOpenedListener(object : ListHeader.ListOpenedListener {
            override fun onListStateChanged(isOpen: Boolean) {
                when (isOpen) {
                    true -> listHeader.setArrowIcon(getActionImages().first)
                    false -> listHeader.setArrowIcon(getActionImages().second)
                }
            }
        })
        setListheaderPaddings(listHeader)
        setListheaderMargins(listHeader)

        listHeader.setDefaultContainerListener()
        listHeader.setArrowClickable(true)
        listHeader.setArrowListener {
            Toast.makeText(requireContext(), resources.getString(R.string.listheader_icon_click), Toast.LENGTH_SHORT)
                .show()
        }

        listHeader.addChild(createTestTextView())
        listHeader.addChild(createTestTextView())
        listHeader.addChild(createTestTextView())

        return listHeader
    }

    private fun createDefaultListheader(): ListHeader {
        return ListHeader(requireContext()).apply {
            this.setDefaultValues()
            this.setActionListener {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.listheader_action_click),
                    Toast.LENGTH_SHORT
                ).show()
            }
            this.addChild(createTestTextView())
            this.addChild(createTestTextView())
            this.addChild(createTestTextView())
        }
    }

    private fun getStyle(style: String): Int {
        return when (style) {
            resources.getString(R.string.style_1) -> R.style.CustomSwitcherTitleTextStyle
            resources.getString(R.string.style_2) -> R.style.CustomSwitcherTextStyleBold
            else -> R.style.CustomSwitcherTextStyleNormal
        }
    }

    private fun getActionImages(): Pair<Int, Int> {
        val checkedButtonId = binding.fDynamicListheaderActionImage.checkedRadioButtonId
        val checkedButton = binding.fDynamicListheaderActionImage.findViewById<RadioButton>(checkedButtonId)

        return when (checkedButton) {
            binding.fDynamicListheaderImageRadio1 -> Pair(R.drawable.ic_arrow_upward, R.drawable.ic_arrow_downward)
            binding.fDynamicListheaderImageRadio2 -> Pair(R.drawable.ic_remove, R.drawable.ic_add)
            else -> Pair(R.drawable.ic_arrow_drop_up, R.drawable.ic_arrow_drop_down)
        }
    }

    private fun getActionImageTint(): Int {
        val checkedButtonId = binding.fDynamicListheaderTintRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicListheaderTintRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when (checkedButton) {
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
            if (Build.VERSION.SDK_INT >= 23)
                setTextAppearance(R.style.HintTextStyle)
            else
                setTextAppearance(this.context, R.style.HintTextStyle)
        }
    }
}
