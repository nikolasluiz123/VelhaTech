package br.com.velhatech.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import br.com.velhatech.screen.registeruser.RegisterUserScreen
import br.com.velhatech.viewmodel.RegisterUserViewModel

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