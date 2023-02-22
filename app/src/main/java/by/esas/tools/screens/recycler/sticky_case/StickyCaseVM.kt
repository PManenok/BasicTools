package by.esas.tools.screens.recycler.sticky_case

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.base.AppVM
import by.esas.tools.recycler.sticky.StickyEntity
import by.esas.tools.screens.recycler.RecyclerEntity
import by.esas.tools.screens.recycler.RecyclerLists
import by.esas.tools.screens.recycler.sticky_case.sticky_adapter.CaseStickyAdapter
import javax.inject.Inject

class StickyCaseVM @Inject constructor() : AppVM() {

    val newEntity: MutableLiveData<String> = MutableLiveData("")
    val withPositionFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    var currentListIsFirst: Boolean = true

    val adapter: CaseStickyAdapter = CaseStickyAdapter(
        { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "stickyAdapter: Click on ${takeTextForSticky(item)}", Toast.LENGTH_SHORT)
                    .show()
        }, { pos, item ->
            if (withPositionFlag.value == true)
                Toast.makeText(
                    App.instance,
                    "stickyAdapter: Click on ${takeTextForSticky(item)} with position $pos",
                    Toast.LENGTH_SHORT
                ).show()
        }, { item ->
            if (withPositionFlag.value == false)
                Toast.makeText(App.instance, "adapter: Long click on ${takeTextForSticky(item)}", Toast.LENGTH_SHORT)
                    .show()
        }, { pos, item ->
            if (withPositionFlag.value == true)
                Toast.makeText(
                    App.instance,
                    "adapter: Long click on ${takeTextForSticky(item)} with position $pos",
                    Toast.LENGTH_SHORT
                ).show()
        }
    )

    private fun takeTextForSticky(item: StickyEntity<RecyclerEntity>): String {
        return if (item.header != null) {
            "header ${item.header?.headerText}"
        } else {
            "item ${item.entity?.name}"
        }
    }

    fun addItem() {
        adapter.addEntity(RecyclerEntity(newEntity.value ?: "Empty"))
    }

    fun clearItems() {
        adapter.cleanItems()
    }

    fun setAnotherList() {
        adapter.setEntities(
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