package by.esas.tools

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import by.esas.tools.app_domain.error_mapper.AppErrorStatusEnum
import java.util.*

fun getLocale(context: Context): Locale {
    return Locale.getDefault()
}

/*    Standard    */
fun hideSystemUIApp(activity: Activity?) {
    // Enables regular immersive mode.
    // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
    // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    if (activity != null) {
        activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                //or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                /*or View.SYSTEM_UI_FLAG_FULLSCREEN*/)
    }
}

fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun getErrorMessage(status: AppErrorStatusEnum, message: String? = null): String {
    val stringRes: Int = when (status) {
        AppErrorStatusEnum.OK -> R.string.error_ok
        AppErrorStatusEnum.NOT_SET -> R.string.error_unknown_happened
        AppErrorStatusEnum.UNKNOWN_ERROR -> R.string.error_unknown_happened
        AppErrorStatusEnum.CLIENT_ERROR -> R.string.error_unknown_happened
        AppErrorStatusEnum.SERVER_ERROR -> R.string.error_server_error
        //Authorization
        AppErrorStatusEnum.UNAUTHORIZED -> R.string.error_unauthenticated
        AppErrorStatusEnum.ACCESS_DENIED -> R.string.error_access_denied
        AppErrorStatusEnum.INVALID_GRANT -> R.string.error_invalid_refresh
        //NET
        AppErrorStatusEnum.NET_NO_CONNECTION -> R.string.error_net_no_connection
        AppErrorStatusEnum.NET_UNAVAILABLE -> R.string.error_net_server_unavailable
        AppErrorStatusEnum.NET_TIMEOUT -> R.string.error_net_timeout
        AppErrorStatusEnum.NET_SSL_HANDSHAKE -> R.string.error_net_ssl_handshake
        AppErrorStatusEnum.NET_UNKNOWN_HOST -> R.string.error_net_unknown_host
        //APP
        AppErrorStatusEnum.APP_UNPREDICTED_ERROR -> R.string.error_app_unpredicted
        AppErrorStatusEnum.APP_DATE_IN_UNEXPECTED_FORMAT -> R.string.error_date_unexpected_format
        AppErrorStatusEnum.APP_NO_ACCESS_TOKEN -> R.string.error_app_no_access_token
        AppErrorStatusEnum.APP_CONTEXT_PARAMETERS_EMPTY -> R.string.error_app_context_parameters_empty
        AppErrorStatusEnum.APP_USER_HAS_NO_SECRETS -> R.string.error_app_user_has_no_secrets
        //Decryption
        AppErrorStatusEnum.BIOMETRIC_UNAVAILABLE -> R.string.error_decryption_biom
        AppErrorStatusEnum.SECRET_CHECK_NOT_MATCH -> R.string.error_app_secret_check_failed
        AppErrorStatusEnum.DECRYPTION_FAILED -> R.string.error_decryption
        AppErrorStatusEnum.APP_REFRESH_TOKEN_DECRYPTION_FAILED -> R.string.error_decryption
        AppErrorStatusEnum.APP_PIN_DECRYPTION_FAILED -> R.string.error_decryption_pin
        AppErrorStatusEnum.APP_BIOMETRIC_DECRYPTION_FAILED -> R.string.error_decryption_biom
        AppErrorStatusEnum.APP_PRIVATE_KEY_DECRYPTION_FAILED -> R.string.error_decryption_private_key
        AppErrorStatusEnum.APP_BIOMETRIC_KEY_DECRYPTION_FAILED -> R.string.error_decryption
        AppErrorStatusEnum.APP_SECRET_KEY_DECRYPTION_FAILED -> R.string.error_decryption_secret_key
        //API
        AppErrorStatusEnum.API_REDIRECTION -> R.string.error_unknown_happened
        AppErrorStatusEnum.API_USER_DOES_NOT_EXIST -> R.string.error_api_user_not_exist_message
        AppErrorStatusEnum.API_USER_ALREADY_EXISTS -> R.string.error_api_user_exists
        AppErrorStatusEnum.API_USER_NOT_ACTIVATED -> R.string.error_api_user_not_active
        AppErrorStatusEnum.API_PASSWORD_RECOVERY_FAILED -> R.string.error_api_password_recovery_failed
        AppErrorStatusEnum.API_ACTIVATION_FAILED -> R.string.error_api_activation_failed
        AppErrorStatusEnum.API_REQUEST_ERROR -> R.string.error_api_request
        AppErrorStatusEnum.API_WRONG_PASSWORD -> R.string.error_api_wrong_password
        AppErrorStatusEnum.API_USER_LOCKED_OUT -> R.string.error_api_user_locked_out
        AppErrorStatusEnum.API_SCOPE_ERROR -> R.string.error_unknown_happened
        AppErrorStatusEnum.API_ALREADY_ACTIVATED -> R.string.error_api_already_activated
        AppErrorStatusEnum.API_REACTIVATE_ERROR -> R.string.error_api_reactivate_error
        AppErrorStatusEnum.API_REGISTER_ERROR -> R.string.error_api_register_error
        AppErrorStatusEnum.API_PHONE_TAKEN -> R.string.error_api_phone_taken
        AppErrorStatusEnum.API_EMAIL_NUMBER_NOT_CONFIRMED -> R.string.error_api_email_not_confirmed
        AppErrorStatusEnum.API_EMAIL_TAKEN -> R.string.error_api_email_taken
        AppErrorStatusEnum.API_EMAIL_INVALID -> R.string.error_api_email_invalid
        AppErrorStatusEnum.API_USER_NAME_TAKEN -> R.string.error_api_user_name_taken
        AppErrorStatusEnum.API_USER_NAME_INVALID -> R.string.error_api_user_name_invalid
        AppErrorStatusEnum.API_INCORRECT_PASSWORD_FORMAT -> R.string.error_api_incorrect_password_format
        AppErrorStatusEnum.API_FORBIDDEN -> R.string.error_api_forbidden
        AppErrorStatusEnum.API_ADD_EMAIL_ERROR -> R.string.error_api_add_email_error
        AppErrorStatusEnum.API_VERIFY_EMAIL_ERROR -> R.string.error_api_verify_email_error
        AppErrorStatusEnum.API_DELETE_EMAIL_ERROR -> R.string.error_api_delete_email_error
        AppErrorStatusEnum.API_EMAIL_ALREADY_SET -> R.string.error_api_email_already_set
        AppErrorStatusEnum.API_ADD_PHONE_ERROR -> R.string.error_api_add_phone_error
        AppErrorStatusEnum.API_VERIFY_PHONE_ERROR -> R.string.error_api_verify_phone_error
        AppErrorStatusEnum.API_DELETE_PHONE_ERROR -> R.string.error_api_delete_phone_error
        AppErrorStatusEnum.API_PHONE_NUMBER_NOT_CONFIRMED -> R.string.error_api_phone_not_confirmed
        AppErrorStatusEnum.API_PHONE_ALREADY_SET -> R.string.error_api_phone_already_set
        AppErrorStatusEnum.API_CODE_INVALID_OR_EXPIRED -> R.string.error_api_wrong_verify_code
        AppErrorStatusEnum.APP_ILLEGAL_PATTERN_CHARACTER -> R.string.error_date_unexpected_format
    }
    return if (stringRes == -1) {
        message ?: App.appContext.getString(R.string.error_unknown_happened)
    } else
        App.appContext.getString(stringRes)
}