package by.esas.tools.accesscontainer.support.supporter

abstract class Supporter<E:Enum<E>> {
    abstract val resProvider: ResourceStrProvider
    abstract val util: IUtil<E>
}