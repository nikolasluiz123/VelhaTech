package br.com.velhatech.state

import br.com.velhatech.components.fields.state.RadioButtonField
import br.com.velhatech.components.fields.state.TextField
import br.com.velhatech.core.state.ILoadingUIState
import br.com.velhatech.core.state.MessageDialogState
import br.com.velhatech.firebase.enums.EnumDifficultLevel
import br.com.velhatech.firebase.to.TORoom
import br.com.velhatech.screen.roomcreation.enums.EnumRoundType

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
