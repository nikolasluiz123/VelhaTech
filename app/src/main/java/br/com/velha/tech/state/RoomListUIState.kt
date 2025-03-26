package br.com.velha.tech.state

import br.com.velha.tech.components.filter.SimpleFilterState
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.to.TORoom

data class RoomListUIState(
    val subtitle: String? = null,
    val rooms: List<TORoom> = emptyList(),
    val filteredRooms: List<TORoom> = emptyList(),
    val messageDialogState: MessageDialogState = MessageDialogState(),
    val simpleFilterState: SimpleFilterState = SimpleFilterState()
)
