package br.com.velhatech.state

import br.com.velhatech.components.filter.SimpleFilterState
import br.com.velhatech.core.state.MessageDialogState
import br.com.velhatech.firebase.to.TORoom

data class RoomListUIState(
    val subtitle: String? = null,
    val rooms: List<TORoom> = emptyList(),
    val filteredRooms: List<TORoom> = emptyList(),
    val messageDialogState: MessageDialogState = MessageDialogState(),
    val simpleFilterState: SimpleFilterState = SimpleFilterState()
)
