package by.esas.tools.accesscontainer.support.supporter

abstract class Supporter<T> {
    abstract val resProvider: ResourceStrProvider
    abstract val util: IUtil<T>
}