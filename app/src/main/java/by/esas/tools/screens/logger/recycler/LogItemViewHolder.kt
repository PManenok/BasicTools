package by.esas.tools.screens.logger.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import by.esas.tools.BR
import by.esas.tools.databinding.ILogItemBinding
import by.esas.tools.entity.LogItem
import by.esas.tools.recycler.BaseViewHolder

class LogItemViewHolder(binding: ILogItemBinding, viewModel: LogItemViewModel) :
    BaseViewHolder<LogItem, LogItemViewModel, ILogItemBinding>(binding, viewModel) {

    companion object {

        fun create(
            parent: ViewGroup,
            viewModel: LogItemViewModel
        ): LogItemViewHolder {
            val binding = ILogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LogItemViewHolder(
                binding, viewModel
            )
        }
    }

    override fun provideVariable() = BR.item
}