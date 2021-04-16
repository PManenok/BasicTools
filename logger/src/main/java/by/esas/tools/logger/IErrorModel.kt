/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

interface IErrorModel<E : Enum<E>> {
    val code: Int
    val statusEnum: E
}
