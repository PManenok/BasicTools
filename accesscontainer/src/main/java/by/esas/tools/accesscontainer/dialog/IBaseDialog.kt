package by.esas.tools.accesscontainer.dialog

import androidx.fragment.app.FragmentManager

interface IBaseDialog {
    fun dismiss()
    fun show(manager: FragmentManager, tag: String)
}