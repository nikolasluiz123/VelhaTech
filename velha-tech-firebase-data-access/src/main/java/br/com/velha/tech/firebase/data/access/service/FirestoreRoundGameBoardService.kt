package br.com.velha.tech.firebase.data.access.service

import br.com.velha.tech.firebase.models.GameBoardDocument
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirestoreRoundGameBoardService(
    private val roomRoundService: FirestoreRoomRoundService
): FirestoreService() {

    suspend fun updateBoard(roomId: String, boardFigures: List<Map<String, Int>>): Unit = withContext(IO) {
        val roundDocumentRef = roomRoundService.getPlayingRoundDocumentRef(roomId)!!
        val gameBoardCollection = roundDocumentRef.collection(GameBoardDocument.COLLECTION_NAME)
        val gameBoardDocumentRef = gameBoardCollection.limit(1).get().await().documents.firstOrNull()?.reference!!
        val gameBoardDocument = gameBoardDocumentRef.get().await().toObject(GameBoardDocument::class.java)!!

        gameBoardDocument.matrix = boardFigures
        gameBoardDocumentRef.update(gameBoardDocument.toMap()).await()
    }

    suspend fun addBoardListener(
        roomId: String,
        onSuccess: (List<Map<String, Int>>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        val roundDocumentRef = roomRoundService.getPlayingRoundDocumentRef(roomId)!!
        val gameBoardCollection = roundDocumentRef.collection(GameBoardDocument.COLLECTION_NAME)
        val gameBoardDocumentRef = gameBoardCollection.limit(1).get().await().documents.firstOrNull()?.reference!!

        return gameBoardDocumentRef.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            if (value != null) {
                val gameBoardDocument = value.toObject(GameBoardDocument::class.java)!!
                onSuccess(gameBoardDocument.matrix)
            }
        }
    }
}