/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.interfaces

interface IControlsProvider {
    fun provideEnableControls()
    fun provideDisableControls()
    fun provideHideProgress()
    fun provide()
}