package br.com.velhatech.repository

import br.com.velhatech.firebase.auth.implementations.FirebaseDefaultAuthenticationService
import br.com.velhatech.firebase.auth.implementations.FirebaseGoogleAuthenticationService
import br.com.velhatech.firebase.auth.user.User
import com.google.firebase.auth.AuthResult

class UserRepository(
    private val firebaseDefaultAuthenticationService: FirebaseDefaultAuthenticationService,
    private val firebaseGoogleAuthenticationService: FirebaseGoogleAuthenticationService,
) {

    suspend fun authenticate(email: String, password: String) {
        firebaseDefaultAuthenticationService.authenticate(email, password)
    }

    suspend fun save(user: User) {
        firebaseDefaultAuthenticationService.save(user)
    }

    suspend fun signInWithGoogle(): AuthResult? {
        return firebaseGoogleAuthenticationService.signIn()
    }

    fun logout() {
        firebaseDefaultAuthenticationService.logout()
    }
}