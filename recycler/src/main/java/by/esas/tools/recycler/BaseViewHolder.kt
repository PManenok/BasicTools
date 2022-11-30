/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<Entity, VM : BaseItemViewModel<Entity>, Binding : ViewDataBinding>
    (val binding: Binding, val viewModel: VM) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.setVariable(this.provideVariable(), viewModel)
    }

    open fun bind(item: Entity, position: Int) {
        viewModel.bindItem(item, position)
        binding.executePendingBindings()
    }

    abstract fun provideVariable(): Int /*{
        BR.item
    }*/
}