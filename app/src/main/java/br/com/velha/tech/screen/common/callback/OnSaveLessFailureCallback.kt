package br.com.velha.tech.screen.common.callback

fun interface OnSaveLessFailureCallback {
    fun onExecute(onSuccess: () -> Unit, onCompleted: () -> Unit)
}