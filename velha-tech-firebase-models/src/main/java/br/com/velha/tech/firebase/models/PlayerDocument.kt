package br.com.velha.tech.firebase.models

data class PlayerDocument(
    val userId: String? = null,
    val name: String? = null,
    val roomOwner: Boolean = false,
    var figure: Int? = null,
    var timer: String? = null,
    var playing: Boolean = false,
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "players"
    }
}