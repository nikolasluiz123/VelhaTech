package br.com.velhatech.firebase.auth.implementations

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import br.com.velhatech.firebase.auth.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseGoogleAuthenticationService(private val context: Context): CommonFirebaseAuthenticationService() {

    suspend fun signIn(): AuthResult? = withContext(IO) {
        val googleCredential = getGoogleCredential()

        if (googleCredential is CustomCredential && googleCredential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val token = GoogleIdTokenCredential.createFrom(googleCredential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(token.idToken, null)

            Firebase.auth.signInWithCredential(firebaseCredential).await()
        }

        null
    }

    private suspend fun getGoogleCredential(): Credential? {
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        return try {
            val result = CredentialManager.create(context).getCredential(
                request = request,
                context = context
            )

            result.credential
        } catch (ex: GetCredentialCancellationException) {
            null
        }

    }
}