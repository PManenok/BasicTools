/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.accesscontainer.support.supporter

import by.esas.tools.logger.BaseErrorModel

class Supporter<M : BaseErrorModel>(
    val resProvider: ResourceStrProvider,
    val util: IUtil<M>
)