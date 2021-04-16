/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler.sticky

interface CompareData {
    fun compareTo(another: CompareData): Boolean
    fun createHeader(): StickyHeader
}