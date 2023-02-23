/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.domain.mapper

import by.esas.tools.domain.mapper.error.ApiErrorStatusEnum
import by.esas.tools.domain.mapper.error.ErrorMessageEnum
import by.esas.tools.domain.mapper.error.HttpErrorStatusEnum
import by.esas.tools.domain.mapper.error.IdentityErrorEnum

fun mapApiErrorToHttpStatus(status: ApiErrorStatusEnum?): HttpErrorStatusEnum {
    return when (status) {
        ApiErrorStatusEnum.OK -> HttpErrorStatusEnum.OK
        ApiErrorStatusEnum.Fail -> HttpErrorStatusEnum.UNKNOWN_ERROR
        ApiErrorStatusEnum.Unauthorized -> HttpErrorStatusEnum.UNAUTHORIZED
        ApiErrorStatusEnum.Forbidden -> HttpErrorStatusEnum.FORBIDDEN
        ApiErrorStatusEnum.Add_Email_Error -> HttpErrorStatusEnum.ADD_EMAIL_ERROR
        ApiErrorStatusEnum.Verify_Email_Error -> HttpErrorStatusEnum.VERIFY_EMAIL_ERROR
        ApiErrorStatusEnum.Delete_Email_Error -> HttpErrorStatusEnum.DELETE_EMAIL_ERROR
        ApiErrorStatusEnum.Add_Phone_Error -> HttpErrorStatusEnum.ADD_PHONE_ERROR
        ApiErrorStatusEnum.Verify_Phone_Error -> HttpErrorStatusEnum.VERIFY_PHONE_ERROR
        ApiErrorStatusEnum.Delete_Phone_Error -> HttpErrorStatusEnum.DELETE_PHONE_ERROR
        ApiErrorStatusEnum.TFA_Mode_Error -> HttpErrorStatusEnum.UNKNOWN_ERROR
        ApiErrorStatusEnum.Update_Profile_Error -> HttpErrorStatusEnum.UPDATE_PROFILE_ERROR
        ApiErrorStatusEnum.Get_Profile_Error -> HttpErrorStatusEnum.GET_PROFILE_ERROR
        ApiErrorStatusEnum.Get_Providers_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Link_Provider_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Unlink_Provider_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Incorrect_Manage_Request -> HttpErrorStatusEnum.REQUEST_ERROR
        ApiErrorStatusEnum.Email_Already_Set -> HttpErrorStatusEnum.EMAIL_ALREADY_SET
        ApiErrorStatusEnum.Phone_Number_Is_Not_Confirmed -> HttpErrorStatusEnum.PHONE_NUMBER_NOT_CONFIRMED
        ApiErrorStatusEnum.Phone_Number_Already_Set -> HttpErrorStatusEnum.PHONE_ALREADY_SET
        ApiErrorStatusEnum.Email_Verify_Fail -> HttpErrorStatusEnum.VERIFY_EMAIL_ERROR
        ApiErrorStatusEnum.Email_Delete_Fail -> HttpErrorStatusEnum.DELETE_EMAIL_ERROR
        ApiErrorStatusEnum.Phone_Number_Verify_Fail -> HttpErrorStatusEnum.VERIFY_PHONE_ERROR
        ApiErrorStatusEnum.Phone_Number_Delete_Fail -> HttpErrorStatusEnum.DELETE_PHONE_ERROR
        ApiErrorStatusEnum.Email_Is_Not_Confirmed -> HttpErrorStatusEnum.EMAIL_NUMBER_NOT_CONFIRMED
        ApiErrorStatusEnum.Change_TFA_Mode_Fail -> HttpErrorStatusEnum.UNKNOWN_ERROR
        ApiErrorStatusEnum.Update_Profile_Fail -> HttpErrorStatusEnum.UPDATE_PROFILE_ERROR
        ApiErrorStatusEnum.Get_Profile_Fail -> HttpErrorStatusEnum.GET_PROFILE_ERROR
        ApiErrorStatusEnum.Email_Has_Another_User -> HttpErrorStatusEnum.EMAIL_TAKEN
        ApiErrorStatusEnum.Phone_Has_Another_User -> HttpErrorStatusEnum.PHONE_TAKEN
        ApiErrorStatusEnum.Get_Providers_Fail -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Link_Provider_Fail -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Unlink_Provider_Fail -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Register_User_Error -> HttpErrorStatusEnum.REGISTER_ERROR
        ApiErrorStatusEnum.Activate_User_Error -> HttpErrorStatusEnum.ACTIVATION_ERROR
        ApiErrorStatusEnum.Forgot_Password_Error -> HttpErrorStatusEnum.PASSWORD_RECOVERY_FAILED
        ApiErrorStatusEnum.Reset_Password_Error -> HttpErrorStatusEnum.PASSWORD_RECOVERY_FAILED
        ApiErrorStatusEnum.Is_Exist_User_Error -> HttpErrorStatusEnum.USER_ALREADY_EXISTS
        ApiErrorStatusEnum.Reactivate_Account_Error -> HttpErrorStatusEnum.REACTIVATE_ERROR
        ApiErrorStatusEnum.Get_ProvidersList_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Bind_User_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Create_Account_Error -> HttpErrorStatusEnum.REGISTER_ERROR
        ApiErrorStatusEnum.Register_User_Fail -> HttpErrorStatusEnum.REGISTER_ERROR
        ApiErrorStatusEnum.Password_Validation_Failed -> HttpErrorStatusEnum.PASSWORD_VALIDATION_ERROR
        ApiErrorStatusEnum.Incorrect_Username_Or_Password -> HttpErrorStatusEnum.WRONG_PASSWORD_OR_USERNAME
        ApiErrorStatusEnum.Incorrect_Account_Request -> HttpErrorStatusEnum.REQUEST_ERROR
        ApiErrorStatusEnum.User_Not_Found -> HttpErrorStatusEnum.USER_DOES_NOT_EXIST
        ApiErrorStatusEnum.User_Activation_Fail -> HttpErrorStatusEnum.ACTIVATION_ERROR
        ApiErrorStatusEnum.Reset_Password_Fail -> HttpErrorStatusEnum.PASSWORD_RECOVERY_FAILED
        ApiErrorStatusEnum.User_Already_Exists -> HttpErrorStatusEnum.USER_ALREADY_EXISTS
        ApiErrorStatusEnum.Account_Already_Activated -> HttpErrorStatusEnum.ALREADY_ACTIVATED
        ApiErrorStatusEnum.External_Providers_Not_Found -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Create_Account_Fail -> HttpErrorStatusEnum.REGISTER_ERROR
        ApiErrorStatusEnum.Check_Account_Fail -> HttpErrorStatusEnum.CHECK_ACCOUNT_FAIL
        ApiErrorStatusEnum.Get_Users_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Get_User_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Update_User_Error -> HttpErrorStatusEnum.UPDATE_USER_ERROR
        ApiErrorStatusEnum.Delete_User_Error -> HttpErrorStatusEnum.DELETE_USER_ERROR
        ApiErrorStatusEnum.Patch_User_Error -> HttpErrorStatusEnum.SERVER_ERROR
        ApiErrorStatusEnum.Incorrect_User_Request -> HttpErrorStatusEnum.REQUEST_ERROR
        ApiErrorStatusEnum.Cant_Update_User -> HttpErrorStatusEnum.UPDATE_USER_ERROR
        ApiErrorStatusEnum.Cant_Delete_User -> HttpErrorStatusEnum.DELETE_USER_ERROR
        ApiErrorStatusEnum.Cant_Patch_User -> HttpErrorStatusEnum.SERVER_ERROR
        null -> HttpErrorStatusEnum.UNKNOWN_ERROR
    }
}

