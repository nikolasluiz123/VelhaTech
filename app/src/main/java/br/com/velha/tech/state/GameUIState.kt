package br.com.velha.tech.state

import android.graphics.drawable.Drawable
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.to.TOPlayer
import java.time.LocalTime

data class GameUIState(
    val title: String = "",
    val subtitle: String = "",
    val messageDialogState: MessageDialogState = MessageDialogState(),
    val players: List<TOPlayer> = emptyList(),
    val isPaused: Boolean = false,
    val onChangePaused: (Boolean) -> Unit = { },
    val gameBoardState: GameBoardState = GameBoardState(),
    val gamePlayedRoundsListState: GamePlayedRoundsListState = GamePlayedRoundsListState(),
    val blockUIMessageState: BlockUIMessageState = BlockUIMessageState()
)

data class GamePlayedRoundsListState(
    val playedRounds: List<GameRoundItem> = emptyList(),
    val totalRounds: Int = 0,
    val expanded: Boolean = false,
    val time: LocalTime = LocalTime.of(0, 0, 0),
    val onToggleExpand: () -> Unit = { }
) {
    val roundsToPlay: Int
        get() = totalRounds - playedRounds.size
}

data class GameRoundItem(
    var roundNumber: Int,
    val winnerPlayer: TOPlayer? = null
)

data class GameBoardState(
    val playersFigure: List<Pair<String, Drawable>> = emptyList(),
    val boardFigures: Array<Array<Int>> = Array(3) { arrayOf(0,0,0) },
    val onInputBoardClick: (rowIndex: Int, columnIndex: Int) -> Unit = { _, _ -> }
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameBoardState

        if (playersFigure != other.playersFigure) return false
        if (!boardFigures.contentDeepEquals(other.boardFigures)) return false
        if (onInputBoardClick != other.onInputBoardClick) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playersFigure.hashCode()
        result = 31 * result + boardFigures.contentDeepHashCode()
        result = 31 * result + onInputBoardClick.hashCode()
        return result
    }
}

data class BlockUIMessageState(
    val title: String = "",
    val message: String = "",
    val visible: Boolean = false
)