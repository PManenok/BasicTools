package by.esas.tools.screens.listheader

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.customswitch.ISwitchHandler
import by.esas.tools.databinding.FMainListheaderBinding
import by.esas.tools.generated.callback.OnClickListener
import by.esas.tools.listheader.ListHeader

class ListheaderFragment: AppFragment<ListheaderVM, FMainListheaderBinding>() {
    override val fragmentDestinationId = R.id.listheaderFragment

    override fun provideLayoutId() = R.layout.f_main_listheader

    override fun provideViewModel(): ListheaderVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(ListheaderVM::class.java)
    }

    override fun provideSwitchableViews(): List<View?> {
        return listOf(
            binding.fMainListheader1,
            binding.fMainListheader2,
            binding.fMainListheader3
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainListheaderSwitcher.switcherIsChecked(true)
        binding.fMainListheaderSwitcher.setSwitchHandler(object : ISwitchHandler {
            override fun onSwitchChange(isChecked: Boolean) {
                if (isChecked)
                    enableControls()
                else
                    disableControls()
            }
        })

        binding.fMainListheader1.addOpenedListener(object : ListHeader.ListOpenedListener {
            override fun onListStateChanged(isOpen: Boolean) {
                if (isOpen){
                    binding.fMainListheaderHiddenLay1.visibility = View.VISIBLE
                    binding.fMainListheader1.setArrowIconImage(R.drawable.ic_arrow_up)
                }
                else {
                    binding.fMainListheaderHiddenLay1.visibility = View.GONE
                    binding.fMainListheader1.setArrowIconImage(R.drawable.ic_arrow_down)
                }
            }
        })

        binding.fMainListheader2.setArrowListener {
            Toast.makeText(requireContext(), resources.getString(R.string.listheader_icon_click), Toast.LENGTH_SHORT).show()
        }

        binding.fMainListheader3.addOpenedListener(object : ListHeader.ListOpenedListener {
            override fun onListStateChanged(isOpen: Boolean) {
                if (isOpen){
                    binding.fMainListheaderHiddenLay3.visibility = View.VISIBLE
                }
                else {
                    binding.fMainListheaderHiddenLay3.visibility = View.GONE
                }
            }
        })
        binding.fMainListheader3.setActionListener{
            Toast.makeText(requireContext(), resources.getString(R.string.listheader_action_click), Toast.LENGTH_SHORT).show()
        }
    }
}