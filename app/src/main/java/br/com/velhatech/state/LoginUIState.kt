package br.com.velhatech.state

import br.com.velhatech.components.fields.state.TextField
import br.com.velhatech.core.state.ILoadingUIState
import br.com.velhatech.core.state.MessageDialogState

data class LoginUIState(
    val email: TextField = TextField(),
    val password: TextField = TextField(),
    val messageDialogState: MessageDialogState = MessageDialogState(),
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
) : ILoadingUIState