/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.inputfieldview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.textview.MaterialTextView


abstract class CustomSpinnerAdapter<T>(
    context: Context
) : ArrayAdapter<String>(context, R.layout.i_field_spinner_drop_down) {
    private val entities: MutableList<EntityInterface<T>> = mutableListOf()

    fun addAll(entities: List<T>) {
        val list = convertEntitiesToInterface(entities)
        this.entities.addAll(list)
        this.addAll(list.map { it.getName() })
        this.notifyDataSetChanged()
    }

    fun renewAdapterData() {
        this.clear()
        this.addAll(entities.map { it.getName() })
        this.notifyDataSetChanged()
    }

    abstract fun convertEntitiesToInterface(entities: List<T>): List<EntityInterface<T>>

    fun getEntity(position: Int): T {
        return entities[position].getEntity()
    }

    fun getPosition(checkAction: (item: T) -> Boolean): Int {
        entities.forEachIndexed { index, item ->
            if (checkAction(item.getEntity())) return index
        }
        return -1
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getListedView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getListedView(position, parent)
    }

    private fun getListedView(position: Int, parent: ViewGroup): View {
        val inflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val name = inflater.inflate(R.layout.i_field_spinner_drop_down, parent, false) as MaterialTextView

        val entity = entities[position]
        name.text = entity.getName()

        return name
    }

    interface EntityInterface<Entity> {
        fun getName(): String
        fun getEntity(): Entity
    }
}