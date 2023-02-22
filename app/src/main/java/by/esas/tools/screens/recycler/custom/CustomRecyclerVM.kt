package by.esas.tools.screens.recycler.custom

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.base.AppVM
import by.esas.tools.databinding.ICustomBinding
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemAdapter
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemModel
import by.esas.tools.screens.recycler.RecyclerLists
import by.esas.tools.screens.recycler.mapToSimple
import javax.inject.Inject

class CustomRecyclerVM @Inject constructor() : AppVM() {

    val newEntity: MutableLiveData<String> = MutableLiveData("")
    val withPositionFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    val makeItemsSelectable: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSingleItemSelectable: MutableLiveData<Boolean> = MutableLiveData(true)
    var currentListIsFirst: Boolean = true
    var currentAlignment: Int = View.TEXT_ALIGNMENT_CENTER

    val adapter: SimpleItemAdapter = SimpleItemAdapter.createCustom(
        inflater = ICustomBinding::class.java,
        onItemClick = { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "simpleAdapter: Click on item ${item.name}", Toast.LENGTH_SHORT).show()
        },
        onItemClickPosition = { pos, item ->
            doOnClickWithPosition(pos, item)
        },
        onItemLongClick = { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "simpleAdapter: Long click on item ${item.name}", Toast.LENGTH_SHORT)
                    .show()
        },
        onItemLongClickPosition = { pos, item ->
            if (withPositionFlag.value == true)
                Toast.makeText(
                    App.instance,
                    "simpleAdapter: Long click on item ${item.name} with position $pos",
                    Toast.LENGTH_SHORT
                ).show()
        }
    )

    private fun doOnClickWithPosition(pos: Int, item: SimpleItemModel) {
        if (withPositionFlag.value == true)
            Toast.makeText(
                App.instance,
                "simpleAdapter: Click on item ${item.name} with position $pos",
                Toast.LENGTH_SHORT
            ).show()
        if (makeItemsSelectable.value == true) {
            if (item.isChoosed) adapter.setItemUnpicked(pos)
            else adapter.setItemPicked(pos, isSingleItemSelectable.value == true)
        }
    }

    fun addItem() {
        val name = newEntity.value ?: "Empty"
        adapter.itemList.get(adapter.itemList.lastIndex).isLast = false
        adapter.addItem(
            SimpleItemModel(
                name = name,
                code = name,
                isChoosed = false,
                isLast = true,
                textAlignment = currentAlignment
            )
        )
    }

    fun clearItems() {
        adapter.cleanItems()
    }

    fun setAnotherList() {
        adapter.setItems(
            if (currentListIsFirst) {
                currentListIsFirst = false
                val lastIndex = RecyclerLists.secondList.lastIndex
                RecyclerLists.secondList.mapIndexed { index, entity ->
                    entity.mapToSimple(lastIndex == index, currentAlignment)
                }
            } else {
                currentListIsFirst = true
                val lastIndex = RecyclerLists.firstList.lastIndex
                RecyclerLists.firstList.mapIndexed { index, entity ->
                    entity.mapToSimple(lastIndex == index, currentAlignment)
                }
            }
        )
    }
}