package by.esas.tools.screens.logger.recycler

import android.view.ViewGroup
import by.esas.tools.entity.LogItem
import by.esas.tools.recycler.BaseRecyclerAdapter
import by.esas.tools.recycler.BaseViewHolder

class LogItemAdapter: BaseRecyclerAdapter<LogItem, LogItemViewModel>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<LogItem, LogItemViewModel, *> {
        return LogItemViewHolder.create(parent, LogItemViewModel())
    }
}