package by.esas.tools.screens.recycler.sticky_case

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.base.AppVM
import by.esas.tools.recycler.sticky.StickyEntity
import by.esas.tools.screens.recycler.FirstEntity
import by.esas.tools.screens.recycler.sticky_case.sticky_adapter.CaseStickyAdapter
import javax.inject.Inject

class StickyCaseVM @Inject constructor() : AppVM() {

    val newEntity: MutableLiveData<String> = MutableLiveData("")
    var currentListIsFirst: Boolean = true
    val firstList: List<FirstEntity> = listOf(
        FirstEntity("Alpha"),
        FirstEntity("Beta"),
        FirstEntity("Gamma"),
        FirstEntity("Delta"),
        FirstEntity("Epsilon"),
        FirstEntity("Zeta"),
        FirstEntity("Eta"),
        FirstEntity("Theta"),
        FirstEntity("Iota"),
        FirstEntity("Cappa"),
        FirstEntity("Lambda"),
        FirstEntity("Mi"),
        FirstEntity("Ni"),
        FirstEntity("Csi"),
        FirstEntity("Omicron"),
        FirstEntity("Pi"),
        FirstEntity("Rho"),
        FirstEntity("Sigma"),
        FirstEntity("Tau"),
        FirstEntity("Ipsilon"),
        FirstEntity("Phi"),
        FirstEntity("Chi"),
        FirstEntity("Psi"),
        FirstEntity("Omega")
    )
    val secondList: List<FirstEntity> = listOf(
        FirstEntity("One"),
        FirstEntity("Two"),
        FirstEntity("Three"),
        FirstEntity("Four"),
        FirstEntity("Five"),
        FirstEntity("Six"),
        FirstEntity("Seven"),
        FirstEntity("Eight"),
        FirstEntity("Nine"),
        FirstEntity("Ten"),
        FirstEntity("Eleven"),
        FirstEntity("Twelve")
    )

    val adapter: CaseStickyAdapter = CaseStickyAdapter(
        { item ->

            Toast.makeText(App.instance, "stickyAdapter: Click on ${takeTextForSticky(item)}", Toast.LENGTH_SHORT)
                .show()
        }, { pos, item ->
            Toast.makeText(
                App.instance,
                "stickyAdapter: Click on ${takeTextForSticky(item)} with position $pos",
                Toast.LENGTH_SHORT
            ).show()
        }, { item ->
            Toast.makeText(App.instance, "adapter: Long click on ${takeTextForSticky(item)}", Toast.LENGTH_SHORT).show()
        }, { pos, item ->
            Toast.makeText(
                App.instance,
                "adapter: Long click on ${takeTextForSticky(item)} with position $pos",
                Toast.LENGTH_SHORT
            ).show()
        }
    )

    private fun takeTextForSticky(item: StickyEntity<FirstEntity>): String {
        return if (item.header != null) {
            "header ${item.header?.headerText}"
        } else {
            "item ${item.entity?.name}"
        }
    }

    fun addItem() {
        adapter.addEntity(FirstEntity(newEntity.value ?: "Empty"))
    }

    fun clearItems() {
        adapter.cleanItems()
    }

    fun setAnotherList() {
        adapter.setEntities(
            if (currentListIsFirst) {
                currentListIsFirst = false
                secondList
            } else {
                currentListIsFirst = true
                firstList
            }
        )
    }
}