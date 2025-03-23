package br.com.velhatech.screen.common.callback

fun interface OnSaveCallback {
    fun onExecute(onSuccess: () -> Unit, onFailure: () -> Unit, onCompleted: () -> Unit)
}