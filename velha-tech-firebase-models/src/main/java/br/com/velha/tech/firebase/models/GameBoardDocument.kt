package br.com.velha.tech.firebase.models

import java.util.UUID

data class GameBoardDocument(
    val id: String = UUID.randomUUID().toString(),
    var matrix:  List<Map<String, Int>> = emptyList()
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "gameBoards"
    }
}
