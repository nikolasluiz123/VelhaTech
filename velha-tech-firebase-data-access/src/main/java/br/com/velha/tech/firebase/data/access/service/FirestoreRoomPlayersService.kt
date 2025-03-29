package br.com.velha.tech.firebase.data.access.service

import br.com.velha.tech.core.enums.EnumDateTimePatterns
import br.com.velha.tech.core.extensions.format
import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.RoomDocument
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRoomPlayersService(
    private val commonFirebaseAuthenticationService: CommonFirebaseAuthenticationService,
    private val roomService: FirestoreRoomService
): FirestoreService() {

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
        }.await()
    }

    suspend fun addAuthenticatedPlayerToRoom(roomId: String) = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)
        val oldPlayersCount = playersCollectionRef.get().await().size()

        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
        val user = commonFirebaseAuthenticationService.getAuthenticatedUser()!!
        val player = playersCollectionRef.whereEqualTo(PlayerDocument::userId.name, user.id).get().await().firstOrNull()

        if (player == null) {
            db.runTransaction { transaction ->
                val playerDocument = PlayerDocument(
                    userId = user.id,
                    name = user.name,
                    roomOwner = roomDocument.playersCount == 0,
                    timer = roomService.getTimeForDifficultLevel(roomDocument.difficultLevel!!).format(EnumDateTimePatterns.TIME_WITH_SECONDS)
                )

                transaction.set(playersCollectionRef.document(user.id!!), playerDocument)
                roomDocument.playersCount = oldPlayersCount + 1
                transaction.update(roomDocumentRef, roomDocument.toMap())
            }.await()
        }
    }

    suspend fun findPlayersFromRoom(roomId: String) = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)

        playersCollectionRef.get().await().documents.map { it.toObject(PlayerDocument::class.java)!! }
    }

    suspend fun sortFiguresToPlayers(roomId: String, figures: List<Int>): List<PlayerDocument> = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)
        val documents = playersCollectionRef.get().await().documents
        val players = documents.map { it.toObject(PlayerDocument::class.java)!! }

        db.runTransaction { transaction ->
            players.forEachIndexed { index, playerDocument ->
                playerDocument.figure = figures[index]

                transaction.update(
                    playersCollectionRef.document(playerDocument.userId!!),
                    playerDocument.toMap()
                )
            }
        }.await()

        players
    }
}