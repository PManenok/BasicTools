package by.esas.tools.accesscontainer.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager

interface IBaseDialog {
    fun dismiss()
    fun show(manager: FragmentManager?, context: Context?, tag: String)
}