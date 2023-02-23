package by.esas.tools.screens.cardline

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainCardlineBinding

class CardlineFragment : AppFragment<CardlineVM, FMainCardlineBinding>() {

    override val fragmentDestinationId = R.id.cardlineFragment

    override fun provideLayoutId(): Int = R.layout.f_main_cardline

    override fun provideViewModel(): CardlineVM {
        return ViewModelProvider(
            this,
            viewModelFactory.provideFactory()
        ).get(CardlineVM::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fMainCardlineChangeStyle.setOnClickListener {
            binding.fMainCardlineStyle.apply {
                setStartIcon(R.drawable.ic_star)
                setStartIconColor(R.color.main_color_2)
                setEndIcon(R.drawable.ic_star)
                setEndIconColor(R.color.main_color_2)
                setBottomDividerColor(R.color.main_color_2)
                setTopDividerColor(R.color.main_color_2)
                setCardTitleStyle(R.style.TextStyle)
                setCardValueStyle(R.style.TextStyle)
                setAllAlignVertical(0.5f)
            }
            it.isEnabled = false
        }
    }
}