package by.esas.tools.screens.recycler

import by.esas.tools.recycler.simpleItemAdapter.SimpleItemModel

class RecyclerEntity(
    val name: String
)

fun RecyclerEntity.mapToSimple(isLast: Boolean, alignment: Int): SimpleItemModel {
    return SimpleItemModel(name = name, code = name, isChoosed = false, isLast = isLast, textAlignment = alignment)
}