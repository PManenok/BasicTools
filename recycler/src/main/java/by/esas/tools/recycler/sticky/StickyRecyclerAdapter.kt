package by.esas.tools.recycler.sticky

import by.esas.tools.recycler.BaseItemViewModel
import by.esas.tools.recycler.BaseRecyclerAdapter
import by.esas.tools.recycler.BaseViewHolder
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

abstract class StickyRecyclerAdapter<Entity, VM : BaseItemViewModel<StickyEntity<Entity>>>(
    itemList: MutableList<StickyEntity<Entity>> = mutableListOf(),
    onItemClick: (StickyEntity<Entity>) -> Unit = {},
    onItemClickPosition: (Int, StickyEntity<Entity>) -> Unit = { _, _ -> },
    onItemLongClick: (StickyEntity<Entity>) -> Unit = {},
    onItemLongClickPosition: (Int, StickyEntity<Entity>) -> Unit = { _, _ -> }
) : BaseRecyclerAdapter<StickyEntity<Entity>, VM>(itemList, onItemClick, onItemClickPosition, onItemLongClick, onItemLongClickPosition) {
    var stickyHeaderDecoration: RecyclerStickyHeaderView<StickyEntity<Entity>, *, VM>? = null

    protected open var timeOut: Long = 1000

    //private val adapterScope = CoroutineScope(Dispatchers.Default)
    protected val entityList: MutableList<Entity> = mutableListOf()
    protected val semaphore: Semaphore = Semaphore(1)

    fun getEntitiesCount(): Int {
        return entityList.size
    }

    override fun addItems(items: List<StickyEntity<Entity>>) {
        if (semaphore.tryAcquire(timeOut, TimeUnit.MILLISECONDS)) {
            itemList.addAll(items)
            notifyDataSetChanged()
            semaphore.release()
        }
    }

    /*override fun addEntities(items: List<StickyEntity<Type>>) {
        if (semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            adapterScope.launch {
                val historyList: MutableList<InvoiceListItem> = analyzeItems(items)

                withContext(Dispatchers.Main) {
                    itemList.addAll(historyList)
                    notifyDataSetChanged()
                    semaphore.release()
                }
            }
        }
    }*/

    open fun addEntities(items: List<Entity>) {
        if (semaphore.tryAcquire(timeOut, TimeUnit.MILLISECONDS)) {
            val addList: MutableList<StickyEntity<Entity>> = analyzeItems(items)
            itemList.addAll(addList)
            notifyDataSetChanged()
            semaphore.release()
        }
    }

    override fun addItem(item: StickyEntity<Entity>) {
        this.addItems(listOf(item))
    }

    open fun addEntity(item: Entity) {
        this.addEntities(listOf(item))
    }

    open fun analyzeItems(items: List<Entity>): MutableList<StickyEntity<Entity>> {
        val addList: MutableList<StickyEntity<Entity>> = mutableListOf()
        var compareData = createNew(this.entityList.lastOrNull())

        items.forEach { newAdapterItem ->
            newAdapterItem.let { item ->
                val itemData = createNew(item)
                if (!compareData.compareTo(itemData)) {
                    addList.add(StickyEntity(header = itemData.createHeader()))
                    compareData = itemData
                }
                addList.add(StickyEntity(entity = item))
                this.entityList.add(item)
            }
        }
        return addList
    }

    abstract fun createNew(entity: Entity?): CompareData

    override fun cleanItems() {
        if (semaphore.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
            entityList.clear()
            super.cleanItems()
            semaphore.release()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].header != null) {
            1
        } else {
            0
        }
    }


    override fun onViewAttachedToWindow(holder: BaseViewHolder<StickyEntity<Entity>, VM, *>) {
        val pos = holder.adapterPosition
        if (itemList[pos].header == null) {
            super.onViewAttachedToWindow(holder)
        }
    }

    val listener = object :
        RecyclerStickyHeaderView.IStickyHeader<StickyEntity<Entity>> {
        override fun getHeaderPositionForItem(itemPosition: Int): Int {
            for (position in (0..itemPosition).sortedDescending()) {
                if (this.isHeader(position)) {
                    return position
                }
            }
            return 0
        }

        override fun isHeader(itemPosition: Int): Boolean {
            return itemList.get(itemPosition).header != null
        }

        override fun getHeaderForCurrentPosition(position: Int): StickyEntity<Entity> {
            return itemList.get(position)
        }
    }
}