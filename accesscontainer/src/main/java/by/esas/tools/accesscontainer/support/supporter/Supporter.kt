/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.support.supporter

import by.esas.tools.logger.BaseErrorModel

class Supporter<E : Enum<E>, M : BaseErrorModel<E>>(
    val resProvider: ResourceStrProvider,
    val util: IUtil<E, M>
)