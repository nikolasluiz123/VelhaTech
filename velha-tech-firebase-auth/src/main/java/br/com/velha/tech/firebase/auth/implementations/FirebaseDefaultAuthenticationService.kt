package br.com.velha.tech.firebase.auth.implementations

import android.content.Context
import br.com.velha.tech.firebase.auth.R
import br.com.velha.tech.firebase.auth.exception.EmailNotVerifiedException
import br.com.velha.tech.firebase.auth.user.User
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseDefaultAuthenticationService(val context: Context): CommonFirebaseAuthenticationService() {

    suspend fun authenticate(email: String, password: String): Unit = withContext(IO) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()

        if (Firebase.auth.currentUser?.isEmailVerified == false) {
            logout()
            throw EmailNotVerifiedException(context.getString(R.string.firebase_default_auth_email_not_verfied_message))
        }
    }

    suspend fun save(user: User): Unit = withContext(IO) {
        val sigInMethods = Firebase.auth.fetchSignInMethodsForEmail(user.email!!).await()

        if (sigInMethods.signInMethods.isNullOrEmpty()) {
            register(user)
        } else {
            updateUserInfos(user)
        }
    }

    private suspend fun register(user: User): Unit = withContext(IO) {
        val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(user.name).build()

        Firebase.auth.createUserWithEmailAndPassword(user.email!!, user.password!!).await()
        Firebase.auth.currentUser?.updateProfile(userProfileChangeRequest)?.await()
        Firebase.auth.currentUser?.sendEmailVerification()?.await()
    }

    private suspend fun updateUserInfos(user: User): Unit = withContext(IO) {
        Firebase.auth.currentUser?.let { firebaseUser ->
            try {
                val displayNameChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(user.name).build()

                firebaseUser.verifyBeforeUpdateEmail(user.email!!).await()
                firebaseUser.updatePassword(user.password!!).await()
                firebaseUser.updateProfile(displayNameChangeRequest).await()
            } catch (_: FirebaseAuthRecentLoginRequiredException) {
                logout()
                authenticate(user.email!!, user.password!!)
                updateUserInfos(user)
            }
        }
    }
}