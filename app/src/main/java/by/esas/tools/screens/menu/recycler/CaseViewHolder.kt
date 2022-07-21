package by.esas.tools.screens.menu.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import by.esas.tools.databinding.ICaseBinding
import by.esas.tools.recycler.BaseViewHolder
import by.esas.tools.BR
import by.esas.tools.entity.CaseItemInfo

class CaseViewHolder(binding: ICaseBinding, viewModel: CaseViewModel): BaseViewHolder<CaseItemInfo, CaseViewModel, ICaseBinding>(binding, viewModel) {
    companion object {
        fun create(
            parent: ViewGroup,
            viewModel: CaseViewModel
        ): CaseViewHolder{
            val binding = ICaseBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return CaseViewHolder(
                binding, viewModel
            )
        }
    }

    override fun provideVariable(): Int = BR.item
}