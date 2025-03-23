package br.com.velhatech.firebase.to

import br.com.velhatech.firebase.enums.EnumDifficultLevel

data class TORoom(
    val id: String? = null,
    var roomName: String? = null,
    var roundsCount: Int? = null,
    var maxPlayers: Int? = null,
    var playersCount: Int? = null,
    var difficultLevel: EnumDifficultLevel? = null,
    var password: String? = null
) {
    fun isRequireAuth() = !password.isNullOrEmpty()
}
