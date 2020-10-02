package by.esas.tools.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<Entity, VM : BaseItemViewModel<Entity>, Binding : ViewDataBinding>
    (val binding: Binding, val viewModel: VM) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.setVariable(this.provideVariable(), viewModel)
    }


    abstract fun provideVariable(): Int /*{
        BR.item
    }*/

    fun bind(item: Entity, position: Int) {
        viewModel.bindItem(item, position)
        binding.executePendingBindings()
    }
}