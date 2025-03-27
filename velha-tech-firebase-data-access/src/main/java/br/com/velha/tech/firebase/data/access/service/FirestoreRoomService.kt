package br.com.velha.tech.firebase.data.access.service

import android.util.Log
import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.RoomDocument
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

    fun addRoomPlayerListListener(
        roomId: String,
        onSuccess: (List<PlayerDocument>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        val playersQuery = db
            .collection(RoomDocument.COLLECTION_NAME)
            .document(roomId)
            .collection(PlayerDocument.COLLECTION_NAME)

        return playersQuery.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
                val players = value.documents.map { it.toObject(PlayerDocument::class.java)!! }
                onSuccess(players)
            }
        }
    }

    suspend fun findRoomById(roomId: String): RoomDocument? = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        roomDocumentRef.get().await().toObject(RoomDocument::class.java)
    }

    suspend fun removeAuthenticatedPlayerFromRoom(roomId: String): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)

        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
        val user = commonFirebaseAuthenticationService.getAuthenticatedUser()!!
        val playerQueryResults = playersCollectionRef.whereEqualTo(PlayerDocument::userId.name, user.id).get().await()
        val playerDelete = playerQueryResults.documents.map { it.reference }.first()

        db.runTransaction { transaction ->
            transaction.delete(playerDelete)
            roomDocument.playersCount--
            transaction.update(roomDocumentRef, roomDocument.toMap())
        }
    }

    suspend fun addAuthenticatedPlayerToRoom(roomId: String) = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)

        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
        val user = commonFirebaseAuthenticationService.getAuthenticatedUser()!!
        val player = playersCollectionRef.whereEqualTo(PlayerDocument::userId.name, user.id).get().await().firstOrNull()

        if (player == null) {
            db.runTransaction { transaction ->
                transaction.set(playersCollectionRef.document(), PlayerDocument(user.id, user.name))
                roomDocument.playersCount++
                transaction.update(roomDocumentRef, roomDocument.toMap())
            }
        }
    }

    suspend fun findPlayersFromRoom(roomId: String) = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)

        playersCollectionRef.get().await().documents.map { it.toObject(PlayerDocument::class.java)!! }
    }
}