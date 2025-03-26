package br.com.velha.tech.screen.game

import br.com.velha.tech.state.GamePlayedRoundsListState
import br.com.velha.tech.state.GameRoundItem
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