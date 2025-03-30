package br.com.velha.tech.firebase.models

import java.util.UUID

data class PlayerTimerDocument(
    var id: String = UUID.randomUUID().toString(),
    var timer: String? = null,
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "playerTimers"
    }
}
