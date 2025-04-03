package br.com.velha.tech.repository

import br.com.velha.tech.core.R
import br.com.velha.tech.core.enums.EnumDateTimePatterns
import br.com.velha.tech.core.extensions.parseToLocalTime
import br.com.velha.tech.firebase.data.access.service.FirestoreRoomPlayersService
import br.com.velha.tech.firebase.to.TOPlayer
import com.google.firebase.firestore.ListenerRegistration
import java.time.LocalTime

class RoomPlayersRepository(
    private val firestoreRoomPlayersService: FirestoreRoomPlayersService,
) {
    private var roomPlayersListListener: ListenerRegistration? = null
    private var playerListener: ListenerRegistration? = null
    private var playerTimerListener: ListenerRegistration? = null

    fun addRoomPlayerListListener(
        roomId: String,
        onSuccess: (List<TOPlayer>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (roomPlayersListListener == null) {
            roomPlayersListListener = firestoreRoomPlayersService.addRoomPlayerListListener(
                roomId = roomId,
                onSuccess = {
                    val result = it.map { it.toTOPlayer() }
                    onSuccess(result)
                },
                onError = onError
            )
        }
    }

    fun addPlayerListener(
        roomId: String,
        playerId: String,
        onSuccess: (TOPlayer) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (playerListener == null) {
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
    }

    fun addPlayerTimerListener(
        roomId: String,
        playerId: String,
        onSuccess: (LocalTime) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (playerTimerListener == null) {
            playerTimerListener = firestoreRoomPlayersService.addPlayerTimerListener(
                roomId = roomId,
                playerId = playerId,
                onSuccess = {
                    onSuccess(it.timer?.parseToLocalTime(EnumDateTimePatterns.TIME_WITH_SECONDS)!!)
                },
                onError = onError
            )
        }
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

    suspend fun reducePlayerTimer(roomId: String, playerId: String) {
        firestoreRoomPlayersService.reducePlayerTimer(roomId, playerId)
    }

    fun removeRoomPlayerListListener() {
        roomPlayersListListener?.remove()
        roomPlayersListListener = null
    }

    fun removePlayerListener() {
        playerListener?.remove()
        playerListener = null
    }

    fun removePlayerTimerListener() {
        playerTimerListener?.remove()
        playerTimerListener = null
    }
}