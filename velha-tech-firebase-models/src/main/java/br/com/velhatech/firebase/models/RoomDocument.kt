package br.com.velhatech.firebase.models

import java.util.UUID

data class RoomDocument(
    var id: String = UUID.randomUUID().toString(),
    val roomName: String? = null,
    val roundsCount: Int? = null,
    val maxPlayers: Int = 2,
    val players: MutableList<PlayerDocument> = mutableListOf(),
    val difficultLevel: String? = null,
    val password: String? = null,
    var creationDate: Long? = null
) {

    companion object {
        const val COLLECTION_NAME = "rooms"
    }
}
