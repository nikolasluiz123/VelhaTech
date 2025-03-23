package br.com.velhatech.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun VelhaTechNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = loginScreenRoute,
        modifier = modifier
    ) {

        loginScreen(
            onNavigateToRoomList = navController::navigateToRoomListScreen,
            onNavigateToRegisterUser = navController::navigateToRegisterUserScreen
        )

        registerUserScreen(
            onBackClick = navController::popBackStack
        )

        roomListScreen(
            onNavigateToRoomCreation = navController::navigateToRoomScreen
        )

        roomScreen(
            onBackClick = navController::popBackStack
        )
    }
}