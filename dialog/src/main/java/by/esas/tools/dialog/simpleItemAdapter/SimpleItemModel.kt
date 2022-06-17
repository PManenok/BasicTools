package by.esas.tools.dialog.simpleItemAdapter

import android.view.View

class SimpleItemModel(
    val shortName: String,
    val name: String,
    val isChoosed: Boolean,
    val isLast: Boolean,
    var textAlignment: Int = View.TEXT_ALIGNMENT_TEXT_START
)