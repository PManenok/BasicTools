package by.esas.tools.screens.recycler

import by.esas.tools.recycler.simpleItemAdapter.SimpleItemModel

class FirstEntity(
    val name: String
)

fun FirstEntity.mapToSimple(isLast: Boolean, alignment: Int): SimpleItemModel {
    return SimpleItemModel(name = name, shortName = name, isChoosed = false, isLast = isLast, textAlignment = alignment)
}