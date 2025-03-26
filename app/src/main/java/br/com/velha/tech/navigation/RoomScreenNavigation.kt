package br.com.velha.tech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velha.tech.screen.game.callback.OnNavigateToGame
import br.com.velha.tech.screen.roomcreation.RoomScreen
import br.com.velha.tech.viewmodel.RoomViewModel

const val roomScreenRoute = "room"

fun NavGraphBuilder.roomScreen(
    onBackClick: () -> Unit,
    onNavigateToGame: OnNavigateToGame
) {
    composable(route = roomScreenRoute) {
        val viewModel = hiltViewModel<RoomViewModel>()

        RoomScreen(
            viewModel = viewModel,
            onBackClick = onBackClick,
            onNavigateToGame = onNavigateToGame
        )
    }
}

fun NavController.navigateToRoomScreen(navOptions: NavOptions? = null) {
    navigate(route = roomScreenRoute, navOptions = navOptions)
}