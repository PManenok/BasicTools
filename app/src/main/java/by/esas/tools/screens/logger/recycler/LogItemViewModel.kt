package by.esas.tools.screens.logger.recycler

import androidx.databinding.ObservableField
import by.esas.tools.entity.LogItem
import by.esas.tools.recycler.BaseItemViewModel

class LogItemViewModel : BaseItemViewModel<LogItem>() {

    val log = ObservableField<String>()

    override fun bindItem(item: LogItem, position: Int) {
        this.position.set(position)
        log.set(setLogText(item.tag, item.category, item.message))
    }

    private fun setLogText(tag: String, category: String, message: String): String {
        val text = StringBuilder()
        if (tag.isNotEmpty()) text.append("Tag: $tag, ")
        if (category.isNotEmpty()) text.append("category: $category, ")
        text.append("message: $message")

        return text.toString()
    }
}