package by.esas.tools.dialog.simpleItemAdapter

import android.view.ViewGroup
import by.esas.tools.recycler.BaseRecyclerAdapter
import by.esas.tools.dialog.simpleItemAdapter.SimpleItemViewHolder

class SimpleItemAdapter(onclick: (Int, SimpleItemModel) -> Unit) :
    BaseRecyclerAdapter<SimpleItemModel, SimpleItemViewModel>(onItemClickPosition = onclick) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleItemViewHolder<*> {
        return if (viewType == 1) {
            SimpleItemViewHolder.createForMessage(
                parent,
                SimpleItemViewModel()
            )
        } else {
            SimpleItemViewHolder.create(
                parent,
                SimpleItemViewModel()
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemList.getOrNull(position)
        return if (item?.shortName.isNullOrBlank()) 1
        else 0
    }
}