package by.esas.tools.screens.saved_state_vm

import androidx.lifecycle.SavedStateHandle
import by.esas.tools.base.AppVM
import by.esas.tools.basedaggerui.factory.AssistedSavedStateViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SavedStateVM @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : AppVM() {

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<SavedStateVM> {
        override fun create(savedStateHandle: SavedStateHandle): SavedStateVM  // may be ommited prior kotlin 1.3.60 or after PR #121 in AssistedInject lib
    }

}