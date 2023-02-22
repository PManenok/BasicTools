/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler.simpleItemAdapter

import android.view.View
import by.esas.tools.recycler.ICheckDiff

data class SimpleItemModel(
    val code: String,
    val name: String,
    val isChoosed: Boolean,
    var isLast: Boolean,
    val textAlignment: Int = View.TEXT_ALIGNMENT_TEXT_START
) : ICheckDiff<SimpleItemModel> {

    override fun checkIfSameItem(item: SimpleItemModel): Boolean {
        return this.name == item.name && this.code == item.code
    }

    override fun checkIfSameContent(item: SimpleItemModel): Boolean {
        return this == item
    }

    override fun equals(other: Any?): Boolean {
        //if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SimpleItemModel

        if (code != other.code) return false
        if (name != other.name) return false
        if (isChoosed != other.isChoosed) return false
        if (isLast != other.isLast) return false
        if (textAlignment != other.textAlignment) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + isChoosed.hashCode()
        result = 31 * result + isLast.hashCode()
        result = 31 * result + textAlignment
        return result
    }
}