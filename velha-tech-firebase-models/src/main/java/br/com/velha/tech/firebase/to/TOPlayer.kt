package br.com.velha.tech.firebase.to

data class TOPlayer(
    val userId: String,
    val name: String,
    val roomOwner: Boolean,
    val figure: Int? = null
)