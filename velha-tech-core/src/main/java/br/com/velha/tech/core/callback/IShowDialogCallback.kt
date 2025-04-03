package br.com.velha.tech.core.callback

import br.com.velha.tech.core.enums.EnumDialogType


fun interface IShowDialogCallback {
    fun onShow(type: EnumDialogType, message: String, onConfirm: () -> Unit, onCancel: () -> Unit, customTitle: String?)
}

fun IShowDialogCallback.showCustomDialog(
    type: EnumDialogType,
    customTitle: String,
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    this.onShow(
        type = type,
        message = message,
        onConfirm = onConfirm,
        onCancel = onCancel,
        customTitle = customTitle
    )
}

fun IShowDialogCallback.showErrorDialog(message: String) {
    this.onShow(
        type = EnumDialogType.ERROR,
        message = message,
        onConfirm = { },
        onCancel = { },
        customTitle = null
    )
}

fun IShowDialogCallback.showConfirmationDialog(message: String, onConfirm: () -> Unit) {
    this.onShow(
        type = EnumDialogType.CONFIRMATION,
        message = message,
        onConfirm = onConfirm,
        onCancel = { },
        customTitle = null
    )
}

fun IShowDialogCallback.showInformationDialog(message: String) {
    this.onShow(
        type = EnumDialogType.INFORMATION,
        message = message,
        onConfirm = { },
        onCancel = { },
        customTitle = null
    )
}