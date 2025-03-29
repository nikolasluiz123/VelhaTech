package br.com.velha.tech.firebase.data.access.service

import br.com.velha.tech.firebase.models.GameBoardDocument
import br.com.velha.tech.firebase.models.RoomDocument
import br.com.velha.tech.firebase.models.RoundDocument
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRoomRoundService : FirestoreService() {

    suspend fun startRound(roomId: String, roundDocument: RoundDocument): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roundDocumentRef = roomDocumentRef.collection(RoundDocument.COLLECTION_NAME).document(roundDocument.id)
        val gameBoardDocument = GameBoardDocument(
            matrix = listOf(
                mapOf("0" to 0, "1" to 0, "2" to 0),
                mapOf("0" to 0, "1" to 0, "2" to 0),
                mapOf("0" to 0, "1" to 0, "2" to 0)
            )
        )
        val gameBoardDocumentRef = roundDocumentRef.collection(GameBoardDocument.COLLECTION_NAME).document(gameBoardDocument.id)

        db.runTransaction { transaction ->
            transaction.set(roundDocumentRef, roundDocument)
            transaction.set(gameBoardDocumentRef, gameBoardDocument)
        }.await()
    }

    suspend fun reduceRoundTimer(roomId: String, roundId: String): Unit = withContext(IO) {
        val roomDocumentRef = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
        val roundDocumentRef = roomDocumentRef.collection(RoundDocument.COLLECTION_NAME).document(roundId)
        val roundDocument = roundDocumentRef.get().await().toObject(RoundDocument::class.java)!!

        roundDocument.apply {
            timerToStart = timerToStart!!.minus(1)
            preparingToStart = timerToStart!! > 0
            playing = timerToStart!! == 0
        }

        delay(1000)
        roundDocumentRef.update(roundDocument.toMap())
    }

    suspend fun getAllRoundsFinished(roomId: String): Boolean = withContext(IO) {
        val playingQuery = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
            .collection(RoundDocument.COLLECTION_NAME)
            .whereEqualTo(RoundDocument::playing.name, true)
            .limit(1)
            .get()
            .await()

        val preparingQuery = db.collection(RoomDocument.COLLECTION_NAME).document(roomId)
            .collection(RoundDocument.COLLECTION_NAME)
            .whereEqualTo(RoundDocument::preparingToStart.name, true)
            .limit(1)
            .get()
            .await()

        playingQuery.isEmpty && preparingQuery.isEmpty
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
}