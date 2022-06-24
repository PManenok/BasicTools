/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.baseui.test.interfaces

import by.esas.tools.domain.usecase.UseCase

interface IExecutingVM {
    var useCases: MutableList<UseCase<*, *, *>>

    fun provideUseCases(): List<UseCase<*, *, *>> {
        return emptyList()
    }

    fun addUseCases() {
        unsubscribeUseCases()
        useCases.clear()
        useCases.addAll(provideUseCases())
    }

    fun unsubscribeUseCases() {
        useCases.forEach { it.unsubscribe() }
    }
}