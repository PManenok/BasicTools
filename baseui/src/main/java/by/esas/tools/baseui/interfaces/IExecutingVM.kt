package by.esas.tools.baseui.interfaces

import by.esas.tools.domain.usecase.UseCase

interface IExecutingVM {
    var useCases: MutableList<UseCase<*,*,*>>

    fun provideUseCases(): List<UseCase<*,*,*>> {
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