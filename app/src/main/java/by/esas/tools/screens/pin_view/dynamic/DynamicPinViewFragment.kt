package by.esas.tools.screens.pin_view.dynamic

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainDynamicPinViewBinding
import by.esas.tools.inputfieldview.dpToPx
import by.esas.tools.pinview.PinView

class DynamicPinViewFragment: AppFragment<DynamicPinViewVM, FMainDynamicPinViewBinding>() {
    override val fragmentDestinationId = R.id.dynamicPinViewFragment

    override fun provideLayoutId() = R.layout.f_main_dynamic_pin_view

    override fun provideViewModel(): DynamicPinViewVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(DynamicPinViewVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fDynamicPinButtonCreate.setOnClickListener {
            binding.fDynamicPinContainer.removeAllViews()
            binding.fDynamicPinButtonsLay.visibility = View.VISIBLE

            val pin = PinView(requireContext())
            pin.setDefaults()
            pin.setPinAmount(binding.fDynamicPinAmount.getText().convertToInt())
            pin.setFilledTintRes(getFilledTint())
            pin.setUnfilledTintRes(getUnfilledTint())
            pin.setUnfilledImage(getUnfilledImage())
            pin.setFilledImage(getFilledImage())
            pin.setAnimInDuration(binding.fDynamicPinTimeIn.getText().convertToInt())
            pin.setAnimOutDuration(binding.fDynamicPinTimeOut.getText().convertToInt())
            pin.setPinSize(dpToPx(binding.fDynamicPinSize.getText().convertToInt()))
            pin.setPinMargins(dpToPx(binding.fDynamicPinMargins.getText().convertToInt()))

            binding.fDynamicPinContainer.addView(pin)
            setupPinButtonsListeners(pin)
        }
    }

    private fun setupPinButtonsListeners(pinView: PinView) {
        binding.fDynamicPinButtonAdd.setOnClickListener { pinView.fillPin() }
        binding.fDynamicPinButtonDelete.setOnClickListener { pinView.unFillPin() }
        binding.fDynamicPinButtonClear.setOnClickListener { pinView.clearPins() }
    }

    private fun getFilledTint(): Int {
        val checkedButtonId = binding.fDynamicPinFilledRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicPinFilledRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicPinFilledRadio1 -> R.color.orange
            binding.fDynamicPinFilledRadio2 -> R.color.purple
            binding.fDynamicPinFilledRadio3 -> R.color.red
            binding.fDynamicPinFilledRadio4 -> R.color.green
            else -> R.color.yellow
        }
    }

    private fun getUnfilledTint(): Int {
        val checkedButtonId = binding.fDynamicPinUnfilledRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicPinUnfilledRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicPinUnfilledRadio1 -> R.color.orange
            binding.fDynamicPinUnfilledRadio2 -> R.color.purple
            binding.fDynamicPinUnfilledRadio3 -> R.color.red
            binding.fDynamicPinUnfilledRadio4 -> R.color.green
            else -> R.color.yellow
        }
    }

    private fun getFilledImage(): Int {
        val checkedButtonId = binding.fDynamicPinImageFilledRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicPinImageFilledRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicPinImageFilledRadio1 -> R.drawable.ic_pin_star_filled
            binding.fDynamicPinImageFilledRadio2 -> R.drawable.ic_favorite
            else -> R.drawable.ic_cloud
        }
    }

    private fun getUnfilledImage(): Int {
        val checkedButtonId = binding.fDynamicPinImageUnfilledRadioGroup.checkedRadioButtonId
        val checkedButton = binding.fDynamicPinImageUnfilledRadioGroup.findViewById<RadioButton>(checkedButtonId)

        return when(checkedButton) {
            binding.fDynamicPinImageUnfilledRadio1 -> R.drawable.ic_pin_star_unfilled
            binding.fDynamicPinImageUnfilledRadio2 -> R.drawable.ic_favorite_border
            else -> R.drawable.ic_cloud_queue
        }
    }

    private fun getPaddings(paddings: String): Int {
        val paddings = if (paddings.isNotEmpty()) paddings.toInt() else 0
        return dpToPx(paddings).toInt()
    }

    private fun String.convertToInt(): Int {
        return if (this.isNotEmpty()) this.toInt() else 0
    }
}