fun mapErrorMessageToHttpStatus(status: ErrorMessageEnum?): HttpErrorStatusEnum {
    return when (status) {
        ErrorMessageEnum.CONCURRENCY_FAILURE -> HttpErrorStatusEnum.SERVER_ERROR
        ErrorMessageEnum.UNKNOWN_FAILURE -> HttpErrorStatusEnum.UNKNOWN_ERROR
        ErrorMessageEnum.EMAIL_TAKEN -> HttpErrorStatusEnum.EMAIL_TAKEN
        ErrorMessageEnum.ROLE_NAME_TAKEN -> HttpErrorStatusEnum.CLIENT_ERROR
        ErrorMessageEnum.USER_NAME_TAKEN -> HttpErrorStatusEnum.USER_NAME_TAKEN
        ErrorMessageEnum.EMAIL_INVALID -> HttpErrorStatusEnum.EMAIL_INVALID
        ErrorMessageEnum.ROLE_NAME_INVALID -> HttpErrorStatusEnum.CLIENT_ERROR
        ErrorMessageEnum.INVALID_TOKEN -> HttpErrorStatusEnum.CODE_INVALID_OR_EXPIRED
        ErrorMessageEnum.USER_NAME_INVALID -> HttpErrorStatusEnum.USER_NAME_INVALID
        ErrorMessageEnum.USER_WITH_LOGIN_EXISTS -> HttpErrorStatusEnum.USER_WITH_LOGIN_EXISTS
        ErrorMessageEnum.INCORRECT_PASSWORD -> HttpErrorStatusEnum.WRONG_PASSWORD
        ErrorMessageEnum.PASSWORD_NOT_CONTAIN_DIGITS -> HttpErrorStatusEnum.INCORRECT_PASSWORD_FORMAT
        ErrorMessageEnum.PASSWORD_NOT_CONTAIN_LOWERCASE -> HttpErrorStatusEnum.INCORRECT_PASSWORD_FORMAT
        ErrorMessageEnum.PASSWORD_NOT_CONTAIN_SYMBOLS -> HttpErrorStatusEnum.INCORRECT_PASSWORD_FORMAT
        ErrorMessageEnum.PASSWORD_NOT_CONTAIN_DIFFERENT_CHARACTERS -> HttpErrorStatusEnum.INCORRECT_PASSWORD_FORMAT
        ErrorMessageEnum.PASSWORD_NOT_CONTAIN_UPPERCASE -> HttpErrorStatusEnum.INCORRECT_PASSWORD_FORMAT
        ErrorMessageEnum.PASSWORD_TOO_SHORT -> HttpErrorStatusEnum.INCORRECT_PASSWORD_FORMAT
        ErrorMessageEnum.RECOVERY_CODE_REDEMPTION_FAILED -> HttpErrorStatusEnum.SERVER_ERROR
        ErrorMessageEnum.USER_ALREADY_SET_PASSWORD -> HttpErrorStatusEnum.CLIENT_ERROR
        ErrorMessageEnum.USER_ALREADY_IN_ROLE -> HttpErrorStatusEnum.CLIENT_ERROR
        ErrorMessageEnum.LOCKOUT_NOT_ENABLED -> HttpErrorStatusEnum.CLIENT_ERROR
        ErrorMessageEnum.USER_IS_NOT_IN_ROLE -> HttpErrorStatusEnum.CLIENT_ERROR
        ErrorMessageEnum.ACCOUNT_NOT_ACTIVATED -> HttpErrorStatusEnum.USER_NOT_ACTIVATED
        ErrorMessageEnum.INVALID_USERNAME_OR_PASSWORD -> HttpErrorStatusEnum.WRONG_PASSWORD
        ErrorMessageEnum.INVALID_PASSWORD_VALIDATION_REQUEST -> HttpErrorStatusEnum.SERVER_ERROR
        ErrorMessageEnum.USER_LOCKED_OUT -> HttpErrorStatusEnum.USER_LOCKED_OUT
        null -> HttpErrorStatusEnum.UNKNOWN_ERROR
    }
}

