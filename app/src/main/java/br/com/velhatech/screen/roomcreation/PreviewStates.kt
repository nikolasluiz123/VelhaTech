package br.com.velhatech.screen.roomcreation

import br.com.velhatech.components.buttons.radio.RadioButtonOption
import br.com.velhatech.components.fields.state.RadioButtonField
import br.com.velhatech.firebase.enums.EnumDifficultLevel
import br.com.velhatech.screen.roomcreation.enums.EnumRoundType
import br.com.velhatech.state.RoomUIState

internal val defaultRoomState = RoomUIState(
    rounds = RadioButtonField(
        options = EnumRoundType.entries.map(::RadioButtonOption)
    ),
    difficultLevel = RadioButtonField(
        options = EnumDifficultLevel.entries.map(::RadioButtonOption)
    )
)