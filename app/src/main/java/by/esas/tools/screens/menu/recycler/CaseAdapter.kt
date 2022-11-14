package by.esas.tools.screens.menu.recycler

import android.view.ViewGroup
import by.esas.tools.entity.CaseItemInfo
import by.esas.tools.recycler.BaseRecyclerAdapter
import by.esas.tools.recycler.BaseViewHolder

class CaseAdapter(onClick: (CaseItemInfo) -> Unit) :
    BaseRecyclerAdapter<CaseItemInfo, CaseViewModel>(onItemClick = onClick) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CaseItemInfo, CaseViewModel, *> {
        return CaseViewHolder.create(parent, CaseViewModel())
    }
}