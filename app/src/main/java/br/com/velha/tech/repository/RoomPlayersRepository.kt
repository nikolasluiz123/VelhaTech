package br.com.velha.tech.repository

import br.com.velha.tech.core.R
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomPlayersService
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.to.TOPlayer
import com.google.firebase.firestore.ListenerRegistration

class RoomPlayersRepository(
    private val firestoreRoomPlayersService: FirestoreRoomPlayersService,
) {
    private var roomPlayersListListener: ListenerRegistration? = null
    private var playerListener: ListenerRegistration? = null

    fun addRoomPlayerListListener(
        roomId: String,
        onSuccess: (List<TOPlayer>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        roomPlayersListListener = firestoreRoomPlayersService.addRoomPlayerListListener(
            roomId = roomId,
            onSuccess = {
                val result = it.map { it.toTOPlayer() }
                onSuccess(result)
            },
            onError = onError
        )
    }

    fun addPlayerListener(
        roomId: String,
        playerId: String,
        onSuccess: (TOPlayer) -> Unit,
        onError: (Exception) -> Unit
    ) {
        playerListener = firestoreRoomPlayersService.addPlayerListener(
            roomId = roomId,
            playerId = playerId,
            onSuccess = {
                val result = it.toTOPlayer()
                onSuccess(result)
            },
            onError = onError
        )
    }

    suspend fun addAuthenticatedPlayerToRoom(roomId: String) {
        firestoreRoomPlayersService.addAuthenticatedPlayerToRoom(roomId)
    }

    suspend fun removePlayerFromRoom(roomId: String) {
        firestoreRoomPlayersService.removeAuthenticatedPlayerFromRoom(roomId)
    }

    suspend fun findPlayersFromRoom(roomId: String): List<TOPlayer> {
        return firestoreRoomPlayersService.findPlayersFromRoom(roomId).map { it.toTOPlayer() }
    }

    suspend fun sortFiguresToPlayers(roomId: String): List<TOPlayer> {
        val figures = listOf(R.drawable.ic_x, R.drawable.ic_elipse)

        return firestoreRoomPlayersService.sortFiguresToPlayers(roomId, figures).map {
            it.toTOPlayer()
        }
    }

    suspend fun selectPlayerToPlay(roomId: String) {
        return firestoreRoomPlayersService.selectPlayerToPlay(roomId)
    }

    fun removeRoomPlayerListListener() {
        roomPlayersListListener?.remove()
    }

    fun removePlayerListener() {
        playerListener?.remove()
    }
}