package by.esas.tools.screens.menu.recycler

import androidx.navigation.NavDirections

data class CaseItemInfo(
    val id: Int,
    val name: String,
    val modules: List<String>,
    val direction: NavDirections? = null
)