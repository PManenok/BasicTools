/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.interfaces

import by.esas.tools.checker.Checking

interface ICheckingVM {
    val checksList: MutableList<Checking>

    fun addCheck(check: Checking) {
        checksList.add(check)
    }

    fun clearChecks() {
        checksList.clear()
    }
}