package br.com.velha.tech.firebase.models

import java.util.UUID

data class GameBoardDocument(
    val id: String = UUID.randomUUID().toString(),
    val matrix: Map<String, Map<String, String>> = emptyMap()
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "gameBoards"
    }
}
