package br.com.velhatech.firebase.data.access.service

import br.com.velhatech.firebase.models.RoomDocument
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.jvm.java

class FirestoreRoomService: FirestoreService() {

    suspend fun saveRoom(room: RoomDocument): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(room.id)
        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)

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
        val roomsQuery = db.collection(RoomDocument.COLLECTION_NAME)

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
}