package br.com.velha.tech.firebase.models

data class PlayerGamesHistory(
    var userId: String,
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "playerGamesHistory"
    }
}