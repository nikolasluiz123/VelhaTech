package br.com.velhatech.screen.common.callback

fun interface OnSaveLessFailureCallback {
    fun onExecute(onSuccess: () -> Unit, onCompleted: () -> Unit)
}