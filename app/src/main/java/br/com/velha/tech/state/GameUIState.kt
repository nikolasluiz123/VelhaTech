package br.com.velha.tech.state

import android.graphics.drawable.Drawable
import br.com.velha.tech.core.state.MessageDialogState
import java.time.LocalTime

data class GameUIState(
    val title: String = "",
    val subtitle: String = "",
    val messageDialogState: MessageDialogState = MessageDialogState(),
    val isPaused: Boolean = false,
    val onChangePaused: (Boolean) -> Unit = { },
    val gameBoardState: GameBoardState = GameBoardState(),
    val gamePlayedRoundsListState: GamePlayedRoundsListState = GamePlayedRoundsListState()
)

data class GamePlayedRoundsListState(
    val playedRounds: List<GameRoundItem> = emptyList(),
    val totalRounds: Int = 0,
    val expanded: Boolean = false,
    val time: LocalTime = LocalTime.of(0, 0, 0),
    val onToggleExpand: () -> Unit = { }
)

data class GameRoundItem(
    var roundNumber: Int,
    var winnerName: String?
)

data class GameBoardState(
    val userId: String = "",
    val playersFigure: List<Pair<String, Drawable>> = emptyList(),
    val boardFigures: List<List<Int?>> = emptyList(),
    val onInputBoardClick: (rowIndex: Int, columnIndex: Int) -> Unit = { _, _ -> }
)
