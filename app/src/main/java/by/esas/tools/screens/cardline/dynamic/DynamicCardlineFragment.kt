package by.esas.tools.screens.cardline.dynamic

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.cardline.CardLine
import by.esas.tools.databinding.FMainDynamicCardlineBinding
import by.esas.tools.utils.getDimensInDp

class DynamicCardlineFragment: AppFragment<DynamicCardlineVM, FMainDynamicCardlineBinding>() {
    override val fragmentDestinationId = R.id.dynamicCardlineFragment

    override fun provideLayoutId() = R.layout.f_main_dynamic_cardline

    override fun provideViewModel(): DynamicCardlineVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(DynamicCardlineVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopDividerSettingsLay()
        setBottomDividerSettingsLay()
        setStartIconSettingsLay()

        binding.fDynamicCardlineButtonCreate.setOnClickListener {
            binding.fDynamicCardlineContainer.removeAllViews()
            binding.fDynamicCardlineContainer.addView(createCustomCardline())
        }

        binding.fDynamicCardlineButtonCreateDefault.setOnClickListener {
            binding.fDynamicCardlineContainer.removeAllViews()
            binding.fDynamicCardlineContainer.addView(createDefaultCardline())
        }

    }

    private fun createDefaultCardline(): CardLine {
        return CardLine(requireContext()).apply {
            this.setDefaultValues()
        }
    }

    private fun createCustomCardline(): CardLine {
        val cardLine = CardLine(requireContext())

        cardLine.setCardTitle(binding.fDynamicCardlineTitle.text.toString())
        val titleStyle = getTextStyle(binding.fDynamicCardlineSpinnerTitleStyle.selectedItem.toString())
        cardLine.setCardTitleStyle(titleStyle)
        cardLine.isTitleSingleLine(binding.fDynamicCardlineTitleSingleCheck.isChecked)
        setTitleWidthPercent(cardLine)

        cardLine.setCardValue(binding.fDynamicCardlineValue.text.toString())
        val valueStyle = getTextStyle(binding.fDynamicCardlineSpinnerValueStyle.selectedItem.toString())
        cardLine.setCardValueStyle(valueStyle)
        cardLine.isTitleSingleLine(binding.fDynamicCardlineValueSingleCheck.isChecked)

        setTopDivider(cardLine)
        setBottomDivider(cardLine)

        setCardlineContainerPaddings(cardLine)
        return cardLine
    }

    private fun setBottomDividerSettingsLay() {
        binding.fDynamicCardlineDivBottomCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicCardlineDivBottomLay.visibility = layVisibility
        }
    }

    private fun setTopDividerSettingsLay() {
        binding.fDynamicCardlineDivTopCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicCardlineDivTopLay.visibility = layVisibility
        }
    }

    private fun getTextStyle(style: String): Int {
        return when(style){
            resources.getString(R.string.style_1) -> R.style.CustomSwitcherTitleTextStyle
            resources.getString(R.string.style_2) -> R.style.CustomSwitcherTextStyleBold
            else -> R.style.CustomSwitcherTextStyleNormal
        }
    }

    private fun setTopDivider(cardLine: CardLine) {
        if (binding.fDynamicCardlineDivTopCheck.isChecked) {
            cardLine.setTopDividerHeight(getDimensInDp(binding.fDynamicCardlineDivTopHeight))
            cardLine.setTopDividerColorRes(getTopDividerColor())
        }
    }

    private fun setBottomDivider(cardLine: CardLine) {
        if (binding.fDynamicCardlineDivBottomCheck.isChecked) {
            cardLine.setBottomDividerHeight(getDimensInDp(binding.fDynamicCardlineDivBottomHeight))
            cardLine.setBottomDividerColorRes(getBottomDividerColor())
        }
    }

    private fun getTopDividerColor(): Int {
        val checkedButtonId = binding.fDynamicCardlineDivTopRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicCardlineDivTopRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicCardlineDivTopRadio1 -> R.color.orange
            binding.fDynamicCardlineDivTopRadio2 -> R.color.purple
            else -> R.color.red
        }
    }

    private fun getBottomDividerColor(): Int {
        val checkedButtonId = binding.fDynamicCardlineDivBottomRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicCardlineDivBottomRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicCardlineDivBottomRadio1 -> R.color.orange
            binding.fDynamicCardlineDivBottomRadio2 -> R.color.purple
            else -> R.color.red
        }
    }

    private fun setCardlineContainerPaddings(cardLine: CardLine) {
        cardLine.setContainerPaddings(
            getDimensInDp(binding.fDynamicCardlinePaddingLeft),
            getDimensInDp(binding.fDynamicCardlinePaddingTop),
            getDimensInDp(binding.fDynamicCardlinePaddingRight),
            getDimensInDp(binding.fDynamicCardlinePaddingBottom)
        )
    }

    private fun setTitleWidthPercent(cardLine: CardLine) {
        val percent = binding.fDynamicCardlineTitleWidthPercent.text.toString()
        if (percent.isNotEmpty() && percent.toFloat() in 0f..1f)
            cardLine.setupTitleWidthPercent(percent.toFloat())
    }

    private fun setStartIconSettingsLay() {
        binding.fDynamicCardlineStartIconCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicCardlineStartIconLay.visibility = layVisibility
        }
    }

    private fun getStartIconImage(): Int {
        val checkedButtonId = binding.fDynamicCardlinaStartImageGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicCardlinaStartImageGroup.findViewById<RadioButton>(checkedButtonId)

        return when (checkedButton) {
            binding.fDynamicCardlinaStartImageRadio1 -> R.drawable.ic_star
            binding.fDynamicCardlinaStartImageRadio2 -> R.drawable.ic_add
            else -> R.drawable.ic_remove
        }
    }
}
