/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler.sticky

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import by.esas.tools.recycler.BaseItemViewModel

abstract class RecyclerStickyHeaderView<Entity, B : ViewDataBinding, VM : BaseItemViewModel<Entity>>
    (val binding: B, val viewModel: VM, protected val listener: IStickyHeader<Entity>) : RecyclerView.ItemDecoration() {

    protected var mStickyHeaderHeight = 0

    abstract fun provideHeaderLayout(): Int

    init {
        binding.setVariable(this.provideVariable(), viewModel)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        doOnDrawOver(canvas, parent, state)
    }

    open fun bind(item: Entity, position: Int) {
        viewModel.bindItem(item, position)
        binding.executePendingBindings()
    }

    protected open fun doOnDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        //TODO make first header not drawable when it is in same position as header on recycler

        val topChild: View = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        val headerPos = listener.getHeaderPositionForItem(topChildPosition)

        fixLayoutSize(parent, binding.root)
        val contactPoint = binding.root.bottom
        val childInContact = getChildInContact(parent, contactPoint, headerPos)

        val topContactPoint = binding.root.top
        val topChildInContact = getChildInContact(parent, topContactPoint, headerPos)
        if (topChildInContact != null && listener.isHeader(parent.getChildAdapterPosition(topChildInContact))) {
            if (topChildInContact.top.toFloat() == 0.0f) {
                return
            }
        }
        if (childInContact != null && listener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(canvas, binding.root, childInContact)
            return
        }
        bind(listener.getHeaderForCurrentPosition(headerPos), headerPos)
        drawHeader(canvas, binding.root)
    }

    protected open fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    protected open fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top.toFloat() - currentHeader.height)
        currentHeader.draw(c)
        c.restore()
    }

    protected open fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View {
        val layoutResId: Int = provideHeaderLayout()
        val header = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        bind(listener.getHeaderForCurrentPosition(headerPosition), headerPosition)
        return header
    }

    protected open fun getChildInContact(parent: RecyclerView, contactPoint: Int, currentHeaderPos: Int): View? {
        var childInContact: View? = null
        var i: Int = 0
        while (i < parent.childCount) {
            var heightTolerance: Int = 0
            val child = parent.getChildAt(i)

            //measure height tolerance with child if child is another header
            if (currentHeaderPos != i) {
                val isChildHeader = listener.isHeader(parent.getChildAdapterPosition(child))
                if (isChildHeader) {
                    heightTolerance = mStickyHeaderHeight - child.height
                }
            }

            //add heightTolerance if child top be in display area
            val childBottomPosition: Int = if (child.top > 0) child.bottom + heightTolerance else child.bottom

            if (childBottomPosition > contactPoint) {
                if (child.top <= contactPoint) {
                    // This child overlaps the contactPoint
                    childInContact = child
                    break
                }
            }
            i++
        }
        return childInContact
    }

    protected open fun fixLayoutSize(parent: ViewGroup, view: View) {
        // Specs for parent (RecyclerView)
        val widthSpec: Int = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec: Int = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec: Int =
            ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec: Int =
            ViewGroup.getChildMeasureSpec(
                heightSpec,
                parent.paddingTop + parent.paddingBottom,
                view.layoutParams.height
            )

        view.measure(childWidthSpec, childHeightSpec)
        mStickyHeaderHeight = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, mStickyHeaderHeight)
    }

    abstract fun provideVariable(): Int /*{
        BR.item
    }*/

    interface IStickyHeader<Entity> {

        fun getHeaderPositionForItem(itemPosition: Int): Int

        fun getHeaderForCurrentPosition(position: Int): Entity

        fun isHeader(itemPosition: Int): Boolean
    }
}
