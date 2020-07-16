package by.esas.tools.accesscontainer.support

import by.esas.tools.accesscontainer.entity.AuthType

interface ITypeManager {
    fun getPreferredType(): AuthType
    fun putPreferredType(type: AuthType)
}