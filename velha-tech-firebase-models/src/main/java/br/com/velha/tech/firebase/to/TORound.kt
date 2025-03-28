package br.com.velha.tech.firebase.to

data class TORound(
    var id: String? = null,
    var roundNumber: Int? = null,
    var winnerName: String? = null,
    var preparingToStart: Boolean = false,
    var timerToStart: Int? = null,
    var started: Boolean = false,
)
