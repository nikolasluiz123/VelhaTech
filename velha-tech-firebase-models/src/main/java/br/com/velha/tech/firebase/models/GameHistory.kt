package br.com.velha.tech.firebase.models

import java.util.UUID

data class GameHistory(
    val id: String = UUID.randomUUID().toString(),
    val dateGameEnd: Long? = null,
    val roomName: String? = null,
    val roundsCount: Int? = null,
    val difficultLevel: String? = null,
    val adversaryName: String? = null,
    val gameStatus: String? = null,
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "gameHistory"
    }
}