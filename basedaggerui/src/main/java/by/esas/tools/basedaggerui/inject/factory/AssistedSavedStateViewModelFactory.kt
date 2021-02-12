package by.esas.tools.basedaggerui.inject.factory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface AssistedSavedStateViewModelFactory<T : ViewModel> {
   fun create(savedStateHandle: SavedStateHandle): T
}