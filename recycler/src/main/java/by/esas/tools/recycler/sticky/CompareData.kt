package by.esas.tools.recycler.sticky

interface CompareData {
    fun compareTo(another: CompareData): Boolean
    fun createHeader(): StickyHeader
}