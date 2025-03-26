package br.com.velha.tech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velha.tech.screen.game.callback.OnNavigateToGame
import br.com.velha.tech.screen.roomlist.RoomListScreen
import br.com.velha.tech.viewmodel.RoomListViewModel

const val roomListScreenRoute = "roomList"

fun NavGraphBuilder.roomListScreen(
    onNavigateToRoomCreation: () -> Unit,
    onNavigateToGame: OnNavigateToGame,
    onLogoutClick: () -> Unit
) {
    composable(route = roomListScreenRoute) {
        val loginViewModel = hiltViewModel<RoomListViewModel>()

        RoomListScreen(
            viewModel = loginViewModel,
            onNavigateToRoomCreation = onNavigateToRoomCreation,
            onNavigateToGame = onNavigateToGame,
            onLogoutClick = onLogoutClick
        )
    }
}

fun NavController.navigateToRoomListScreen(navOptions: NavOptions? = null) {
    navigate(route = roomListScreenRoute, navOptions = navOptions)
}