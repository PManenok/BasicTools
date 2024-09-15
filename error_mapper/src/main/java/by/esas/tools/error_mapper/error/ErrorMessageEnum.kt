/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.error_mapper.error

enum class ErrorMessageEnum(val message: String) {
    ACCOUNT_NOT_ACTIVATED("Account hasn't been activated"),
    INVALID_USERNAME_OR_PASSWORD("Invalid username or password"),//Invalid password validation request
    INVALID_PASSWORD_VALIDATION_REQUEST("Invalid password validation request"),
    USER_LOCKED_OUT("User is locked out"),

    CONCURRENCY_FAILURE("Optimistic concurrency failure, object has been modified."),
    UNKNOWN_FAILURE("An unknown failure has occurred."),
    EMAIL_TAKEN("Email [\\w]+ is already taken."),
    ROLE_NAME_TAKEN("Role name [\\w]+ is already taken."),
    USER_NAME_TAKEN("User name [\\w]+ is already taken."),
    EMAIL_INVALID("Email [\\w]+ is invalid."),
    ROLE_NAME_INVALID("Role name [\\w]+ is invalid."),
    INVALID_TOKEN("Invalid token."),
    USER_NAME_INVALID("User name [\\w]+ is invalid, can only contain letters or digits."),
    USER_WITH_LOGIN_EXISTS("A user with this login already exists."),
    INCORRECT_PASSWORD("Incorrect password."),
    PASSWORD_NOT_CONTAIN_DIGITS("Passwords must have at least one digit ('0'-'9'),."),
    PASSWORD_NOT_CONTAIN_LOWERCASE("Passwords must have at least one lowercase ('a'-'z'),."),
    PASSWORD_NOT_CONTAIN_SYMBOLS("Passwords must have at least one non alphanumeric character."),
    PASSWORD_NOT_CONTAIN_DIFFERENT_CHARACTERS("Passwords must use at least [\\w]+ different characters."),
    PASSWORD_NOT_CONTAIN_UPPERCASE("Passwords must have at least one uppercase ('A'-'Z'),."),
    PASSWORD_TOO_SHORT("Passwords must be at least [\\w]+ characters."),
    RECOVERY_CODE_REDEMPTION_FAILED("Recovery code redemption failed."),
    USER_ALREADY_SET_PASSWORD("User already has a password set."),
    USER_ALREADY_IN_ROLE("User already in role [\\w]+."),
    LOCKOUT_NOT_ENABLED("Lockout is not enabled for this user."),
    USER_IS_NOT_IN_ROLE("User is not in role [\\w]+.")
}