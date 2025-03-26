package br.com.velha.tech.screen.game.callback

import br.com.velha.tech.navigation.GameScreenArgs

fun interface OnNavigateToGame {
    fun onExecute(args: GameScreenArgs)
}