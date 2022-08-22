package by.esas.tools.screens.cardline

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainCardlineBinding

class CardlineFragment : AppFragment<CardlineVM, FMainCardlineBinding >() {
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


    }

}