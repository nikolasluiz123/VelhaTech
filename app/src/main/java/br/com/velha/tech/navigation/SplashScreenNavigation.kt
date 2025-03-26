package br.com.velha.tech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.velha.tech.screen.splash.SplashScreen
import br.com.velha.tech.viewmodel.SplashViewModel

const val splashScreenRoute = "splash"

fun NavGraphBuilder.splashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRoomList: () -> Unit
) {
    composable(route = splashScreenRoute) {
        val viewModel = hiltViewModel<SplashViewModel>()

        SplashScreen(
            viewModel = viewModel,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToRoomList = onNavigateToRoomList
        )
    }
}