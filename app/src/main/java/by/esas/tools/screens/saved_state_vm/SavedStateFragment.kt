package by.esas.tools.screens.saved_state_vm

import androidx.lifecycle.ViewModelProvider
import by.esas.tools.R
import by.esas.tools.base.AppFragment
import by.esas.tools.databinding.FMainSavedStateBinding

class SavedStateFragment : AppFragment<SavedStateVM, FMainSavedStateBinding>() {

    override val fragmentDestinationId: Int = R.id.savedStateFragment

    override fun provideLayoutId(): Int {
        return R.layout.f_main_saved_state
    }

    override fun provideViewModel(): SavedStateVM {
        return ViewModelProvider(this, viewModelFactory.provideFactory(this, arguments)).get(SavedStateVM::class.java)
    }
}