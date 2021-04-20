/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.dialog

interface StateCallback<T : Exception> {
    /**
     * This method is used to handle errors and exceptions of type [T], which happens in dialog
     * @see BaseDialogFragment
     * @see BaseBottomDialogFragment
     * @param e is the error from dialog
     * */
    fun onError(e: T)

    /**
     * This method is used to handle dismiss action of dialog. afterOk flag helps to handle this event in the right way
     * @see BaseDialogFragment
     * @see BaseBottomDialogFragment
     * @param afterOk - flag that shows if the dismiss action was after successful event or if dialog was, for example, canceled
     * */
    fun onDismiss(afterOk: Boolean)
}