fun mapIdentityErrorToHttpStatus(status: IdentityErrorEnum?): HttpErrorStatusEnum {
    return when (status) {
        IdentityErrorEnum.invalid_request -> HttpErrorStatusEnum.REQUEST_ERROR
        IdentityErrorEnum.request_uri_not_supported -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.request_not_supported -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.invalid_request_object -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.invalid_request_uri -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.consent_required -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.account_selection_required -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.registration_not_supported -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.login_required -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.temporarily_unavailable -> HttpErrorStatusEnum.UNAVAILABLE
        IdentityErrorEnum.server_error -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.invalid_scope -> HttpErrorStatusEnum.SCOPE_ERROR
        IdentityErrorEnum.unsupported_response_type -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.access_denied -> HttpErrorStatusEnum.ACCESS_DENIED
        IdentityErrorEnum.unauthorized_client -> HttpErrorStatusEnum.UNAUTHORIZED
        IdentityErrorEnum.interaction_required -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.invalid_target -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.invalid_client -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.invalid_grant -> HttpErrorStatusEnum.INVALID_GRANT
        IdentityErrorEnum.unsupported_grant_type -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.authorization_pending -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.slow_down -> HttpErrorStatusEnum.SERVER_ERROR
        IdentityErrorEnum.expired_token -> HttpErrorStatusEnum.CODE_INVALID_OR_EXPIRED
        IdentityErrorEnum.invalid_token -> HttpErrorStatusEnum.CODE_INVALID_OR_EXPIRED
        IdentityErrorEnum.insufficient_scope -> HttpErrorStatusEnum.SCOPE_ERROR
        null -> HttpErrorStatusEnum.UNKNOWN_ERROR
    }
}