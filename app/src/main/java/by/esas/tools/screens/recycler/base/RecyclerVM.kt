package by.esas.tools.screens.recycler.base

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import by.esas.tools.App
import by.esas.tools.base.AppVM
import by.esas.tools.databinding.IRecyclerCaseTwoBinding
import by.esas.tools.recycler.simpleItemAdapter.SimpleItemAdapter
import by.esas.tools.screens.recycler.FirstEntity
import by.esas.tools.screens.recycler.base.adapter.FirstAdapter
import javax.inject.Inject

class RecyclerVM @Inject constructor() : AppVM() {

    val newEntity: MutableLiveData<String> = MutableLiveData("")
    val withPositionFlag: MutableLiveData<Boolean> = MutableLiveData(false)
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

    val simpleAdapter: SimpleItemAdapter = SimpleItemAdapter(
        onItemClick = { item ->
            Toast.makeText(App.instance, "simpleAdapter: Click on item ${item.name}", Toast.LENGTH_SHORT).show()
        },
        onItemClickPosition = { pos, item ->
            Toast.makeText(
                App.instance,
                "simpleAdapter: Click on item ${item.name} with position $pos",
                Toast.LENGTH_SHORT
            ).show()
        },
        onItemLongClick = { item ->
            Toast.makeText(App.instance, "simpleAdapter: Long click on item ${item.name}", Toast.LENGTH_SHORT).show()
        },
        onItemLongClickPosition = { pos, item ->
            Toast.makeText(
                App.instance,
                "simpleAdapter: Long click on item ${item.name} with position $pos",
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
        adapter.addItem(FirstEntity(newEntity.value ?: "Empty"))
    }

    fun clearItems() {
        adapter.cleanItems()
    }

    fun setAnotherList() {
        adapter.addItems(
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