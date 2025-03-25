package br.com.velhatech.screen.game

import br.com.velhatech.state.GameBoardState

val gameBoardEmptyState = GameBoardState()

val gameBoardFinishedState = GameBoardState(
    boardFigures = listOf(
        listOf(
            br.com.velhatech.core.R.drawable.ic_elipse,
            br.com.velhatech.core.R.drawable.ic_elipse,
            br.com.velhatech.core.R.drawable.ic_elipse
        ),
        listOf(
            br.com.velhatech.core.R.drawable.ic_x,
            br.com.velhatech.core.R.drawable.ic_elipse,
            br.com.velhatech.core.R.drawable.ic_x
        ),
        listOf(
            br.com.velhatech.core.R.drawable.ic_elipse,
            br.com.velhatech.core.R.drawable.ic_x,
            br.com.velhatech.core.R.drawable.ic_elipse
        ),
    )
)