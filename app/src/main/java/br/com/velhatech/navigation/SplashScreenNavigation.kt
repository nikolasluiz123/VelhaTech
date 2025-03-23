package br.com.velhatech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.velhatech.screen.splash.SplashScreen
import br.com.velhatech.viewmodel.LoginViewModel
import br.com.velhatech.viewmodel.SplashViewModel

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