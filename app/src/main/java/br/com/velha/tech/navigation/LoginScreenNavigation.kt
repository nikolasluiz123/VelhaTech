package br.com.velha.tech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velha.tech.screen.login.LoginScreen
import br.com.velha.tech.viewmodel.LoginViewModel

const val loginScreenRoute = "login"

fun NavGraphBuilder.loginScreen(
    onNavigateToRoomList: () -> Unit,
    onNavigateToRegisterUser: () -> Unit
) {
    composable(route = loginScreenRoute) {
        val loginViewModel = hiltViewModel<LoginViewModel>()

        LoginScreen(
            viewModel = loginViewModel,
            onNavigateToRoomList = onNavigateToRoomList,
            onNavigateToRegisterUser = onNavigateToRegisterUser
        )
    }
}

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(route = loginScreenRoute, navOptions = navOptions)
}