package br.com.velha.tech.firebase.data.access.service

import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.enums.EnumGameStatus
import br.com.velha.tech.firebase.models.GameHistory
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.PlayerGamesHistory
import br.com.velha.tech.firebase.models.RoomDocument
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalTime

class FirestoreRoomService(): FirestoreService() {

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

    fun getTimeForDifficultLevel(levelValue: String): LocalTime {
        val level = EnumDifficultLevel.valueOf(levelValue)

        return when (level) {
            EnumDifficultLevel.EASY -> LocalTime.of(0, 0, 10)
            EnumDifficultLevel.MEDIUM -> LocalTime.of(0, 0, 5)
            EnumDifficultLevel.HARD -> LocalTime.of(0, 0, 2)
        }
    }

    suspend fun finishGame(roomId: String, winnerUserId: String?): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!

        val playersCollection = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME)
        val players = playersCollection.get().await().documents.map { it.toObject(PlayerDocument::class.java)!! }

        val playerGamesHistoryList = players.map { PlayerGamesHistory(userId = it.userId!!) }
        val playerGamesHistoryRefList = players.map { db.collection(PlayerGamesHistory.COLLECTION_NAME).document(it.userId!!) }

        val gameHistoryDocumentList = players.map { playerDocument ->
            val adversary = players.first { it.userId != playerDocument.userId }
            val gameStatus = when (playerDocument.userId) {
                winnerUserId -> EnumGameStatus.VICTORY.name
                adversary.userId -> EnumGameStatus.LOSE.name
                else -> EnumGameStatus.DRAW.name
            }

            GameHistory(
                dateGameEnd = getServerTime(),
                roomName = roomDocument.roomName,
                roundsCount = roomDocument.roundsCount,
                difficultLevel = roomDocument.difficultLevel,
                adversaryName = adversary.name,
                gameStatus = gameStatus,
            )
        }

        val gameHistoryDocumentRefList = gameHistoryDocumentList.mapIndexed { index, gameHistory ->
            playerGamesHistoryRefList[index].collection(GameHistory.COLLECTION_NAME).document(gameHistory.id)
        }

        db.runTransaction { transaction ->
            playerGamesHistoryRefList.forEachIndexed { index, playerGamesHistoryRef ->
                transaction.set(playerGamesHistoryRef, playerGamesHistoryList[index])
            }

            gameHistoryDocumentRefList.forEachIndexed { index, gameHistoryDocumentRef ->
                transaction.set(gameHistoryDocumentRef, gameHistoryDocumentList[index])
            }

            transaction.delete(roomDocumentRef)
        }.await()
    }

}