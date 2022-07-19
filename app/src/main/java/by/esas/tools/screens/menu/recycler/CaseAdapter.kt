package by.esas.tools.screens.menu.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import by.esas.tools.recycler.BaseRecyclerAdapter
import by.esas.tools.recycler.BaseViewHolder

class CaseAdapter(onClick: (CaseItemInfo) -> Unit): BaseRecyclerAdapter<CaseItemInfo, CaseViewModel>(onItemClick = onClick) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CaseItemInfo, CaseViewModel, *> {
        return CaseViewHolder.create(parent, CaseViewModel())
    }

    override fun addItems(items: List<CaseItemInfo>) {
        val diffCallback = CaseDiffCallback(itemList, items)
        val difResult = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(items)
        difResult.dispatchUpdatesTo(this)
    }
}