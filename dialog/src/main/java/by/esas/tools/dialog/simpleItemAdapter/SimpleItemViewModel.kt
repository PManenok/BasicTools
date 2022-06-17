package by.esas.tools.dialog.simpleItemAdapter

import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import by.esas.tools.recycler.BaseItemViewModel

class SimpleItemViewModel : BaseItemViewModel<SimpleItemModel>() {
    val isChoosed = ObservableBoolean(false)
    val name = ObservableField<String>("")
    val isLast = ObservableBoolean()
    val alignment = ObservableInt(View.TEXT_ALIGNMENT_TEXT_START)

    override fun bindItem(item: SimpleItemModel, position: Int) {
        this.position.set(position)
        isChoosed.set(item.isChoosed)
        name.set(item.name)
        isLast.set(item.isLast)
        alignment.set(item.textAlignment)
    }
}