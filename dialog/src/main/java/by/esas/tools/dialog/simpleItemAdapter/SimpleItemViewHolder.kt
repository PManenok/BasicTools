package by.esas.tools.dialog.simpleItemAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import by.esas.tools.dialog.databinding.IDialogMessageBinding
import by.esas.tools.dialog.databinding.IItemBinding
import by.esas.tools.recycler.BaseViewHolder
import by.esas.eposmobile.BR

class SimpleItemViewHolder<Binding : ViewDataBinding>(binding: Binding, viewModel: SimpleItemViewModel) :
    BaseViewHolder<SimpleItemModel, SimpleItemViewModel, Binding>(binding, viewModel) {
    companion object {
        fun create(parent: ViewGroup, viewModel: SimpleItemViewModel): SimpleItemViewHolder<IItemBinding> {
            val binding = IItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SimpleItemViewHolder(
                binding,
                viewModel
            )
        }

        fun createForMessage(
            parent: ViewGroup,
            viewModel: SimpleItemViewModel
        ): SimpleItemViewHolder<IDialogMessageBinding> {
            val binding = IDialogMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SimpleItemViewHolder(
                binding,
                viewModel
            )
        }
    }

    override fun provideVariable(): Int = BR.item
}