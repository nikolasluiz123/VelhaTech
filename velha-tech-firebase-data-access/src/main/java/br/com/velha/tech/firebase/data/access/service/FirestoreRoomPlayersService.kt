package br.com.velha.tech.firebase.data.access.service

import br.com.velha.tech.core.enums.EnumDateTimePatterns.TIME_WITH_SECONDS
import br.com.velha.tech.core.extensions.format
import br.com.velha.tech.core.extensions.parseToLocalTime
import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.PlayerTimerDocument
import br.com.velha.tech.firebase.models.RoomDocument
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.concurrent.timer

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
        val user = commonFirebaseAuthenticationService.getAuthenticatedUser()!!

        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)
        val playerTimerCollectionRef = playersCollectionRef.document(user.id!!).collection(PlayerTimerDocument.COLLECTION_NAME)
        val oldPlayersCount = playersCollectionRef.get().await().size()

        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
        val player = playersCollectionRef.whereEqualTo(PlayerDocument::userId.name, user.id).get().await().firstOrNull()

        if (player == null) {
            db.runTransaction { transaction ->
                val playerDocument = PlayerDocument(
                    userId = user.id,
                    name = user.name,
                    roomOwner = roomDocument.playersCount == 0,
                )

                val playerTimer = PlayerTimerDocument(
                    timer = roomService.getTimeForDifficultLevel(roomDocument.difficultLevel!!).format(TIME_WITH_SECONDS)
                )

                transaction.set(playersCollectionRef.document(user.id!!), playerDocument)
                transaction.set(playerTimerCollectionRef.document(playerTimer.id), playerTimer)
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

    suspend fun selectPlayerToPlay(roomId: String): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playersCollectionRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)
        val documents = playersCollectionRef.get().await().documents
        val players = documents.map { it.toObject(PlayerDocument::class.java)!! }

        if (players.none { it.playing }) {
            players.random().playing = true
        } else {
            players.forEach {
                it.playing = !it.playing
            }
        }

        db.runTransaction { transaction ->
            players.forEach {
                transaction.update(
                    playersCollectionRef.document(it.userId!!),
                    it.toMap()
                )
            }
        }.await()
    }

    fun addPlayerListener(
        roomId: String,
        playerId: String,
        onSuccess: (PlayerDocument) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        val playerDocumentRef = db
            .collection(RoomDocument.COLLECTION_NAME)
            .document(roomId)
            .collection(PlayerDocument.COLLECTION_NAME)
            .document(playerId)

        return playerDocumentRef.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {
                val player = value.toObject(PlayerDocument::class.java)!!
                onSuccess(player)
            }
        }
    }

    fun addPlayerTimerListener(
        roomId: String,
        playerId: String,
        onSuccess: (PlayerTimerDocument) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        val playerTimerQuery = db
            .collection(RoomDocument.COLLECTION_NAME)
            .document(roomId)
            .collection(PlayerDocument.COLLECTION_NAME)
            .document(playerId)
            .collection(PlayerTimerDocument.COLLECTION_NAME)
            .limit(1)

        return playerTimerQuery.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
                val playerTimer = value.documents.first().toObject(PlayerTimerDocument::class.java)!!
                onSuccess(playerTimer)
            }
        }
    }

    suspend fun reducePlayerTimer(roomId: String, playerId: String): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val playerDocumentRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME).document(playerId)
        val playerDocument = playerDocumentRef.get().await().toObject(PlayerDocument::class.java)!!

        if (playerDocument.playing) {
            val playerTimerQuery = playerDocumentRef.collection(PlayerTimerDocument.COLLECTION_NAME).limit(1).get().await()
            val playerTimerDocumentRef = playerTimerQuery.documents.first().reference
            val playerTimerDocument = playerTimerDocumentRef.get().await().toObject(PlayerTimerDocument::class.java)!!

            val timer = playerTimerDocument.timer?.parseToLocalTime(TIME_WITH_SECONDS)!!

            if (timer.second > 0) {
                playerTimerDocument.timer = timer.minusSeconds(1).format(TIME_WITH_SECONDS)

                delay(1000)
                playerTimerDocumentRef.update(playerTimerDocument.toMap()).await()
            } else {
                val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
                val startTime = roomService.getTimeForDifficultLevel(roomDocument.difficultLevel!!).format(TIME_WITH_SECONDS)

                playerTimerDocument.timer = startTime
                playerTimerDocumentRef.update(playerTimerDocument.toMap()).await()
                selectPlayerToPlay(roomId)
            }

            reducePlayerTimer(roomId, playerId)
        }
    }
}