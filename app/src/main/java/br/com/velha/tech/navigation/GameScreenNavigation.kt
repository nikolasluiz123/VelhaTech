package br.com.velha.tech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velha.tech.core.extensions.defaultGSon
import br.com.velha.tech.screen.game.GameScreen
import br.com.velha.tech.viewmodel.GameViewModel
import com.google.gson.GsonBuilder

const val gameScreenRoute = "game"
const val gameScreenArgument = "gameArguments"

fun NavGraphBuilder.gameScreen(
    onBackClick: () -> Unit,
) {
    composable(route = "$gameScreenRoute?$gameScreenArgument={$gameScreenArgument}") {
        val viewModel = hiltViewModel<GameViewModel>()

        GameScreen(
            viewModel = viewModel,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToGameScreen(args: GameScreenArgs, navOptions: NavOptions? = null) {
    val json = GsonBuilder().defaultGSon().toJson(args)
    navigate(route = "$gameScreenRoute?$gameScreenArgument={$json}", navOptions = navOptions)
}

data class GameScreenArgs(val roomId: String)