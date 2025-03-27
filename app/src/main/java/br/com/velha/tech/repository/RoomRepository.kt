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
}
