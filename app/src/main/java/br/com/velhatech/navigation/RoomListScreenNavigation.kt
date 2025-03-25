package br.com.velhatech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velhatech.screen.game.callback.OnNavigateToGame
import br.com.velhatech.screen.roomlist.RoomListScreen
import br.com.velhatech.viewmodel.RoomListViewModel

const val roomListScreenRoute = "roomList"

fun NavGraphBuilder.roomListScreen(
    onNavigateToRoomCreation: () -> Unit,
    onNavigateToGame: OnNavigateToGame
) {
    composable(route = roomListScreenRoute) {
        val loginViewModel = hiltViewModel<RoomListViewModel>()

        RoomListScreen(
            viewModel = loginViewModel,
            onNavigateToRoomCreation = onNavigateToRoomCreation,
            onNavigateToGame = onNavigateToGame
        )
    }
}

fun NavController.navigateToRoomListScreen(navOptions: NavOptions? = null) {
    navigate(route = roomListScreenRoute, navOptions = navOptions)
}