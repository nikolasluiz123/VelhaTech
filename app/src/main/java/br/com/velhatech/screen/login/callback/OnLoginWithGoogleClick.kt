package br.com.velhatech.screen.login.callback

fun interface OnLoginWithGoogleClick {
    fun onExecute(onSuccess: () -> Unit, onFailure: () -> Unit)
}