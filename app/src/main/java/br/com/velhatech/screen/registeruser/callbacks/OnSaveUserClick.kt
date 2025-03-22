package br.com.velhatech.screen.registeruser.callbacks

fun interface OnSaveUserClick {
    fun onExecute(onSaved: () -> Unit)
}