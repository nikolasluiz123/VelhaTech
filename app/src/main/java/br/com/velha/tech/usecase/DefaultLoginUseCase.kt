package br.com.velha.tech.usecase

import android.content.Context
import br.com.velha.tech.R
import br.com.velha.tech.repository.UserRepository
import br.com.velha.tech.core.validation.FieldValidationError
import br.com.velha.tech.screen.login.enums.EnumValidatedLoginFields
import br.com.velha.tech.screen.login.enums.EnumValidatedLoginFields.*

class DefaultLoginUseCase(
    private val context: Context,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String?, password: String?): List<FieldValidationError<EnumValidatedLoginFields>> {
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

    private fun validatePassword(password: String?): FieldValidationError<EnumValidatedLoginFields>? {
        return when {
            password?.trim().isNullOrEmpty() -> {
                val message = context.getString(
                    br.com.velha.tech.core.R.string.validation_msg_required_field,
                    context.getString(EnumValidatedLoginFields.PASSWORD.labelResId)
                )

                FieldValidationError(
                    field = EnumValidatedLoginFields.PASSWORD,
                    message = message,
                )
            }

            else -> null
        }
    }

    private fun validateEmail(email: String?): FieldValidationError<EnumValidatedLoginFields>? {
        return when {
            email?.trim().isNullOrEmpty() -> {
                val message = context.getString(
                    br.com.velha.tech.core.R.string.validation_msg_required_field,
                    context.getString(EnumValidatedLoginFields.EMAIL.labelResId)
                )

                FieldValidationError(
                    field = EnumValidatedLoginFields.EMAIL,
                    message = message,
                )
            }

            else -> null
        }
    }

    private fun validateUserCredentials(
        email: String?,
        password: String?
    ): FieldValidationError<EnumValidatedLoginFields>? {
        val emailTrimmed = email?.trim()
        val passwordTrimmed = password?.trim()

        if (emailTrimmed.isNullOrEmpty() || passwordTrimmed.isNullOrEmpty()) {
            return null
        }

        val invalidLength = emailTrimmed.length > EnumValidatedLoginFields.EMAIL.maxLength || passwordTrimmed.length > EnumValidatedLoginFields.PASSWORD.maxLength

        return when {
            invalidLength -> {
                FieldValidationError(
                    field = null,
                    message = context.getString(R.string.validation_msg_invalid_credetials_login),
                )
            }

            else -> null
        }
    }
}