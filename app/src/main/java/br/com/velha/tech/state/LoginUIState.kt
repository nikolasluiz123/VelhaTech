package br.com.velha.tech.state

import br.com.velha.tech.components.fields.state.TextField
import br.com.velha.tech.core.state.ILoadingUIState
import br.com.velha.tech.core.state.MessageDialogState

data class LoginUIState(
    val email: TextField = TextField(),
    val password: TextField = TextField(),
    val messageDialogState: MessageDialogState = MessageDialogState(),
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
) : ILoadingUIState