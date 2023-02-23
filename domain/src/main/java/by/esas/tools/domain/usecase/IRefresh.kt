/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.domain.usecase

import by.esas.tools.logger.BaseErrorModel

interface IRefresh<Model : BaseErrorModel> {

    fun getToken(): String
    fun refresh(repeat: () -> Unit, onError: (Model) -> Unit, onCancel: () -> Unit)
    fun onCancel()
}