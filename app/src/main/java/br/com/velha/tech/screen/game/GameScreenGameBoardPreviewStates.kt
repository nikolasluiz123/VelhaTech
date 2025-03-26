package br.com.velha.tech.screen.game

import br.com.velha.tech.core.R
import br.com.velha.tech.state.GameBoardState

val gameBoardEmptyState = GameBoardState()

val gameBoardFinishedState = GameBoardState(
    boardFigures = listOf(
        listOf(
            R.drawable.ic_elipse,
            R.drawable.ic_elipse,
            R.drawable.ic_elipse
        ),
        listOf(
            R.drawable.ic_x,
            R.drawable.ic_elipse,
            R.drawable.ic_x
        ),
        listOf(
            R.drawable.ic_elipse,
            R.drawable.ic_x,
            R.drawable.ic_elipse
        ),
    )
)