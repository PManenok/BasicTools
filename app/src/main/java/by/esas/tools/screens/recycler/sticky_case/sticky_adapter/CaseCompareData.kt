package by.esas.tools.screens.recycler.sticky_case.sticky_adapter

import by.esas.tools.recycler.sticky.CompareData
import by.esas.tools.recycler.sticky.StickyHeader

class CaseCompareData(val firstLetter: String) : CompareData {
    override fun compareTo(another: CompareData): Boolean {
        return if (another is CaseCompareData) {
            firstLetter == another.firstLetter
        } else {
            false
        }
    }

    override fun createHeader(): StickyHeader {
        return StickyHeader(firstLetter)
    }
}