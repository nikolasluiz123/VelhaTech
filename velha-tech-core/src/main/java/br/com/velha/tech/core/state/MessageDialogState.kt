package br.com.velha.tech.core.state

import br.com.velha.tech.core.callback.IShowDialogCallback
import br.com.velha.tech.core.enums.EnumDialogType


data class MessageDialogState(
    val dialogType: EnumDialogType = EnumDialogType.ERROR,
    val dialogMessage: String = "",
    val showDialog: Boolean = false,
    val onShowDialog: IShowDialogCallback? = null,
    val onHideDialog: () -> Unit = { },
    val onConfirm: () -> Unit = { },
    val onCancel: () -> Unit = { }
)