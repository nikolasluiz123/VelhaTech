package br.com.velha.tech.screen.login.callback

fun interface OnLoginWithGoogleClick {
    fun onExecute(onSuccess: () -> Unit, onFailure: () -> Unit)
}