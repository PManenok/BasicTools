/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<Entity, VM : BaseItemViewModel<Entity>>(
    val itemList: MutableList<Entity> = mutableListOf(),
    val onItemClick: (Entity) -> Unit = {},
    val onItemClickPosition: (Int, Entity) -> Unit = { _, _ -> },
    val onItemLongClick: (Entity) -> Unit = {},
    val onItemLongClickPosition: (Int, Entity) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<BaseViewHolder<Entity, VM, *>>() {

    /*Override methods*/
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder<Entity, VM, *>, position: Int) {
        doOnBindViewHolder(holder, position)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder<Entity, VM, *>) {
        super.onViewAttachedToWindow(holder)
        doOnViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<Entity, VM, *>) {
        super.onViewDetachedFromWindow(holder)
        doOnViewDetachedFromWindow(holder)
    }

    /*Public methods*/
    open fun addItems(items: List<Entity>) {
        val startPos = itemList.size
        itemList.addAll(items)
        notifyItemRangeInserted(startPos, items.size)
    }

    open fun addItem(item: Entity) {
        val startPos = itemList.size
        itemList.add(item)
        notifyItemRangeInserted(startPos, 1)
    }

    /**
     * Should be used for cases where we need to update previous itemList content
     */
    open fun setItems(items: List<Entity>) {
        val diffCallback = BaseDiffCallback(itemList, items)
        val difResult = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(items)
        difResult.dispatchUpdatesTo(this)
    }

    open fun cleanItems() {
        itemList.clear()
        notifyDataSetChanged()
    }

    /*Protected methods*/
    protected open fun doOnBindViewHolder(holder: BaseViewHolder<Entity, VM, *>, position: Int) {
        holder.bind(itemList[position], position)
    }

    protected open fun doOnViewAttachedToWindow(holder: BaseViewHolder<Entity, VM, *>) {
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            //clickItemSubject.onNext(ItemClick(itemList[pos], pos))
            holder.viewModel.onItemClick()
            onItemClick(itemList[pos])
            onItemClickPosition(pos, itemList[pos])
        }
        holder.itemView.setOnLongClickListener {
            val pos = holder.adapterPosition
            //clickItemSubject.onNext(ItemClick(itemList[pos], pos))
            onItemLongClick(itemList[pos])
            onItemLongClickPosition(pos, itemList[pos])
            return@setOnLongClickListener true
        }
    }

    protected open fun doOnViewDetachedFromWindow(holder: BaseViewHolder<Entity, VM, *>) {
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
    }
}