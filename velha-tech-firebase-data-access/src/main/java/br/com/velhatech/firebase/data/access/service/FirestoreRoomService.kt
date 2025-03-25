package br.com.velhatech.firebase.data.access.service

import br.com.velhatech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velhatech.firebase.models.PlayerDocument
import br.com.velhatech.firebase.models.RoomDocument
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.jvm.java

class FirestoreRoomService(
    private val commonFirebaseAuthenticationService: CommonFirebaseAuthenticationService
): FirestoreService() {

    suspend fun saveRoom(room: RoomDocument): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(room.id)
        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)

        room.creationDate = getServerTime()

        if (roomDocument == null) {
            roomDocumentRef.set(room).await()
        } else {
            roomDocumentRef.set(room).await()
        }
    }

    fun addRoomListListener(
        onSuccess: (List<RoomDocument>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        val roomsQuery = db
            .collection(RoomDocument.COLLECTION_NAME)
            .orderBy(RoomDocument::creationDate.name, Query.Direction.DESCENDING)

        return roomsQuery.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
                val chats = value.documents.map { it.toObject(RoomDocument::class.java)!! }
                onSuccess(chats)
            }
        }
    }

    suspend fun findRoomById(roomId: String): RoomDocument? = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        roomDocumentRef.get().await().toObject(RoomDocument::class.java)
    }

    suspend fun removeAuthenticatedPlayerFromRoom(roomId: String) = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
        val user = commonFirebaseAuthenticationService.getAuthenticatedUser()!!

        roomDocument.players.removeIf { it.userId == user.id }

        roomDocumentRef.update(RoomDocument::players.name, roomDocument.players)
    }

    suspend fun addAuthenticatedPlayerToRoom(roomId: String) = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
        val user = commonFirebaseAuthenticationService.getAuthenticatedUser()!!

        if (roomDocument.players.none { it.userId == user.id }) {
            roomDocument.players.add(PlayerDocument(user.id!!, user.name!!))
        }

        roomDocumentRef.update(RoomDocument::players.name, roomDocument.players)
    }
}