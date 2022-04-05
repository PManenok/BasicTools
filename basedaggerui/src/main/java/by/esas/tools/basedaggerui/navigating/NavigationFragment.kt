/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.basedaggerui.navigating

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import by.esas.tools.basedaggerui.basic.BaseActivity
import by.esas.tools.basedaggerui.mvvm.DataBindingFragment
import by.esas.tools.dialog.BaseBottomDialogFragment
import by.esas.tools.dialog.BaseDialogFragment
import by.esas.tools.logger.BaseErrorModel

abstract class NavigationFragment<VM : NavigatingViewModel<E, M>, B : ViewDataBinding, E : Enum<E>, M : BaseErrorModel<E>> :
    DataBindingFragment<VM, B, E, M>() {
    protected var navController: NavController? = null
    abstract val fragmentDestinationId: Int



}