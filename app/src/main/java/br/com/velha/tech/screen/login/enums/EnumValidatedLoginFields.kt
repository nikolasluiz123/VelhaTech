package br.com.velha.tech.screen.login.enums

import br.com.velha.tech.R
import br.com.velha.tech.screen.registeruser.enums.EnumValidatedUserFields


enum class EnumValidatedLoginFields(val labelResId: Int, val maxLength: Int = 0) {
    EMAIL(R.string.label_user_email, EnumValidatedUserFields.EMAIL.maxLength),
    PASSWORD(R.string.label_user_password, EnumValidatedUserFields.PASSWORD.maxLength),
}