package by.esas.tools.entity

import androidx.navigation.NavDirections
import by.esas.tools.recycler.ICheckDiff

data class CaseItemInfo(
    val id: Int,
    val name: String,
    val modules: List<String>,
    val direction: NavDirections? = null
) : ICheckDiff<CaseItemInfo> {

    override fun checkIfSameItem(item: CaseItemInfo): Boolean {
        return id == item.id
    }

    override fun checkIfSameContent(item: CaseItemInfo): Boolean {
        if (id != item.id) return false
        else if (name != item.name) return false
        else if (modules.size != item.modules.size || !modules.containsAll(item.modules)) return false
        else if (direction != item.direction) return false

        return true
    }
}