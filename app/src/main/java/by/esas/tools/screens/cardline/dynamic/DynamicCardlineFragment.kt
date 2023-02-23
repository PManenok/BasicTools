package by.esas.tools.screens.cardline.dynamic

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.cardline.CardLine
import by.esas.tools.databinding.FMainDynamicCardlineBinding
import by.esas.tools.utils.getDimensInDp
import by.esas.tools.utils.getFloatValue

class DynamicCardlineFragment : AppFragment<DynamicCardlineVM, FMainDynamicCardlineBinding>() {

    override val fragmentDestinationId = R.id.dynamicCardlineFragment

    override fun provideLayoutId() = R.layout.f_main_dynamic_cardline

    override fun provideViewModel(): DynamicCardlineVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(DynamicCardlineVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTopDividerSettingsLay()
        setBottomDividerSettingsLay()
        setStartIconSettingsLay()
        setEndIconSettingsLay()

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

        cardLine.setCardTitle(binding.fDynamicCardlineTitle.getText())
        val titleStyle =
            getTextStyle(binding.fDynamicCardlineSpinnerTitleStyle.selectedItem.toString())
        cardLine.setCardTitleStyle(titleStyle)
        cardLine.isTitleSingleLine(binding.fDynamicCardlineTitleSingleCheck.isChecked)
        setTitleWidthPercent(cardLine)

        cardLine.setCardValue(binding.fDynamicCardlineValue.getText())
        val valueStyle =
            getTextStyle(binding.fDynamicCardlineSpinnerValueStyle.selectedItem.toString())
        cardLine.setCardValueStyle(valueStyle)
        cardLine.isTitleSingleLine(binding.fDynamicCardlineValueSingleCheck.isChecked)
        cardLine.setupTextAlignVertical(binding.fDynamicCardlineTitleValueBias.getFloatValue())

        setTopDivider(cardLine)
        setBottomDivider(cardLine)

        setStartIcon(cardLine)
        setEndIcon(cardLine)

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
        return when (style) {
            resources.getString(R.string.style_1) -> R.style.CustomSwitcherTitleTextStyle
            resources.getString(R.string.style_2) -> R.style.CustomSwitcherTextStyleBold
            else -> R.style.CustomSwitcherTextStyleNormal
        }
    }

    private fun setTopDivider(cardLine: CardLine) {
        if (binding.fDynamicCardlineDivTopCheck.isChecked) {
            cardLine.setTopDividerHeight(getDimensInDp(binding.fDynamicCardlineDivTopHeight))
            val divColor = getColorFromRadioGroup(binding.fDynamicCardlineDivTopRadioGroup as RadioGroup)
            cardLine.setTopDividerColorRes(divColor)
        }
    }

    private fun setBottomDivider(cardLine: CardLine) {
        if (binding.fDynamicCardlineDivBottomCheck.isChecked) {
            cardLine.setBottomDividerHeight(getDimensInDp(binding.fDynamicCardlineDivBottomHeight))
            val divColor = getColorFromRadioGroup(binding.fDynamicCardlineDivBottomRadioGroup as RadioGroup)
            cardLine.setBottomDividerColorRes(divColor)
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
        val percent = binding.fDynamicCardlineTitleWidthPercent.getText()
        if (percent.isNotEmpty() && percent.toFloat() in 0f..1f)
            cardLine.setupTitleWidthPercent(percent.toFloat())
    }

    private fun setStartIconSettingsLay() {
        binding.fDynamicCardlineStartIconCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicCardlineStartIconLay.visibility = layVisibility
            binding.fDynamicCardlineStartImagePaddingsLay1.visibility = layVisibility
            binding.fDynamicCardlineStartImagePaddingsLay2.visibility = layVisibility
            binding.fDynamicCardlineStartImageSizeLay.visibility = layVisibility
            binding.fDynamicCardlineStartImagePaddingsText.visibility = layVisibility
        }
    }

