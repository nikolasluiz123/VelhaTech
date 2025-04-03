package br.com.velha.tech.firebase.data.access.service

import android.util.Log
import br.com.velha.tech.core.enums.EnumDateTimePatterns.TIME_WITH_SECONDS
import br.com.velha.tech.core.extensions.format
import br.com.velha.tech.firebase.models.GameBoardDocument
import br.com.velha.tech.firebase.models.PlayerDocument
import br.com.velha.tech.firebase.models.PlayerTimerDocument
import br.com.velha.tech.firebase.models.RoomDocument
import br.com.velha.tech.firebase.models.RoundDocument
import br.com.velha.tech.firebase.models.RoundTimerDocument
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRoomRoundService(
    private val roomService: FirestoreRoomService
) : FirestoreService() {

    suspend fun startRound(roomId: String, roundNumber: Int): Unit = withContext(IO) {
        val roundDocument = RoundDocument(roundNumber = roundNumber, preparingToStart = true)
        val roundTimerDocument = RoundTimerDocument(timer = 5)

        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roundDocumentRef = roomDocumentRef.collection(RoundDocument.COLLECTION_NAME).document(roundDocument.id)
        val roundTimerDocumentRef = roundDocumentRef.collection(RoundTimerDocument.COLLECTION_NAME).document(roundTimerDocument.id)
        val gameBoardDocument = GameBoardDocument(matrix = getEmptyGameBoardMatrix())
        val gameBoardDocumentRef = roundDocumentRef.collection(GameBoardDocument.COLLECTION_NAME).document(gameBoardDocument.id)

        db.runTransaction { transaction ->
            transaction.set(roundTimerDocumentRef, roundTimerDocument)
            transaction.set(roundDocumentRef, roundDocument)
            transaction.set(gameBoardDocumentRef, gameBoardDocument)
        }.await()
    }

    private fun getEmptyGameBoardMatrix(): List<Map<String, Int>> = listOf(
        mapOf("0" to 0, "1" to 0, "2" to 0),
        mapOf("0" to 0, "1" to 0, "2" to 0),
        mapOf("0" to 0, "1" to 0, "2" to 0)
    )

    suspend fun reduceRoundTimer(roomId: String, roundId: String): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roundDocumentRef = roomDocumentRef.collection(RoundDocument.COLLECTION_NAME).document(roundId)
        val roundDocument = roundDocumentRef.get().await().toObject(RoundDocument::class.java)!!
        val roundTimerDocumentRef = roundDocumentRef.collection(RoundTimerDocument.COLLECTION_NAME).get().await().first().reference
        val roundTimerDocument = roundTimerDocumentRef.get().await().toObject(RoundTimerDocument::class.java)!!

        if (roundTimerDocument.timer!! > 0) {
            roundTimerDocument.timer = roundTimerDocument.timer!! - 1

            delay(1000)
            roundTimerDocumentRef.update(roundTimerDocument.toMap()).await()
            reduceRoundTimer(roomId, roundId)
        } else {
            roundDocument.playing = true
            roundDocument.preparingToStart = false
            roundDocumentRef.update(roundDocument.toMap()).await()
        }
    }

    suspend fun getAllRoundsFinished(roomId: String): Boolean = withContext(IO) {
        val rounds = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
            .collection(RoundDocument.COLLECTION_NAME)
            .get()
            .await()
            .documents.map {
                it.reference.get().await().toObject(RoundDocument::class.java)!!
            }

        rounds.all { it.finished }
    }

    suspend fun getPlayingRoundDocumentRef(roomId: String): DocumentReference? = withContext(IO) {
        val playingQuery = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
            .collection(RoundDocument.COLLECTION_NAME)
            .whereEqualTo(RoundDocument::playing.name, true)
            .limit(1)
            .get()
            .await()

        playingQuery.documents.firstOrNull()?.reference
    }

    fun addRoundListener(
        roomId: String,
        onSuccess: (RoundDocument) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        val query = db.collection(RoomDocument.COLLECTION_NAME)
            .document(roomId)
            .collection(RoundDocument.COLLECTION_NAME)
            .orderBy(RoundDocument::roundNumber.name)
            .limitToLast(1)

        return query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
                value.documents.firstOrNull()?.toObject(RoundDocument::class.java)?.let { document ->
                    onSuccess(document)
                }
            }
        }
    }

    suspend fun addRoundTimerListener(
        roomId: String,
        roundId: String,
        onSuccess: (RoundTimerDocument) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration? = withContext(IO) {
        val roundTimerDocumentRef = db
            .collection(RoomDocument.COLLECTION_NAME).document(roomId)
            .collection(RoundDocument.COLLECTION_NAME).document(roundId)
            .collection(RoundTimerDocument.COLLECTION_NAME)
            .get().await().firstOrNull()?.reference

        roundTimerDocumentRef?.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {
                value.toObject(RoundTimerDocument::class.java)?.let { document ->
                    onSuccess(document)
                }
            }
        }
    }

    suspend fun prepareNextRound(roomId: String, playerWinner: PlayerDocument?): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roomDocument = roomDocumentRef.get().await().toObject(RoomDocument::class.java)!!
        val roundDocumentRef = getPlayingRoundDocumentRef(roomId)!!
        val roundDocument = roundDocumentRef.get().await().toObject(RoundDocument::class.java)!!.apply {
            playing = false
            finished = true
            winnerPlayerId = playerWinner?.userId
        }

        val newRoundDocument = RoundDocument(
            roundNumber = roundDocument.roundNumber!! + 1,
            preparingToStart = true
        )
        val newRoundTimerDocument = RoundTimerDocument(timer = 5)

        val newRoundDocumentRef = roomDocumentRef.collection(RoundDocument.COLLECTION_NAME).document(newRoundDocument.id)
        val newRoundTimerDocumentRef = newRoundDocumentRef.collection(RoundTimerDocument.COLLECTION_NAME).document(newRoundTimerDocument.id)
        val gameBoardDocument = GameBoardDocument(matrix = getEmptyGameBoardMatrix())
        val gameBoardDocumentRef = newRoundDocumentRef.collection(GameBoardDocument.COLLECTION_NAME).document(gameBoardDocument.id)

        val playerTimer = roomService.getTimeForDifficultLevel(roomDocument.difficultLevel!!).format(TIME_WITH_SECONDS)
        val playersDocumentsRef = roomDocumentRef.collection(PlayerDocument.COLLECTION_NAME).get().await().documents.map { it.reference }
        val playersTimerRef = playersDocumentsRef.map {
            it.collection(PlayerTimerDocument.COLLECTION_NAME).get().await().documents.first().reference
        }

        db.runTransaction { transaction ->
            transaction.update(roundDocumentRef, roundDocument.toMap())

            transaction.set(newRoundTimerDocumentRef, newRoundTimerDocument)
            transaction.set(newRoundDocumentRef, newRoundDocument)
            transaction.set(gameBoardDocumentRef, gameBoardDocument)

            playersDocumentsRef.forEach { playerDocumentRef ->
                transaction.update(playerDocumentRef, PlayerDocument::playing.name, false)
            }

            playersTimerRef.forEach { playerTimerDocumentRef ->
                transaction.update(playerTimerDocumentRef, PlayerTimerDocument::timer.name, playerTimer)
            }
        }.await()
    }

}