/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.support

import by.esas.tools.accesscontainer.ContainerRequest
import by.esas.tools.accesscontainer.entity.AuthType
import by.esas.tools.accesscontainer.entity.Token
import by.esas.tools.logger.BaseErrorModel
import javax.crypto.Cipher
import javax.crypto.SecretKey

interface IContainerExecutor<M : BaseErrorModel> {
    fun refreshAccessUC(refreshToken: String, block: ContainerRequest<Token?, M>.() -> Unit)

    fun getSecretsUC(block: ContainerRequest<List<AuthType>, M>.() -> Unit)

    fun refreshWithSecretUC(
        pinKey: SecretKey? = null,
        cipher: Cipher? = null,
        block: ContainerRequest<Token, M>.() -> Unit
    )

    fun checkSecretUC(
        pinKey: SecretKey? = null,
        cipher: Cipher? = null,
        block: ContainerRequest<Pair<Boolean, String>, M>.() -> Unit
    )

    fun refreshWithSecretUC(secret: String, block: ContainerRequest<Token, M>.() -> Unit)

    fun authorizationInSystemUC(password: String, block: ContainerRequest<Token, M>.() -> Unit)

    fun createSecretUC(
        token: String,
        cipher: Cipher? = null,
        pinKey: SecretKey? = null,
        block: ContainerRequest<Boolean, M>.() -> Unit
    )

    fun deleteSecretTypeUC(type: AuthType, block: ContainerRequest<Boolean, M>.() -> Unit)

    fun deleteSecretsUC(block: ContainerRequest<Boolean, M>.() -> Unit)

    fun unsubscribe()
}