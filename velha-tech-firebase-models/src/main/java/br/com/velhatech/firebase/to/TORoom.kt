package br.com.velhatech.firebase.to

import br.com.velhatech.firebase.enums.EnumDifficultLevel

data class TORoom(
    val id: String? = null,
    val roomName: String? = null,
    val roundsCount: Int? = null,
    val maxPlayers: Int? = null,
    val playersCount: Int? = null,
    val difficultLevel: EnumDifficultLevel? = null,
    val password: String? = null
) {
    fun isRequireAuth() = !password.isNullOrEmpty()
}
