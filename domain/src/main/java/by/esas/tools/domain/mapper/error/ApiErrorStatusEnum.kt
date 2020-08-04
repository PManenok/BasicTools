package by.esas.tools.domain.mapper.error

enum class ApiErrorStatusEnum {
    OK,
    Fail,
    Unauthorized,
    Forbidden,

    //#region ManageApi
    //#region Коды ошибок для работы с профилем
    //#region Ошибки
    Add_Email_Error,
    Verify_Email_Error,
    Delete_Email_Error,
    Add_Phone_Error,
    Verify_Phone_Error,
    Delete_Phone_Error,
    TFA_Mode_Error,
    Update_Profile_Error,
    Get_Profile_Error,
    Get_Providers_Error,
    Link_Provider_Error,
    Unlink_Provider_Error,

    //#region Предупреждения
    Incorrect_Manage_Request,
    Email_Already_Set,
    Phone_Number_Is_Not_Confirmed,
    Phone_Number_Already_Set,
    Email_Verify_Fail,
    Email_Delete_Fail,
    Phone_Number_Verify_Fail,
    Phone_Number_Delete_Fail,
    Email_Is_Not_Confirmed,
    Change_TFA_Mode_Fail,
    Update_Profile_Fail,
    Get_Profile_Fail,
    Email_Has_Another_User,
    Phone_Has_Another_User,
    Get_Providers_Fail,
    Link_Provider_Fail,
    Unlink_Provider_Fail,

    //#region AccountApi
    //#region Ошибки
    Register_User_Error,
    Activate_User_Error,
    Forgot_Password_Error,
    Reset_Password_Error,
    Is_Exist_User_Error,
    Reactivate_Account_Error,
    Get_ProvidersList_Error,
    Bind_User_Error,
    Create_Account_Error,

    //#region Предупреждения
    Register_User_Fail,
    Password_Validation_Failed,
    Incorrect_Username_Or_Password,
    Incorrect_Account_Request,
    User_Not_Found,
    User_Activation_Fail,
    Reset_Password_Fail,
    User_Already_Exists,
    Account_Already_Activated,
    External_Providers_Not_Found,
    Create_Account_Fail,
    Check_Account_Fail,

    //#region UserApi
    //#region Ошибки
    Get_Users_Error,
    Get_User_Error,
    Update_User_Error,
    Delete_User_Error,
    Patch_User_Error,

    //#region Предупреждения
    Incorrect_User_Request,
    Cant_Update_User,
    Cant_Delete_User,
    Cant_Patch_User
}