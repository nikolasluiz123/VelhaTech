package br.com.velha.tech.firebase.to

import br.com.velha.tech.firebase.enums.EnumDifficultLevel

data class TORoom(
    var id: String? = null,
    var roomName: String? = null,
    var roundsCount: Int? = null,
    var players: MutableList<TOPlayer> = mutableListOf(),
    var playersCount: Int = 0,
    var difficultLevel: EnumDifficultLevel? = null,
    var password: String? = null
) {
    fun isRequireAuth() = !password.isNullOrEmpty()
}
