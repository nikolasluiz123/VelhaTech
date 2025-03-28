package br.com.velha.tech.repository

import android.util.Log
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomRoundService
import br.com.velha.tech.firebase.models.RoundDocument
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORound
import com.google.firebase.firestore.ListenerRegistration

class RoomRoundRepository(
    private val firestoreRoomRoundService: FirestoreRoomRoundService
) {
    private var roundListenerRegistration: ListenerRegistration? = null

    suspend fun startNewRound(roomId: String, roundNumber: Int) {
        val document = RoundDocument(
            roundNumber = roundNumber,
            preparingToStart = true,
            timerToStart = 10
        )

        firestoreRoomRoundService.startRound(roomId, document)
        Log.i("Teste", "startNewRound: document = $document")
    }

    suspend fun reduceRoundTimer(roomId: String, roundId: String) {
        firestoreRoomRoundService.reduceRoundTimer(roomId, roundId)
    }

    fun addRoundListener(
        roomId: String,
        onSuccess: (TORound) -> Unit,
        onError: (Exception) -> Unit
    ) {
        roundListenerRegistration = firestoreRoomRoundService.addRoundListener(
            roomId = roomId,
            onSuccess = {
                val result = it.toTORound()
                onSuccess(result)
            },
            onError = onError
        )
    }

    fun removeRoundListener() {
        roundListenerRegistration?.remove()
    }
}