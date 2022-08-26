package by.esas.tools.screens.topbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainTopbarBinding

class TopbarFragment: AppFragment<TopbarVM, FMainTopbarBinding>() {
    override val fragmentDestinationId = R.id.topbarFragment
    override fun provideLayoutId() = R.layout.f_main_topbar

    override fun provideViewModel(): TopbarVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory()).get(TopbarVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainTopbarButtonChangeIcons.setOnClickListener {
            binding.fMainTopbarCase6.apply {
                setTitle(R.string.topbar_title)
                setStartActionTitle(R.string.topbar_action_cancel)
                setActionIconImage(R.drawable.ic_done)
            }
        }

        binding.fMainTopbarButtonChangeStyle.setOnClickListener {
            binding.fMainTopbarCase7.apply {
                setTitleStyle(R.style.TextStyle)
                setStartActionViewStyle(R.style.TextStyle)
                setEndActionViewStyle(R.style.TextStyle)
                setDividerVisibility(true)
                setDividerColor(R.color.main_color_2)
            }
        }
    }
}