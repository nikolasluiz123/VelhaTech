package br.com.velhatech.usecase

import android.content.Context
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import br.com.velhatech.core.validation.FieldValidationError
import br.com.velhatech.firebase.auth.user.User
import br.com.velhatech.repository.UserRepository
import br.com.velhatech.screen.registeruser.enums.EnumValidatedUserFields
import br.com.velhatech.screen.registeruser.enums.EnumValidatedUserFields.EMAIL
import br.com.velhatech.screen.registeruser.enums.EnumValidatedUserFields.PASSWORD

class SaveUserUseCase(
    private val context: Context,
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(user: User): List<FieldValidationError<EnumValidatedUserFields>> {
        val validationResults = mutableListOf<FieldValidationError<EnumValidatedUserFields>>()
        validationResults.addAll(validateUser(user))

        if (validationResults.isEmpty()) {
            userRepository.save(user)
        }

        return validationResults
    }

    private fun validateUser(user: User): MutableList<FieldValidationError<EnumValidatedUserFields>> {
        val validationResults = mutableListOf(
            validateUserEmail(user),
            validateUserPassword(user)
        )

        return validationResults.filterNotNull().toMutableList()
    }

    private fun validateUserEmail(user: User): FieldValidationError<EnumValidatedUserFields>? {
        val email = user.email?.trim()

        val validationPair = when {
            email.isNullOrEmpty() -> {
                val message = context.getString(
                    br.com.velhatech.core.R.string.validation_msg_required_field,
                    context.getString(EMAIL.labelResId)
                )

                FieldValidationError(
                    field = EMAIL,
                    message = message,
                )
            }

            email.length > EMAIL.maxLength -> {
                val message = context.getString(
                    br.com.velhatech.core.R.string.validation_msg_field_with_max_length,
                    context.getString(EMAIL.labelResId),
                    EMAIL.maxLength
                )

                FieldValidationError(
                    field = EMAIL,
                    message = message,
                )
            }

            !EMAIL_ADDRESS.matcher(email).matches() -> {
                val message = context.getString(
                    br.com.velhatech.core.R.string.validation_msg_invalid_field,
                    context.getString(EMAIL.labelResId)
                )

                FieldValidationError(
                    field = EMAIL,
                    message = message,
                )
            }

            else -> null
        }

        if (validationPair == null) {
            user.email = email
        }

        return validationPair
    }

    private fun validateUserPassword(user: User): FieldValidationError<EnumValidatedUserFields>? {
        val password = user.password?.trim()

        val validationPair = when {
            password.isNullOrEmpty() -> {
                val message = context.getString(
                    br.com.velhatech.core.R.string.validation_msg_required_field,
                    context.getString(PASSWORD.labelResId)
                )

                FieldValidationError(
                    field = PASSWORD,
                    message = message,
                )
            }

            user.password!!.length > PASSWORD.maxLength -> {
                val message = context.getString(
                    br.com.velhatech.core.R.string.validation_msg_field_with_max_length,
                    context.getString(PASSWORD.labelResId),
                    PASSWORD.maxLength
                )

                FieldValidationError(
                    field = PASSWORD,
                    message = message,
                )
            }

            else -> null
        }

        if (validationPair == null) {
            user.password = password
        }

        return validationPair
    }
}