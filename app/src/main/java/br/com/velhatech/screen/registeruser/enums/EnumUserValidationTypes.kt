package br.com.velhatech.screen.registeruser.enums

enum class EnumUserValidationTypes {
    REQUIRED_USER_NAME,
    MAX_LENGTH_USER_NAME,

    REQUIRED_USER_EMAIL,
    MAX_LENGTH_USER_EMAIL,
    INVALID_USER_EMAIL,
    USER_EMAIL_IN_USE,

    REQUIRED_USER_PASSWORD,
    MAX_LENGTH_USER_PASSWORD
}