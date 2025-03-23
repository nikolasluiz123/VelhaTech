package br.com.velhatech.screen.roomlist

import br.com.velhatech.firebase.enums.EnumDifficultLevel
import br.com.velhatech.firebase.to.TORoom
import br.com.velhatech.state.RoomListUIState


internal val emptyUIState = RoomListUIState(
    subtitle = "Nikolas Luiz Schmitt"
)

internal val populatedUIState = RoomListUIState(
    subtitle = "Nikolas Luiz Schmitt",
    rooms = listOf(
        TORoom(
            roomName = "Sala 1",
            roundsCount = 3,
            maxPlayers = 2,
            playersCount = 1,
            difficultLevel = EnumDifficultLevel.EASY,
            password = null,
        ),
        TORoom(
            roomName = "Sala 2",
            roundsCount = 5,
            maxPlayers = 2,
            playersCount = 1,
            difficultLevel = EnumDifficultLevel.HARD,
            password = "123",
        )
    )
)