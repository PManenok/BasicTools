/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.app

import android.content.Context

interface BaseApp {
    companion object {
        lateinit var appContext: Context
    }
}