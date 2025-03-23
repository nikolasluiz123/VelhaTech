package br.com.velhatech.firebase.to

import br.com.velhatech.firebase.enums.EnumDifficultLevel

data class TORoom(
    val id: String? = null,
    var roomName: String? = null,
    var roundsCount: Int? = null,
    var maxPlayers: Int = 2,
    var playersCount: Int = 1,
    var difficultLevel: EnumDifficultLevel? = null,
    var password: String? = null
) {
    fun isRequireAuth() = !password.isNullOrEmpty()
}
