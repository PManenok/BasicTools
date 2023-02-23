package by.esas.tools.screens.recycler.sticky_case.sticky_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import by.esas.tools.BR
import by.esas.tools.R
import by.esas.tools.databinding.IStickyRecyclerHeaderBinding
import by.esas.tools.recycler.sticky.RecyclerStickyHeaderView
import by.esas.tools.recycler.sticky.StickyEntity
import by.esas.tools.screens.recycler.RecyclerEntity

class CaseStickyHeader(
    binding: IStickyRecyclerHeaderBinding, viewModel: CaseStickyItemVM,
    listener: IStickyHeader<StickyEntity<RecyclerEntity>>
) : RecyclerStickyHeaderView<StickyEntity<RecyclerEntity>, IStickyRecyclerHeaderBinding, CaseStickyItemVM>
    (binding, viewModel, listener) {

    override fun provideHeaderLayout(): Int = R.layout.i_sticky_recycler_header

    companion object {

        fun create(
            parent: ViewGroup,
            viewModel: CaseStickyItemVM,
            listener: IStickyHeader<StickyEntity<RecyclerEntity>>
        ): RecyclerStickyHeaderView<StickyEntity<RecyclerEntity>, IStickyRecyclerHeaderBinding, CaseStickyItemVM> {
            val binding = IStickyRecyclerHeaderBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return CaseStickyHeader(
                binding,
                viewModel,
                listener
            )
        }
    }

    override fun provideVariable(): Int = BR.item
}
