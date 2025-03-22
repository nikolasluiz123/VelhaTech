package br.com.velhatech.usecase

import android.content.Context
import br.com.velhatech.R
import br.com.velhatech.repository.UserRepository

class GoogleLoginUseCase(
    private val context: Context,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): GoogleAuthResult {
        userRepository.signInWithGoogle() ?: return GoogleAuthResult(errorMessage = context.getString(R.string.login_google_unknown_error))
        return GoogleAuthResult(success = true, errorMessage = null)
    }
}

data class GoogleAuthResult(
    val success: Boolean = false,
    val errorMessage: String? = null
)