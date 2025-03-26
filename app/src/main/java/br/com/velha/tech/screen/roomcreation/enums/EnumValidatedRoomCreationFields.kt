package br.com.velha.tech.screen.roomcreation.enums

import br.com.velha.tech.R

enum class EnumValidatedRoomCreationFields(val labelResId: Int, val maxLength: Int = 0) {
    NAME(R.string.label_room_name, 64),
    PASSWORD(R.string.label_room_password, 128),
    ROUNDS_COUNT(R.string.label_rounds_count),
    DIFFICULTY_LEVEL(R.string.label_difficulty_level),
}