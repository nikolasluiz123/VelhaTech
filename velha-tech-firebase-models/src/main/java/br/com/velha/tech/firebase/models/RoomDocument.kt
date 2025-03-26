package br.com.velha.tech.firebase.models

import java.util.UUID

data class RoomDocument(
    var id: String = UUID.randomUUID().toString(),
    val roomName: String? = null,
    val roundsCount: Int? = null,
    val players: MutableList<PlayerDocument> = mutableListOf(),
    var playersCount: Int = 0,
    val difficultLevel: String? = null,
    val password: String? = null,
    var creationDate: Long? = null
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "rooms"
    }
}
