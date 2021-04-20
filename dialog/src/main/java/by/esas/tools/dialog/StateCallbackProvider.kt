/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.dialog

interface StateCallbackProvider<E : Exception> {
    /**
     * Provide [StateCallback] for [BaseDialogFragment] or [BaseBottomDialogFragment]
     * @param enabling flag shows should or should not this callback
     * enable controls of its holder (activity or fragment) after invocation of StateCallback  methods
     * @return StateCallback
     * */
    fun provideStateCallback(enabling: Boolean): StateCallback<E>
}