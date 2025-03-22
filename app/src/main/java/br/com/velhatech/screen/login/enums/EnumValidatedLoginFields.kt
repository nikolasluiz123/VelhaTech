package br.com.velhatech.screen.login.enums

import br.com.velhatech.R
import br.com.velhatech.screen.registeruser.enums.EnumValidatedUserFields


enum class EnumValidatedLoginFields(val labelResId: Int, val maxLength: Int = 0) {
    EMAIL(R.string.label_user_email, EnumValidatedUserFields.EMAIL.maxLength),
    PASSWORD(R.string.label_user_password, EnumValidatedUserFields.PASSWORD.maxLength),
}