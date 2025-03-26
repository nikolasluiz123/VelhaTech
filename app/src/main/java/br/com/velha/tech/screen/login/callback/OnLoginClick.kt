package br.com.velha.tech.screen.login.callback

fun interface OnLoginClick {
    fun onExecute(onSuccess: () -> Unit, onFailure: () -> Unit)
}