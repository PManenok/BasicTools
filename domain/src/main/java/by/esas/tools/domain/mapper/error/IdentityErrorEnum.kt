/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.domain.mapper.error

enum class IdentityErrorEnum {
    invalid_request,
    request_uri_not_supported,
    request_not_supported,
    invalid_request_object,
    invalid_request_uri,
    consent_required,
    account_selection_required,
    registration_not_supported,
    login_required,
    temporarily_unavailable,
    server_error,
    invalid_scope,
    unsupported_response_type,
    access_denied,
    unauthorized_client,
    interaction_required,
    invalid_target,
    invalid_client,
    invalid_grant,
    unsupported_grant_type,
    authorization_pending,
    slow_down,
    expired_token,
    invalid_token,
    insufficient_scope
}