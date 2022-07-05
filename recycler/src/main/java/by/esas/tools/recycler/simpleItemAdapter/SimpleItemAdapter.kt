package by.esas.tools.recycler.simpleItemAdapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import by.esas.tools.recycler.BaseRecyclerAdapter

open class SimpleItemAdapter(onclick: (Int, SimpleItemModel) -> Unit) :
    BaseRecyclerAdapter<SimpleItemModel, SimpleItemViewModel>(onItemClickPosition = onclick) {

    companion object {
        fun <T : ViewDataBinding> createCustom(
            inflater: Class<T>,
            onclick: (Int, SimpleItemModel) -> Unit
        ): SimpleItemAdapter {
            return object : SimpleItemAdapter(onclick) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleItemViewHolder<*> {
                    return SimpleItemViewHolder.createBinding(
                        inflater,
                        parent,
                        SimpleItemViewModel()
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleItemViewHolder<*> {
        return SimpleItemViewHolder.create(
            parent,
            SimpleItemViewModel()
        )
    }
}