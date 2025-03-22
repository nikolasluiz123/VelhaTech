package br.com.velhatech.usecase

import android.content.Context
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import br.com.velhatech.repository.UserRepository
import br.com.velhatech.core.validation.FieldValidationError
import br.com.velhatech.firebase.auth.user.User
import br.com.velhatech.screen.registeruser.enums.EnumUserValidationTypes
import br.com.velhatech.screen.registeruser.enums.EnumUserValidationTypes.*
import br.com.velhatech.screen.registeruser.enums.EnumValidatedUserFields
import br.com.velhatech.screen.registeruser.enums.EnumValidatedUserFields.*

class SaveUserUseCase(
    private val context: Context,
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(user: User): List<FieldValidationError<EnumValidatedUserFields, EnumUserValidationTypes>> {
        val validationResults = mutableListOf<FieldValidationError<EnumValidatedUserFields, EnumUserValidationTypes>>()
        validationResults.addAll(validateUser(user))

        if (validationResults.isEmpty()) {
            userRepository.save(user)
        }

        return validationResults
    }

    private fun validateUser(user: User): MutableList<FieldValidationError<EnumValidatedUserFields, EnumUserValidationTypes>> {
        val validationResults = mutableListOf(
            validateUserEmail(user),
            validateUserPassword(user)
        )

        return validationResults.filterNotNull().toMutableList()
    }

    private fun validateUserEmail(user: User): FieldValidationError<EnumValidatedUserFields, EnumUserValidationTypes>? {
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
                    validationType = REQUIRED_USER_EMAIL
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
                    validationType = MAX_LENGTH_USER_EMAIL
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
                    validationType = INVALID_USER_EMAIL
                )
            }

            else -> null
        }

        if (validationPair == null) {
            user.email = email
        }

        return validationPair
    }

    private fun validateUserPassword(user: User): FieldValidationError<EnumValidatedUserFields, EnumUserValidationTypes>? {
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
                    validationType = REQUIRED_USER_PASSWORD
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
                    validationType = MAX_LENGTH_USER_PASSWORD
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