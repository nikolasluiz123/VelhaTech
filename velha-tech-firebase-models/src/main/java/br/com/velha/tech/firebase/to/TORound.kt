package br.com.velha.tech.firebase.to

data class TORound(
    var id: String? = null,
    var roundNumber: Int? = null,
    var winnerPlayerId: String? = null,
    var playing: Boolean = false,
    var finished: Boolean = false,
    var preparingToStart: Boolean = false
)