    private fun getStartIconImage(): Int {
        val checkedButtonId = binding.fDynamicCardlineStartImageGroup.checkedRadioButtonId
        val checkedButton =
            binding.fDynamicCardlineStartImageGroup.findViewById<RadioButton>(checkedButtonId)

        return when (checkedButton) {
            binding.fDynamicCardlineStartImageRadio1 -> R.drawable.ic_star
            binding.fDynamicCardlineStartImageRadio2 -> R.drawable.ic_add
            else -> R.drawable.ic_remove
        }
    }

    private fun setStartIcon(cardLine: CardLine) {
        if (binding.fDynamicCardlineStartIconCheck.isChecked) {
            cardLine.setStartIcon(getStartIconImage())
            cardLine.setStartIconColorRes(getColorFromRadioGroup(binding.fDynamicCardlineStartImageColorGroup as RadioGroup))
            cardLine.setStartIconPadding(
                getDimensInDp(binding.fDynamicCardlineStartImagePaddingLeft),
                getDimensInDp(binding.fDynamicCardlineStartImagePaddingTop),
                getDimensInDp(binding.fDynamicCardlineStartImagePaddingRight),
                getDimensInDp(binding.fDynamicCardlineStartImagePaddingBottom)
            )
            cardLine.setStartIconSize(getDimensInDp(binding.fDynamicCardlineStartImageSize))
            cardLine.setStartIconAlignVertical(binding.fDynamicCardlineStartImageVerticalBias.getFloatValue())
        }
    }

    private fun setEndIconSettingsLay() {
        binding.fDynamicCardlineEndIconCheck.setOnCheckedChangeListener { _, isChecked ->
            val layVisibility = if (isChecked) View.VISIBLE else View.GONE
            binding.fDynamicCardlineEndIconLay.visibility = layVisibility
            binding.fDynamicCardlineEndImagePaddingsLay1.visibility = layVisibility
            binding.fDynamicCardlineEndImagePaddingsLay2.visibility = layVisibility
            binding.fDynamicCardlineEndImageSizeLay.visibility = layVisibility
            binding.fDynamicCardlineEndImagePaddingsText.visibility = layVisibility
        }
    }

    private fun getEndIconImage(): Int {
        val checkedButtonId = binding.fDynamicCardlineEndImageGroup.checkedRadioButtonId
        val checkedButton =
            binding.fDynamicCardlineEndImageGroup.findViewById<RadioButton>(checkedButtonId)

        return when (checkedButton) {
            binding.fDynamicCardlineEndImageRadio1 -> R.drawable.ic_star
            binding.fDynamicCardlineEndImageRadio2 -> R.drawable.ic_add
            else -> R.drawable.ic_remove
        }
    }

    private fun setEndIcon(cardLine: CardLine) {
        if (binding.fDynamicCardlineEndIconCheck.isChecked) {
            cardLine.setEndIcon(getEndIconImage())
            cardLine.setEndIconColorRes(getColorFromRadioGroup(binding.fDynamicCardlineEndImageColorGroup as RadioGroup))
            cardLine.setEndIconPadding(
                getDimensInDp(binding.fDynamicCardlineEndImagePaddingLeft),
                getDimensInDp(binding.fDynamicCardlineEndImagePaddingTop),
                getDimensInDp(binding.fDynamicCardlineEndImagePaddingRight),
                getDimensInDp(binding.fDynamicCardlineEndImagePaddingBottom)
            )
            cardLine.setEndIconSize(getDimensInDp(binding.fDynamicCardlineEndImageSize))
            cardLine.setEndIconAlignVertical(binding.fDynamicCardlineEndImageVerticalBias.getFloatValue())
        }
    }

    private fun getColorFromRadioGroup(radioGroup: RadioGroup): Int {
        val checkedButtonId = radioGroup.checkedRadioButtonId
        val checkedButton = radioGroup.findViewById<RadioButton>(checkedButtonId)

        return when (radioGroup.indexOfChild(checkedButton)) {
            0 -> R.color.orange
            1 -> R.color.purple
            else -> R.color.red
        }
    }
}
