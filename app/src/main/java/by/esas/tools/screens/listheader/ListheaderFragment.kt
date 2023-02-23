package by.esas.tools.screens.listheader

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainListheaderBinding
import by.esas.tools.listheader.ListHeader
import by.esas.tools.util.SwitchManager

class ListheaderFragment : AppFragment<ListheaderVM, FMainListheaderBinding>() {

    override val fragmentDestinationId = R.id.listheaderFragment

    override fun provideLayoutId() = R.layout.f_main_listheader

    override fun provideViewModel(): ListheaderVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(ListheaderVM::class.java)
    }

    override var switcher = object : SwitchManager() {
        override fun enableView(view: View): Boolean {
            return if (view is Button) {
                view.isEnabled = true
                true
            } else
                super.enableView(view)
        }

        override fun disableView(view: View): Boolean {
            return if (view is Button) {
                view.isEnabled = false
                true
            } else
                super.disableView(view)
        }
    }

    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fMainListheader1,
            binding.fMainListheader2,
            binding.fMainListheader3,
            binding.fMainListheaderAddButton
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainListheaderSwitcher.switcherIsChecked(true)
        binding.fMainListheaderSwitcher.setSwitchHandler(object : ISwitchHandler {
            override fun onSwitchChange(isChecked: Boolean) {
                if (isChecked)
                    viewModel.enableControls()
                else
                    viewModel.disableControls()
            }
        })

        binding.fMainListheader1.addOpenedListener(object : ListHeader.ListOpenedListener {
            override fun onListStateChanged(isOpen: Boolean) {
                if (isOpen)
                    binding.fMainListheader1.setArrowIcon(R.drawable.ic_arrow_up)
                else
                    binding.fMainListheader1.setArrowIcon(R.drawable.ic_arrow_down)
            }
        })
        binding.fMainListheaderAddButton.setOnClickListener {
            binding.fMainListheader1.addChild(createTestTextView())
        }

        binding.fMainListheader2.setArrowListener {
            Toast.makeText(requireContext(), resources.getString(R.string.listheader_icon_click), Toast.LENGTH_SHORT)
                .show()
        }

        binding.fMainListheader3.addOpenedListener(object : ListHeader.ListOpenedListener {
            override fun onListStateChanged(isOpen: Boolean) {
                if (isOpen)
                    binding.fMainListheaderHiddenLay3.visibility = View.VISIBLE
                else
                    binding.fMainListheaderHiddenLay3.visibility = View.GONE
            }
        })
        binding.fMainListheader3.setActionListener {
            Toast.makeText(requireContext(), resources.getString(R.string.listheader_action_click), Toast.LENGTH_SHORT)
                .show()
        }
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
