package br.com.velhatech.screen.game

import br.com.velhatech.state.GamePlayedRoundsListState
import br.com.velhatech.state.GameRoundItem
import java.time.LocalTime

internal val gamePlayedRoundsListStateThreeRoundsClosed = GamePlayedRoundsListState(
    playedRounds = listOf(),
    totalRounds = 3,
    expanded = false,
    time = LocalTime.of(0, 0, 15),
)

internal val gamePlayedRoundsListStateThreeRoundsOpened = GamePlayedRoundsListState(
    playedRounds = listOf(
        GameRoundItem(
            roundNumber = 1,
            winnerName = "Player 1"
        ),
        GameRoundItem(
            roundNumber = 2,
            winnerName = null
        )
    ),
    totalRounds = 3,
    expanded = true,
    time = LocalTime.of(0, 0, 15)
)