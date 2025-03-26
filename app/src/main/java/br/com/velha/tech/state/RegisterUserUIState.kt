package br.com.velha.tech.state

import br.com.velha.tech.components.fields.state.TextField
import br.com.velha.tech.core.state.ILoadingUIState
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.auth.user.User

data class RegisterUserUIState(
    var title: String = "",
    val name: TextField = TextField(),
    val email: TextField = TextField(),
    val password: TextField = TextField(),
    val messageDialogState: MessageDialogState = MessageDialogState(),
    val user: User = User(),
    override val showLoading: Boolean = false,
    override val onToggleLoading: () -> Unit = { },
) : ILoadingUIState