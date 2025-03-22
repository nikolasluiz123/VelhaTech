package br.com.velhatech.core.state

import br.com.velhatech.core.callback.IShowDialogCallback
import br.com.velhatech.core.enums.EnumDialogType


data class MessageDialogState(
    val dialogType: EnumDialogType = EnumDialogType.ERROR,
    val dialogMessage: String = "",
    val showDialog: Boolean = false,
    val onShowDialog: IShowDialogCallback? = null,
    val onHideDialog: () -> Unit = { },
    val onConfirm: () -> Unit = { },
    val onCancel: () -> Unit = { }
)