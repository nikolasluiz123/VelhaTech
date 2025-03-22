package br.com.velhatech.screen.registeruser.enums

import br.com.velhatech.R


enum class EnumValidatedUserFields(val labelResId: Int, val maxLength: Int = 0) {
    NAME(R.string.label_user_name, 512),
    EMAIL(R.string.label_user_email, 64),
    PASSWORD(R.string.label_user_password, 1024),
}