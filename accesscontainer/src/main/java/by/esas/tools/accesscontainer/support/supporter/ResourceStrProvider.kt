/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.support.supporter

import android.content.Context
import androidx.annotation.StringRes

interface ResourceStrProvider {
    fun requireContext(): Context
    fun getString(@StringRes resId: Int): String
    fun provideAlterCancelStr(): Int
    fun provideCancelStr(): Int
    fun provideAccessStr(): Int
    fun provideAccessConfirmStr(): Int
    fun provideEncryptTitle(): Int
}