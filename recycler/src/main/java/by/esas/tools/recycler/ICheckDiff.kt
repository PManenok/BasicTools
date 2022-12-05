/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler

interface ICheckDiff<Entity> {
    fun checkIfSameItem(item: Entity): Boolean
    fun checkIfSameContent(item: Entity): Boolean
}