package br.com.velha.tech.firebase.models

import java.util.UUID

data class RoundTimerDocument(
    var id: String = UUID.randomUUID().toString(),
    var timer: Int? = null
): FirestoreDocument() {

    companion object {
        const val COLLECTION_NAME = "roundTimers"
    }
}
