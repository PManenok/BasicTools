package by.esas.tools.screens.recycler.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import by.esas.tools.BR
import by.esas.tools.databinding.IRecyclerBaseBinding
import by.esas.tools.recycler.BaseViewHolder
import by.esas.tools.screens.recycler.RecyclerEntity

class FirstViewHolder(binding: IRecyclerBaseBinding, viewModel: FirstItemVM) :
    BaseViewHolder<RecyclerEntity, FirstItemVM, IRecyclerBaseBinding>(binding, viewModel) {

    companion object {

        fun create(
            parent: ViewGroup,
            viewModel: FirstItemVM
        ): FirstViewHolder {
            val binding = IRecyclerBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FirstViewHolder(binding, viewModel)
        }
    }

    override fun provideVariable(): Int = BR.item
}