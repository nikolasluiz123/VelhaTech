package br.com.velhatech.firebase.auth.implementations

import br.com.velhatech.firebase.auth.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

open class CommonFirebaseAuthenticationService {

    fun logout() {
        Firebase.auth.signOut()
    }

    fun getAuthenticatedUser(): User? {
        return Firebase.auth.currentUser?.let { firebaseUser ->
            User(
                id = firebaseUser.uid,
                name = firebaseUser.displayName,
                email = firebaseUser.email,
            )
        }
    }
}