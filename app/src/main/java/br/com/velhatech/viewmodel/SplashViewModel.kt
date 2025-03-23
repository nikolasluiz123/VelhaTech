package br.com.velhatech.viewmodel

import androidx.lifecycle.ViewModel
import br.com.velhatech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseAuthenticationService: CommonFirebaseAuthenticationService
): ViewModel() {

    fun verifyNavigationDestination(
        onNavigateToLogin: () -> Unit,
        onNavigateToRoomList: () -> Unit
    ) {
        if (firebaseAuthenticationService.getAuthenticatedUser() != null) {
            onNavigateToRoomList()
        } else {
            onNavigateToLogin()
        }
    }
}