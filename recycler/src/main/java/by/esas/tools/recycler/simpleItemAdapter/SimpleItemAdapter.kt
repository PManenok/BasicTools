/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler.simpleItemAdapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import by.esas.tools.recycler.BaseRecyclerAdapter

open class SimpleItemAdapter(
    itemList: MutableList<SimpleItemModel> = mutableListOf(),
    onItemClick: (SimpleItemModel) -> Unit = {},
    onItemClickPosition: (Int, SimpleItemModel) -> Unit = { _, _ -> },
    onItemLongClick: (SimpleItemModel) -> Unit = {},
    onItemLongClickPosition: (Int, SimpleItemModel) -> Unit = { _, _ -> }
) : BaseRecyclerAdapter<SimpleItemModel, SimpleItemViewModel>(
    itemList = itemList,
    onItemClick = onItemClick,
    onItemClickPosition = onItemClickPosition,
    onItemLongClick = onItemLongClick,
    onItemLongClickPosition = onItemLongClickPosition
) {

    companion object {
        fun <T : ViewDataBinding> createCustom(
            inflater: Class<T>,
            itemList: MutableList<SimpleItemModel> = mutableListOf(),
            onItemClick: (SimpleItemModel) -> Unit = {},
            onItemClickPosition: (Int, SimpleItemModel) -> Unit = { _, _ -> },
            onItemLongClick: (SimpleItemModel) -> Unit = {},
            onItemLongClickPosition: (Int, SimpleItemModel) -> Unit = { _, _ -> }
        ): SimpleItemAdapter {
            return object : SimpleItemAdapter(
                itemList,
                onItemClick,
                onItemClickPosition,
                onItemLongClick,
                onItemLongClickPosition
            ) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleItemViewHolder<*> {
                    return SimpleItemViewHolder.createBinding(
                        inflater,
                        parent,
                        SimpleItemViewModel()
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleItemViewHolder<*> {
        return SimpleItemViewHolder.create(
            parent,
            SimpleItemViewModel()
        )
    }
}