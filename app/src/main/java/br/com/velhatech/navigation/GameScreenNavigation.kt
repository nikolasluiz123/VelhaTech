package br.com.velhatech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velhatech.core.extensions.defaultGSon
import br.com.velhatech.screen.game.GameScreen
import br.com.velhatech.viewmodel.GameViewModel
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