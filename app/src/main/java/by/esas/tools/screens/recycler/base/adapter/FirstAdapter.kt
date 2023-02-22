package by.esas.tools.screens.recycler.base.adapter

import android.view.ViewGroup
import by.esas.tools.recycler.BaseRecyclerAdapter
import by.esas.tools.recycler.BaseViewHolder
import by.esas.tools.screens.recycler.RecyclerEntity

class FirstAdapter(
    onClick: (RecyclerEntity) -> Unit,
    onClickPosition: (Int, RecyclerEntity) -> Unit,
    onLongClick: (RecyclerEntity) -> Unit,
    onLongClickPosition: (Int, RecyclerEntity) -> Unit
) : BaseRecyclerAdapter<RecyclerEntity, FirstItemVM>(
    onItemClick = onClick, onItemClickPosition = onClickPosition,
    onItemLongClick = onLongClick, onItemLongClickPosition = onLongClickPosition
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RecyclerEntity, FirstItemVM, *> {
        return FirstViewHolder.create(parent, FirstItemVM())
    }
}