package br.com.velha.tech.state

import br.com.velha.tech.components.fields.state.RadioButtonField
import br.com.velha.tech.components.fields.state.TextField
import br.com.velha.tech.core.state.ILoadingUIState
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.to.TORoom
import br.com.velha.tech.screen.roomcreation.enums.EnumRoundType

data class RoomUIState(
    val messageDialogState: MessageDialogState = MessageDialogState(),
    val roomName: TextField = TextField(),
    val roomPassword: TextField = TextField(),
    val rounds: RadioButtonField<EnumRoundType> = RadioButtonField(),
    val difficultLevel: RadioButtonField<EnumDifficultLevel> = RadioButtonField(),
    val toRoom: TORoom = TORoom(),
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { }
): ILoadingUIState
