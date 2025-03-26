package br.com.velha.tech.usecase

import android.content.Context
import br.com.velha.tech.core.validation.FieldValidationError
import br.com.velha.tech.repository.RoomRepository
import br.com.velha.tech.core.R
import br.com.velha.tech.firebase.to.TORoom
import br.com.velha.tech.screen.roomcreation.enums.EnumValidatedRoomCreationFields
import br.com.velha.tech.screen.roomcreation.enums.EnumValidatedRoomCreationFields.*

class SaveRoomUseCase(
    private val context: Context,
    private val roomRepository: RoomRepository
) {

    suspend operator fun invoke(toRoom: TORoom): List<FieldValidationError<EnumValidatedRoomCreationFields>> {
        val validationResults = mutableListOf<FieldValidationError<EnumValidatedRoomCreationFields>>()
        validationResults.addAll(validateRoom(toRoom))

        if (validationResults.isEmpty()) {
            roomRepository.saveRoom(toRoom)
        }

        return validationResults
    }

    private fun validateRoom(room: TORoom): List<FieldValidationError<EnumValidatedRoomCreationFields>> {
        val validationResults = mutableListOf(
            validateRoomName(room),
            validateRoomPassword(room),
            validateRoomRoundsCount(room),
            validateRoomDifficultyLevel(room)
        )

        return validationResults.filterNotNull().toMutableList()
    }

    private fun validateRoomName(room: TORoom): FieldValidationError<EnumValidatedRoomCreationFields>? {
        val name = room.roomName?.trim()
        
        val validationResult = when {
            name.isNullOrEmpty() -> {
                val message = context.getString(
                    R.string.validation_msg_required_field,
                    context.getString(EnumValidatedRoomCreationFields.NAME.labelResId)
                )
                
                FieldValidationError(
                    field = EnumValidatedRoomCreationFields.NAME,
                    message = message,
                )
            }
            
            name.length > EnumValidatedRoomCreationFields.NAME.maxLength -> {
                val message = context.getString(
                    R.string.validation_msg_field_with_max_length,
                    context.getString(EnumValidatedRoomCreationFields.NAME.labelResId),
                    EnumValidatedRoomCreationFields.NAME.maxLength
                )
                
                FieldValidationError(
                    field = EnumValidatedRoomCreationFields.NAME,
                    message = message,
                )
            }
            
            else -> null
        }
        
        if (validationResult == null) {
            room.roomName = name
        }
        
        return validationResult
    }

    private fun validateRoomPassword(room: TORoom): FieldValidationError<EnumValidatedRoomCreationFields>? {
        val password = room.password?.trim()
        
        val validationResult = when {
            password.isNullOrEmpty() -> null
            
            password.length > EnumValidatedRoomCreationFields.PASSWORD.maxLength -> {
                val message = context.getString(
                    R.string.validation_msg_field_with_max_length,
                    context.getString(EnumValidatedRoomCreationFields.PASSWORD.labelResId),
                    EnumValidatedRoomCreationFields.PASSWORD.maxLength
                )

                FieldValidationError(
                    field = EnumValidatedRoomCreationFields.PASSWORD,
                    message = message,
                )
            }
            
            else -> null
        }
        
        if (validationResult == null) {
            room.password = password
        }
        
        return validationResult
    }

    private fun validateRoomRoundsCount(room: TORoom): FieldValidationError<EnumValidatedRoomCreationFields>? {
        return if (room.roundsCount == null) {
            val message = context.getString(
                R.string.validation_msg_required_field,
                context.getString(EnumValidatedRoomCreationFields.ROUNDS_COUNT.labelResId)
            )

            FieldValidationError(
                field = null,
                message = message,
            )
        } else {
            null
        }
    }

    private fun validateRoomDifficultyLevel(room: TORoom): FieldValidationError<EnumValidatedRoomCreationFields>? {
        return if (room.difficultLevel == null) {
            val message = context.getString(
                R.string.validation_msg_required_field,
                context.getString(EnumValidatedRoomCreationFields.DIFFICULTY_LEVEL.labelResId)
            )

            FieldValidationError(
                field = null,
                message = message,
            )
        } else {
            null
        }
    }
}