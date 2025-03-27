package br.com.velha.tech.screen.roomlist

import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORoom
import br.com.velha.tech.state.RoomListUIState


internal val emptyUIState = RoomListUIState(
    subtitle = "Nikolas Luiz Schmitt"
)

internal val populatedUIState = RoomListUIState(
    subtitle = "Nikolas Luiz Schmitt",
    rooms = listOf(
        TORoom(
            roomName = "Sala 1",
            roundsCount = 3,
            playersCount = 1,
            difficultLevel = EnumDifficultLevel.EASY,
            password = null,
        ),
        TORoom(
            roomName = "Sala 2",
            roundsCount = 5,
            playersCount = 1,
            difficultLevel = EnumDifficultLevel.HARD,
            password = "123",
        )
    )
)