package br.com.velhatech.screen.login.callback

fun interface OnLoginClick {
    fun onExecute(onSuccess: () -> Unit, onFailure: () -> Unit)
}