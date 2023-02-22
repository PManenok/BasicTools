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
        return SimpleItemViewHolder.create(parent, SimpleItemViewModel())
    }

    /**
     * Sets item as selected for concrete index. Can remove other items selection,
     * or change selection of only one item.
     */
    open fun setItemPicked(pickedIndex: Int, clearOtherPicked: Boolean = true) {
        val newList = itemList.mapIndexed { index, simpleItemModel ->
            if (clearOtherPicked)
                simpleItemModel.copy(isChoosed = index == pickedIndex)
            else if (index == pickedIndex)
                simpleItemModel.copy(isChoosed = true)
            else
                simpleItemModel.copy()
        }
        setItems(newList)
    }

    /**
     * Sets item unselected
     */
    open fun setItemUnpicked(pickedIndex: Int) {
        val newList = itemList.mapIndexed { index, simpleItemModel ->
            if (index == pickedIndex)
                simpleItemModel.copy(isChoosed = false)
            else
                simpleItemModel.copy()
        }
        setItems(newList)
    }

    /**
     * Adds item in the end of the list and change its parameter as is last,
     * and fix same parameter for previous last item
     */
    override fun addItem(item: SimpleItemModel) {
        val startPos = itemList.size
        val lastInd = itemList.lastIndex
        if (lastInd > -1) {
            itemList.lastOrNull()?.isLast = false
            notifyItemChanged(lastInd)
        }
        item.isLast = true
        itemList.add(item)
        notifyItemRangeInserted(startPos, 1)
    }

    override fun addItems(items: List<SimpleItemModel>) {
        var startPos = itemList.size
        val lastInd = itemList.lastIndex
        if (lastInd > -1) {
            itemList.lastOrNull()?.isLast = false
            startPos = lastInd
            notifyItemChanged(lastInd)
        }
        items.lastOrNull()?.isLast = true
        itemList.addAll(items)
        notifyItemRangeInserted(startPos, items.size)
    }

    override fun setItems(items: List<SimpleItemModel>) {
        val lastInd = items.lastIndex
        super.setItems(items.mapIndexed { index, simpleItemModel ->
            simpleItemModel.copy(isLast = index == lastInd)
        })
    }
}