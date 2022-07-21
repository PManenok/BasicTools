package by.esas.tools.entity

import androidx.navigation.NavDirections

data class CaseItemInfo(
    val id: Int,
    val name: String,
    val modules: List<ModuleEnum>,
    val direction: NavDirections? = null
)