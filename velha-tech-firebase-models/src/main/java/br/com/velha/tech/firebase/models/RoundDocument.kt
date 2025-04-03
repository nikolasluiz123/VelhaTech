package br.com.velha.tech.firebase.models

import java.util.UUID

data class RoundDocument(
    var id: String = UUID.randomUUID().toString(),
    var roundNumber: Int? = null,
    var winnerPlayerId: String? = null,
    var playing: Boolean = false,
    var finished: Boolean = false,
    var preparingToStart: Boolean = false
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "rounds"
    }
}