package by.esas.tools

import androidx.lifecycle.SavedStateHandle
import by.esas.tools.basedaggerui.factory.AssistedSavedStateViewModelFactory
import by.esas.tools.error_mapper.AppErrorMapper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AddInvoiceViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val mapper: AppErrorMapper
) : AppVM() {
    override val TAG: String = AddInvoiceViewModel::class.java.simpleName

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<AddInvoiceViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): AddInvoiceViewModel  // may be ommited prior kotlin 1.3.60 or after PR #121 in AssistedInject lib
    }
}