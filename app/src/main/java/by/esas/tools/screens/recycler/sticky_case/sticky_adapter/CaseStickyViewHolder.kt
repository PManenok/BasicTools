package by.esas.tools.screens.recycler.sticky_case.sticky_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import by.esas.tools.BR
import by.esas.tools.databinding.IStickyRecyclerCaseBinding
import by.esas.tools.databinding.IStickyRecyclerHeaderBinding
import by.esas.tools.recycler.BaseViewHolder
import by.esas.tools.recycler.sticky.StickyEntity
import by.esas.tools.screens.recycler.RecyclerEntity

class CaseStickyViewHolder<B : ViewDataBinding>(binding: B, viewModel: CaseStickyItemVM) :
    BaseViewHolder<StickyEntity<RecyclerEntity>, CaseStickyItemVM, B>
        (binding, viewModel) {
    companion object {
        fun createItem(
            parent: ViewGroup,
            viewModel: CaseStickyItemVM
        ): CaseStickyViewHolder<IStickyRecyclerCaseBinding> {
            val binding =
                IStickyRecyclerCaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CaseStickyViewHolder(
                binding,
                viewModel
            )
        }

        fun createTitle(
            parent: ViewGroup,
            viewModel: CaseStickyItemVM
        ): CaseStickyViewHolder<IStickyRecyclerHeaderBinding> {
            val binding =
                IStickyRecyclerHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return CaseStickyViewHolder(
                binding,
                viewModel
            )
        }
    }

    override fun provideVariable(): Int = BR.item
}