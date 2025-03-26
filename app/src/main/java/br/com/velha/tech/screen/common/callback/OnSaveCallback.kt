package br.com.velha.tech.screen.common.callback

fun interface OnSaveCallback {
    fun onExecute(onSuccess: () -> Unit, onFailure: () -> Unit, onCompleted: () -> Unit)
}