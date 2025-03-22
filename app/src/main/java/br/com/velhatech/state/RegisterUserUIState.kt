package br.com.velhatech.state

import br.com.velhatech.components.fields.state.TextField
import br.com.velhatech.core.state.ILoadingUIState
import br.com.velhatech.core.state.MessageDialogState
import br.com.velhatech.firebase.auth.user.User

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