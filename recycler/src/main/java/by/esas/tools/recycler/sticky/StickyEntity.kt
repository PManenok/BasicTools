/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler.sticky

import by.esas.tools.recycler.ICheckDiff

open class StickyEntity<Entity>(
    val header: StickyHeader? = null,
    val entity: Entity? = null
) : ICheckDiff<StickyEntity<Entity>> {

    override fun checkIfSameItem(item: StickyEntity<Entity>): Boolean {
        return if (header != null && item.header != null) {
            header.headerText == item.header.headerText
        } else if (entity != null && item.entity != null) {
            if (entity is ICheckDiff<*>) {
                (entity as ICheckDiff<Entity>).checkIfSameItem(item.entity)
            } else {
                false
            }
        } else {
            false
        }
    }

    override fun checkIfSameContent(item: StickyEntity<Entity>): Boolean {
        return if (header != null && item.header != null) {
            header.headerText == item.header.headerText
        } else if (entity != null && item.entity != null) {
            if (entity is ICheckDiff<*>) {
                (entity as ICheckDiff<Entity>).checkIfSameContent(item.entity)
            } else {
                false
            }
        } else {
            false
        }
    }
}