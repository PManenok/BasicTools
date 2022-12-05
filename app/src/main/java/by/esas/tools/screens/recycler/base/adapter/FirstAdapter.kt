package by.esas.tools.screens.recycler.base.adapter

import android.view.ViewGroup
import by.esas.tools.recycler.BaseRecyclerAdapter
import by.esas.tools.recycler.BaseViewHolder
import by.esas.tools.screens.recycler.FirstEntity

class FirstAdapter(
    onClick: (FirstEntity) -> Unit,
    onClickPosition: (Int, FirstEntity) -> Unit,
    onLongClick: (FirstEntity) -> Unit,
    onLongClickPosition: (Int, FirstEntity) -> Unit
) : BaseRecyclerAdapter<FirstEntity, FirstItemVM>(
    onItemClick = onClick, onItemClickPosition = onClickPosition,
    onItemLongClick = onLongClick, onItemLongClickPosition = onLongClickPosition
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FirstEntity, FirstItemVM, *> {
        return FirstViewHolder.create(parent, FirstItemVM())
    }
}