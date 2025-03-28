package br.com.velha.tech.firebase.models

import java.util.UUID

data class RoundDocument(
    var id: String = UUID.randomUUID().toString(),
    var roundNumber: Int? = null,
    var winnerName: String? = null,
    var preparingToStart: Boolean = false,
    var timerToStart: Int? = null,
    var started: Boolean = false,
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "rounds"
    }
}