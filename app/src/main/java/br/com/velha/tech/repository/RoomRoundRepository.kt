package br.com.velha.tech.repository

import br.com.velha.tech.firebase.data.access.service.FirestoreRoomRoundService
import br.com.velha.tech.firebase.models.RoomDocument
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORound
import br.com.velha.tech.firebase.to.TORoundTimer
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class RoomRoundRepository(
    private val firestoreRoomRoundService: FirestoreRoomRoundService
) {
    private var roundListenerRegistration: ListenerRegistration? = null
    private var roundTimerListenerRegistration: ListenerRegistration? = null

    suspend fun startNewRound(roomId: String, roundNumber: Int) {
        firestoreRoomRoundService.startRound(roomId, roundNumber)
    }

    suspend fun reduceRoundTimer(roomId: String, roundId: String) {
        firestoreRoomRoundService.reduceRoundTimer(roomId, roundId)
    }

    suspend fun getAllRoundsFinished(roomId: String): Boolean {
        return firestoreRoomRoundService.getAllRoundsFinished(roomId)
    }

    fun addRoundListener(
        roomId: String,
        onSuccess: (TORound) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (roundListenerRegistration == null) {
            roundListenerRegistration = firestoreRoomRoundService.addRoundListener(
                roomId = roomId,
                onSuccess = {
                    val result = it.toTORound()
                    onSuccess(result)
                },
                onError = onError
            )
        }
    }

    suspend fun addRoundTimerListener(
        roomId: String,
        roundId: String,
        onSuccess: (Int) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (roundTimerListenerRegistration == null) {
            roundTimerListenerRegistration = firestoreRoomRoundService.addRoundTimerListener(
                roomId = roomId,
                roundId = roundId,
                onSuccess = {
                    val result = it.toTORoundTimer()
                    onSuccess(result.timer!!)
                },
                onError = onError
            )
        }
    }

    suspend fun prepareNextRound(roomId: String, player: TOPlayer?) {
        firestoreRoomRoundService.prepareNextRound(roomId, player?.toPlayerDocument())
    }

    fun removeRoundListener() {
        roundListenerRegistration?.remove()
        roundListenerRegistration = null
    }

    fun removeRoundTimerListener() {
        roundTimerListenerRegistration?.remove()
        roundTimerListenerRegistration = null
    }
}