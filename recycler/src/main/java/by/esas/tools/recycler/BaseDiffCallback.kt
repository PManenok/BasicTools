/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler

import androidx.recyclerview.widget.DiffUtil

class BaseDiffCallback<Entity>(
    private val oldList: List<Entity>,
    private val newList: List<Entity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: Entity = oldList[oldItemPosition]
        val newItem: Entity = newList[newItemPosition]

        return if (oldItem is ICheckDiff<*>) {
            (oldItem as ICheckDiff<Entity>).checkIfSameItem(newItem)
        } else {
            //You need to override method and add you realisation of the check
            // if your Entity does not implement ICheckDiff interface, otherwise it will always return false
            customCheckIfItemsTheSame(oldItem, newItem)
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is ICheckDiff<*>) {
            (oldItem as ICheckDiff<Entity>).checkIfSameContent(newItem)
        } else {
            //You need to override method and add you realisation of the check
            // if your Entity does not implement ICheckDiff interface, otherwise it will always return false
            customCheckIfContentsTheSame(oldItem, newItem)
        }
    }

    open fun customCheckIfItemsTheSame(oldItem: Entity, newItem: Entity): Boolean {
        return false
    }

    open fun customCheckIfContentsTheSame(oldItem: Entity, newItem: Entity): Boolean {
        return false
    }
}