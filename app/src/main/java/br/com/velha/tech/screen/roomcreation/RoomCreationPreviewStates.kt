package br.com.velha.tech.screen.roomcreation

import br.com.velha.tech.components.buttons.radio.RadioButtonOption
import br.com.velha.tech.components.fields.state.RadioButtonField
import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.screen.roomcreation.enums.EnumRoundType
import br.com.velha.tech.state.RoomUIState

internal val defaultRoomState = RoomUIState(
    rounds = RadioButtonField(
        options = EnumRoundType.entries.map(::RadioButtonOption)
    ),
    difficultLevel = RadioButtonField(
        options = EnumDifficultLevel.entries.map(::RadioButtonOption)
    )
)