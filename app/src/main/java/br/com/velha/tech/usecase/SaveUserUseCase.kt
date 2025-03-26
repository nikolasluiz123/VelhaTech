package br.com.velha.tech.usecase

import android.content.Context
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import br.com.velha.tech.core.validation.FieldValidationError
import br.com.velha.tech.firebase.auth.user.User
import br.com.velha.tech.repository.UserRepository
import br.com.velha.tech.core.R
import br.com.velha.tech.screen.registeruser.enums.EnumValidatedUserFields

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
                    R.string.validation_msg_required_field,
                    context.getString(EnumValidatedUserFields.EMAIL.labelResId)
                )

                FieldValidationError(
                    field = EnumValidatedUserFields.EMAIL,
                    message = message,
                )
            }

            email.length > EnumValidatedUserFields.EMAIL.maxLength -> {
                val message = context.getString(
                    R.string.validation_msg_field_with_max_length,
                    context.getString(EnumValidatedUserFields.EMAIL.labelResId),
                    EnumValidatedUserFields.EMAIL.maxLength
                )

                FieldValidationError(
                    field = EnumValidatedUserFields.EMAIL,
                    message = message,
                )
            }

            !EMAIL_ADDRESS.matcher(email).matches() -> {
                val message = context.getString(
                    R.string.validation_msg_invalid_field,
                    context.getString(EnumValidatedUserFields.EMAIL.labelResId)
                )

                FieldValidationError(
                    field = EnumValidatedUserFields.EMAIL,
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
                    R.string.validation_msg_required_field,
                    context.getString(EnumValidatedUserFields.PASSWORD.labelResId)
                )

                FieldValidationError(
                    field = EnumValidatedUserFields.PASSWORD,
                    message = message,
                )
            }

            user.password!!.length > EnumValidatedUserFields.PASSWORD.maxLength -> {
                val message = context.getString(
                    R.string.validation_msg_field_with_max_length,
                    context.getString(EnumValidatedUserFields.PASSWORD.labelResId),
                    EnumValidatedUserFields.PASSWORD.maxLength
                )

                FieldValidationError(
                    field = EnumValidatedUserFields.PASSWORD,
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