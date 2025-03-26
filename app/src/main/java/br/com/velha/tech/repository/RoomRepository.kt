package br.com.velha.tech.repository

import br.com.velha.tech.firebase.data.access.service.FirestoreRoomService
import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.RoomDocument
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORoom
import com.google.firebase.firestore.ListenerRegistration

class RoomRepository(
    private val firestoreRoomService: FirestoreRoomService,
) {
    private var roomListListener: ListenerRegistration? = null

    suspend fun saveRoom(toRoom: TORoom) {
        val room = toRoom.toRoomDocument()
        toRoom.id = room.id

        firestoreRoomService.saveRoom(room)
    }

    fun addRoomListListener(
        onSuccess: (List<TORoom>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        roomListListener = firestoreRoomService.addRoomListListener(
            onSuccess = {
                val result = it.map { it.toTORoom() }
                onSuccess(result)
            },
            onError = onError
        )
    }

    suspend fun findRoomById(roomId: String): TORoom? {
        return firestoreRoomService.findRoomById(roomId)?.toTORoom()
    }

    fun removeRoomListListener() {
        roomListListener?.remove()
    }

    suspend fun addAuthenticatedPlayerToRoom(roomId: String) {
        firestoreRoomService.addAuthenticatedPlayerToRoom(roomId)
    }

    suspend fun removePlayerFromRoom(roomId: String) {
        firestoreRoomService.removeAuthenticatedPlayerFromRoom(roomId)
    }
}

fun TORoom.toRoomDocument(): RoomDocument {
    val document = RoomDocument(
        roomName = roomName,
        roundsCount = roundsCount,
        players = players.map { it.toPlayerDocument() }.toMutableList(),
        difficultLevel = difficultLevel?.name,
        password = password,
        playersCount = playersCount
    )

    id?.let { document.id = it }

    return document
}

fun RoomDocument.toTORoom(): TORoom {
    return TORoom(
        id = id,
        roomName = roomName,
        roundsCount = roundsCount,
        players = players.map { it.toTOPlayer() }.toMutableList(),
        difficultLevel = EnumDifficultLevel.valueOf(difficultLevel!!),
        password = password,
        playersCount = playersCount,
    )
}

fun PlayerDocument.toTOPlayer(): TOPlayer {
    return TOPlayer(
        userId = userId!!,
        name = name!!
    )
}

fun TOPlayer.toPlayerDocument(): PlayerDocument {
    return PlayerDocument(
        userId = userId,
        name = name
    )
}
