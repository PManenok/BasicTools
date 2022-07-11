/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.interfaces

import androidx.lifecycle.MutableLiveData
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment

interface IShowingVM {
    val showDialog: MutableLiveData<BaseDialogFragment<*>?>
    val showBottomDialog: MutableLiveData<BaseBottomDialogFragment<*>?>

    fun showDialog(dialog: BaseDialogFragment<*>) {
        showDialog.postValue(dialog)
    }

    fun showDialog(dialog: BaseBottomDialogFragment<*>) {
        showBottomDialog.postValue(dialog)
    }
}