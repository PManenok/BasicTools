/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.domain.mapper.response

import com.squareup.moshi.Json

data class ErrorCode(
    @Json(name = "error") val error: String? = null,
    @Json(name = "error_description") val errorDescriptions: String? = null,
    @Json(name = "error_decision") val errorDecision: String? = null,
    @Json(name = "message") val message: String? = null,
    @Json(name = "code") val code: Int? = null
)