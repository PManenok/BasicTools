/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler.sticky

import androidx.recyclerview.widget.DiffUtil
import by.esas.tools.recycler.ICheckDiff

class StickyDiffCallback<Entity>(
    private val oldList: List<StickyEntity<Entity>>,
    private val newList: List<StickyEntity<Entity>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: StickyEntity<Entity> = oldList[oldItemPosition]
        val newItem: StickyEntity<Entity> = newList[newItemPosition]

        return if (oldItem.header != null && newItem.header != null) {
            oldItem.header.headerText == newItem.header.headerText
        } else if (oldItem.entity != null && newItem.entity != null) {
            if (oldItem.entity is ICheckDiff<*>) {
                (oldItem.entity as ICheckDiff<Entity>).checkIfSameItem(newItem.entity)
            } else {
                //You need to override method and add you realisation of the check
                // if your Entity does not implement ICheckDiff interface, otherwise it will always return false
                customCheckIfItemsTheSame(oldItem, newItem)
            }
        } else {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem.header != null && newItem.header != null) {
            oldItem.header.headerText == newItem.header.headerText
        } else if (oldItem.entity != null && newItem.entity != null) {
            if (oldItem.entity is ICheckDiff<*>) {
                (oldItem.entity as ICheckDiff<Entity>).checkIfSameContent(newItem.entity)
            } else {
                //You need to override method and add you realisation of the check
                // if your Entity does not implement ICheckDiff interface, otherwise it will always return false
                customCheckIfContentsTheSame(oldItem, newItem)
            }
        } else {
            false
        }
    }

    open fun customCheckIfItemsTheSame(oldItem: StickyEntity<Entity>, newItem: StickyEntity<Entity>): Boolean {
        return false
    }

    open fun customCheckIfContentsTheSame(oldItem: StickyEntity<Entity>, newItem: StickyEntity<Entity>): Boolean {
        return false
    }
}