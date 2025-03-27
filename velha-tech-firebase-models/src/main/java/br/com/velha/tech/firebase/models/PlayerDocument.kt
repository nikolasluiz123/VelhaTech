package br.com.velha.tech.firebase.models

data class PlayerDocument(
    val userId: String? = null,
    val name: String? = null,
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "players"
    }
}