package by.esas.tools.accesscontainer.support.supporter

import by.esas.tools.logger.BaseErrorModel

abstract class Supporter<E:Enum<E>, M:BaseErrorModel<E>> {
    abstract val resProvider: ResourceStrProvider
    abstract val util: IUtil<E, M>
}