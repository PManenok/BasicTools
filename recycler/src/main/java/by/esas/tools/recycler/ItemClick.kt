/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.recycler

data class ItemClick<Entity>(val item: Entity, val position: Int)