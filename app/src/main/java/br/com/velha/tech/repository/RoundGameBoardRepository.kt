package br.com.velha.tech.repository

import br.com.velha.tech.firebase.data.access.service.FirestoreRoundGameBoardService
import com.google.firebase.firestore.ListenerRegistration

class RoundGameBoardRepository(
    private val roundGameBoardService: FirestoreRoundGameBoardService,
) {
    private var boardListener: ListenerRegistration? = null

    suspend fun updateBoard(roomId: String, boardFigures: Array<Array<Int>>) {
        val boardFiguresList = boardFigures.map { row ->
            row.mapIndexed { index, value -> index.toString() to value }.toMap()
        }

        roundGameBoardService.updateBoard(roomId, boardFiguresList)
    }

    suspend fun addBoardListener(
        roomId: String,
        onSuccess: (Array<Array<Int>>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (boardListener == null) {
            boardListener = roundGameBoardService.addBoardListener(
                roomId = roomId,
                onSuccess = { matrix ->
                    val array = matrix.map { it.values.toTypedArray() }.toTypedArray()
                    onSuccess(array)
                },
                onError = onError
            )
        }
    }

    fun removeBoardListener() {
        boardListener?.remove()
    }
}