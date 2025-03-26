package br.com.velha.tech.navigation

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun VelhaTechNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = getStartDestination(),
        modifier = modifier
    ) {

        splashScreen(
            onNavigateToLogin = navController::navigateToLoginScreen,
            onNavigateToRoomList = navController::navigateToRoomListScreen
        )

        loginScreen(
            onNavigateToRoomList = navController::navigateToRoomListScreen,
            onNavigateToRegisterUser = navController::navigateToRegisterUserScreen
        )

        registerUserScreen(
            onBackClick = navController::popBackStack
        )

        roomListScreen(
            onNavigateToRoomCreation = navController::navigateToRoomScreen,
            onNavigateToGame = navController::navigateToGameScreen,
            onLogoutClick = {
                navController.navigateToLoginScreen(
                    navOptions = navOptions {
                        popUpTo(loginScreenRoute) {
                            inclusive = true
                        }
                    }
                )
            }
        )

        roomScreen(
            onBackClick = navController::popBackStack,
            onNavigateToGame = navController::navigateToGameScreen
        )

        gameScreen(
            onBackClick = navController::popBackStack
        )
    }
}

@Composable
private fun getStartDestination(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (Firebase.auth.currentUser != null) {
            roomListScreenRoute
        } else {
            loginScreenRoute
        }
    } else {
        splashScreenRoute
    }
}