package br.com.velha.tech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velha.tech.screen.registeruser.RegisterUserScreen
import br.com.velha.tech.viewmodel.RegisterUserViewModel

const val registerUserScreenRoute = "registerUser"

fun NavGraphBuilder.registerUserScreen(
    onBackClick: () -> Unit
) {
    composable(route = registerUserScreenRoute) {
        val viewModel = hiltViewModel<RegisterUserViewModel>()

        RegisterUserScreen(
            viewModel = viewModel,
            onBackClick = onBackClick
        )
    }
}

fun NavController.navigateToRegisterUserScreen(navOptions: NavOptions? = null) {
    navigate(route = registerUserScreenRoute, navOptions = navOptions)
}