package by.esas.tools.screens.recycler.base.adapter

import androidx.databinding.ObservableField
import by.esas.tools.recycler.BaseItemViewModel
import by.esas.tools.screens.recycler.FirstEntity

class FirstItemVM : BaseItemViewModel<FirstEntity>() {
    val text = ObservableField<String>("")
    val counter = ObservableField<String>("")
    private var innerCounter = 0

    override fun bindItem(item: FirstEntity, position: Int) {
        this.position.set(position)
        text.set(item.name)
        innerCounter = 0
        counter.set(innerCounter.toString())
    }

    override fun onItemClick() {
        super.onItemClick()
        counter.set((++innerCounter).toString())
    }
}