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