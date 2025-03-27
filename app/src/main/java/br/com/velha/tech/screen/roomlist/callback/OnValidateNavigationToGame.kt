package br.com.velha.tech.screen.roomlist.callback

fun interface OnValidateNavigationToGame {
    fun onExecute(roomId: String, onNavigate: () -> Unit)
}