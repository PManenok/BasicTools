package by.esas.tools.screens.saved_state_vm

import androidx.lifecycle.MutableLiveData
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
        override fun create(savedStateHandle: SavedStateHandle): SavedStateVM
    }

    val firstField = savedStateHandle.getLiveData<String>("firstField", "")
    val secondField = MutableLiveData("")
}