package by.esas.tools.screens.recycler.sticky_case.sticky_adapter

import android.view.ViewGroup
import by.esas.tools.recycler.sticky.CompareData
import by.esas.tools.recycler.sticky.StickyEntity
import by.esas.tools.recycler.sticky.StickyRecyclerAdapter
import by.esas.tools.screens.recycler.RecyclerEntity

class CaseStickyAdapter(
    onClick: (StickyEntity<RecyclerEntity>) -> Unit,
    onClickPosition: (Int, StickyEntity<RecyclerEntity>) -> Unit,
    onLongClick: (StickyEntity<RecyclerEntity>) -> Unit,
    onLongClickPosition: (Int, StickyEntity<RecyclerEntity>) -> Unit
) : StickyRecyclerAdapter<RecyclerEntity, CaseStickyItemVM>(
    onItemClick = onClick,
    onItemClickPosition = onClickPosition,
    onItemLongClick = onLongClick,
    onItemLongClickPosition = onLongClickPosition
) {
    /*
    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun setEntities(items: List<FirstEntity>) {
        if (semaphore.tryAcquire(timeOut, TimeUnit.MILLISECONDS)) {
            adapterScope.launch {
                val addList: MutableList<StickyEntity<FirstEntity>> = analyzeItems(items, true)
                val diffCallback = StickyDiffCallback(itemList, addList)
                val difResult = DiffUtil.calculateDiff(diffCallback)

                withContext(Dispatchers.Main) {
                    itemList.clear()
                    itemList.addAll(addList)
                    difResult.dispatchUpdatesTo(this@CaseStickyAdapter)
                    semaphore.release()
                }
            }
        }
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : CaseStickyViewHolder<*> {
        return when (viewType) {
            0 -> {
                CaseStickyViewHolder.createItem(parent, CaseStickyItemVM())
            }
            1 -> {
                CaseStickyViewHolder.createTitle(parent, CaseStickyItemVM())
            }
            else -> {
                CaseStickyViewHolder.createItem(parent, CaseStickyItemVM())
            }
        }
    }

    override fun createNew(entity: RecyclerEntity?): CompareData {
        return if (entity != null) {
            val letter = if (entity.name.isNotEmpty()) {
                entity.name.uppercase().substring(0, 1)
            } else {
                "-"
            }
            CaseCompareData(letter)
        } else {
            CaseCompareData("-")
        }
    }
}