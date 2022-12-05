/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler.simpleItemAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import by.esas.tools.recycler.BR
import by.esas.tools.recycler.BaseViewHolder
import by.esas.tools.recycler.databinding.IPickedBinding

class SimpleItemViewHolder<Binding : ViewDataBinding>(binding: Binding, viewModel: SimpleItemViewModel) :
    BaseViewHolder<SimpleItemModel, SimpleItemViewModel, Binding>(binding, viewModel) {
    companion object {
        fun create(parent: ViewGroup, viewModel: SimpleItemViewModel): SimpleItemViewHolder<IPickedBinding> {
            val binding = IPickedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SimpleItemViewHolder(
                binding,
                viewModel
            )
        }

        fun <T : ViewDataBinding> createBinding(
            inflater: Class<T>,
            parent: ViewGroup,
            viewModel: SimpleItemViewModel
        ): SimpleItemViewHolder<T> {
            val binding =
                inflater.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
                    .invoke(null, LayoutInflater.from(parent.context), parent, false) as T
            return SimpleItemViewHolder(
                binding,
                viewModel
            )
        }
    }

    override fun provideVariable(): Int = BR.item
}