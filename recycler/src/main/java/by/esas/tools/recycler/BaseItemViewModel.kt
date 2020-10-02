package by.esas.tools.recycler

import androidx.databinding.ObservableInt

abstract class BaseItemViewModel<Entity> {
    val position = ObservableInt()
    abstract fun bindItem(item: Entity, position: Int)
    open fun onItemClick() {}

}