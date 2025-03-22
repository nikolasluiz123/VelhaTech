package br.com.velhatech.usecase

import android.content.Context
import br.com.velhatech.R
import br.com.velhatech.repository.UserRepository
import br.com.velhatech.core.validation.FieldValidationError
import br.com.velhatech.screen.login.enums.EnumLoginValidationTypes
import br.com.velhatech.screen.login.enums.EnumValidatedLoginFields
import br.com.velhatech.screen.login.enums.EnumValidatedLoginFields.*

class DefaultLoginUseCase(
    private val context: Context,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String?, password: String?): List<FieldValidationError<EnumValidatedLoginFields, EnumLoginValidationTypes>> {
        val validationsResults = mutableListOf(
            validateEmail(email),
            validatePassword(password),
            validateUserCredentials(email, password)
        ).filterNotNull().toMutableList()

        if (validationsResults.isEmpty()) {
            userRepository.authenticate(
                email = email!!,
                password = password!!
            )
        }

        return validationsResults
    }

    private fun validatePassword(password: String?): FieldValidationError<EnumValidatedLoginFields, EnumLoginValidationTypes>? {
        return when {
            password?.trim().isNullOrEmpty() -> {
                val message = context.getString(
                    br.com.velhatech.core.R.string.validation_msg_required_field,
                    context.getString(PASSWORD.labelResId)
                )

                FieldValidationError(
                    field = PASSWORD,
                    message = message,
                    validationType = EnumLoginValidationTypes.REQUIRED_PASSWORD
                )
            }

            else -> null
        }
    }

    private fun validateEmail(email: String?): FieldValidationError<EnumValidatedLoginFields, EnumLoginValidationTypes>? {
        return when {
            email?.trim().isNullOrEmpty() -> {
                val message = context.getString(
                    br.com.velhatech.core.R.string.validation_msg_required_field,
                    context.getString(EMAIL.labelResId)
                )

                FieldValidationError(
                    field = EMAIL,
                    message = message,
                    validationType = EnumLoginValidationTypes.REQUIRED_EMAIL
                )
            }

            else -> null
        }
    }

    private fun validateUserCredentials(
        email: String?,
        password: String?
    ): FieldValidationError<EnumValidatedLoginFields, EnumLoginValidationTypes>? {
        val emailTrimmed = email?.trim()
        val passwordTrimmed = password?.trim()

        if (emailTrimmed.isNullOrEmpty() || passwordTrimmed.isNullOrEmpty()) {
            return null
        }

        val invalidLength = emailTrimmed.length > EMAIL.maxLength || passwordTrimmed.length > PASSWORD.maxLength

        return when {
            invalidLength -> {
                FieldValidationError(
                    field = null,
                    message = context.getString(R.string.validation_msg_invalid_credetials_login),
                    validationType = EnumLoginValidationTypes.INVALID_CREDENTIALS
                )
            }

            else -> null
        }
    }
}