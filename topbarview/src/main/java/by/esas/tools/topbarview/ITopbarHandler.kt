/*
 * Copyright 2022 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.topbarview

/**
 * Interface for handling clicks on Topbar view
 * on navigation button(left image or text icon)
 * and action button (right image or text icon).
 */

interface ITopbarHandler {

    fun onNavigationClick() {}
    fun onActionClick() {}
}