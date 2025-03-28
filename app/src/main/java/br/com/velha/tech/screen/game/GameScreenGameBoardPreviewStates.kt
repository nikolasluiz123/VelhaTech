package br.com.velha.tech.screen.game

import br.com.velha.tech.core.R
import br.com.velha.tech.state.GameBoardState

val gameBoardEmptyState = GameBoardState()

val gameBoardFinishedState = GameBoardState(
    boardFigures = Array(3) {
        arrayOf(
            R.drawable.ic_x,
            R.drawable.ic_elipse,
            R.drawable.ic_x
        )
        arrayOf(
            R.drawable.ic_elipse,
            R.drawable.ic_x,
            R.drawable.ic_elipse
        )
        arrayOf(
            R.drawable.ic_x,
            R.drawable.ic_elipse,
            R.drawable.ic_x
        )
    }
)