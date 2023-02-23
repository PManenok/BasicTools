package by.esas.tools.screens.recycler.sticky_case.sticky_adapter

import androidx.databinding.ObservableField
import by.esas.tools.recycler.BaseItemViewModel
import by.esas.tools.recycler.sticky.StickyEntity
import by.esas.tools.screens.recycler.RecyclerEntity

class CaseStickyItemVM : BaseItemViewModel<StickyEntity<RecyclerEntity>>() {

    val title = ObservableField<String>("t")

    val name = ObservableField<String>("n")
    val counter = ObservableField<String>("0")
    private var innerCounter = 0

    override fun bindItem(item: StickyEntity<RecyclerEntity>, position: Int) {
        this.position.set(position)
        item.header?.run { title.set(headerText) }
        item.entity?.let {
            name.set(it.name)
            innerCounter = 0
            counter.set(innerCounter.toString())
        }
    }

    override fun onItemClick() {
        super.onItemClick()
        counter.set((++innerCounter).toString())
    }
}
