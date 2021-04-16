/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler

import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<Entity, VM : BaseItemViewModel<Entity>>(
    val itemList: MutableList<Entity> = mutableListOf(),
    val onItemClick: (Entity) -> Unit = {},
    val onItemClickPosition: (Int, Entity) -> Unit = { _, _ -> },
    val onItemLongClick: (Entity) -> Unit = {},
    val onItemLongClickPosition: (Int, Entity) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<BaseViewHolder<Entity, VM, *>>() {
    //protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    //val clickItemSubject = PublishSubject.create<ItemClick<Entity>>()

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder<Entity, VM, *>, position: Int) {
        holder.bind(itemList[position], position)
        //addToDisposable(holder.viewModel.disposable)
    }

    open fun addItems(items: List<Entity>) {
        val startPos = itemList.size
        itemList.addAll(items)
        notifyItemRangeChanged(startPos, items.size)
    }

    open fun addItem(item: Entity) {
        val startPos = itemList.size
        itemList.add(item)
        notifyItemRangeChanged(startPos, 1)
    }

    open fun cleanItems() {
        itemList.clear()
        notifyDataSetChanged()
    }

    /* protected fun addToDisposable(disposable: Disposable?) {
         if (disposable != null)
             compositeDisposable.add(disposable)
     }

     fun clearDisposables() {
         compositeDisposable.clear()
     }*/

    override fun onViewAttachedToWindow(holder: BaseViewHolder<Entity, VM, *>) {
        super.onViewAttachedToWindow(holder)
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

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<Entity, VM, *>) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
        holder.itemView.setOnLongClickListener(null)
    }
}