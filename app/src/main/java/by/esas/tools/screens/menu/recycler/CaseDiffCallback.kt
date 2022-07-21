package by.esas.tools.screens.menu.recycler

import androidx.recyclerview.widget.DiffUtil
import by.esas.tools.entity.CaseItemInfo

class CaseDiffCallback(
    private val oldList: List<CaseItemInfo>,
    private val newList: List<CaseItemInfo>
): DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.name == newItem.name && oldItem.modules == newItem.modules
    }
}