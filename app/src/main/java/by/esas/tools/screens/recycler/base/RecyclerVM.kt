package by.esas.tools.screens.recycler.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.base.AppVM
import by.esas.tools.databinding.IRecyclerCaseTwoBinding
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemAdapter
import by.esas.tools.screens.recycler.RecyclerEntity
import by.esas.tools.screens.recycler.RecyclerLists
import by.esas.tools.screens.recycler.base.adapter.FirstAdapter
import javax.inject.Inject

class RecyclerVM @Inject constructor() : AppVM() {

    val newEntity: MutableLiveData<String> = MutableLiveData("")
    val withPositionFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    var currentListIsFirst: Boolean = true

    val adapter: FirstAdapter = FirstAdapter(
        { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "adapter: Click on item ${item.name}", Toast.LENGTH_SHORT).show()
        }, { pos, item ->
            if (withPositionFlag.value == true)
                Toast.makeText(
                    App.instance,
                    "adapter: Click on item ${item.name} with position $pos",
                    Toast.LENGTH_SHORT
                )
                    .show()
        }, { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "adapter: Long click on item ${item.name}", Toast.LENGTH_SHORT).show()
        }, { pos, item ->
            if (withPositionFlag.value == true)
                Toast.makeText(
                    App.instance,
                    "adapter: Long click on item ${item.name} with position $pos",
                    Toast.LENGTH_SHORT
                ).show()
        }
    )

    val customAdapter: SimpleItemAdapter = SimpleItemAdapter.createCustom(
        inflater = IRecyclerCaseTwoBinding::class.java,
        onItemClick = { item ->
            Toast.makeText(App.instance, "customAdapter: Click on item ${item.name}", Toast.LENGTH_SHORT).show()
        },
        onItemClickPosition = { pos, item ->
            Toast.makeText(
                App.instance,
                "customAdapter: Click on item ${item.name} with position $pos",
                Toast.LENGTH_SHORT
            ).show()
        },
        onItemLongClick = { item ->
            Toast.makeText(App.instance, "customAdapter: Long click on item ${item.name}", Toast.LENGTH_SHORT).show()
        },
        onItemLongClickPosition = { pos, item ->
            Toast.makeText(
                App.instance,
                "customAdapter: Long click on item ${item.name} with position $pos",
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    fun addItem() {
        adapter.addItem(RecyclerEntity(newEntity.value ?: "Empty"))
    }

    fun clearItems() {
        adapter.cleanItems()
    }

    fun setAnotherList() {
        adapter.setItems(
            if (currentListIsFirst) {
                currentListIsFirst = false
                RecyclerLists.secondList
            } else {
                currentListIsFirst = true
                RecyclerLists.firstList
            }
        )
    }
}