package br.com.velhatech.viewmodel

import android.content.Context
import br.com.velhatech.R
import br.com.velhatech.core.callback.showErrorDialog
import br.com.velhatech.core.validation.FieldValidationError
import br.com.velhatech.screen.login.enums.EnumLoginValidationTypes
import br.com.velhatech.screen.login.enums.EnumValidatedLoginFields
import br.com.velhatech.state.LoginUIState
import br.com.velhatech.usecase.DefaultLoginUseCase
import br.com.velhatech.usecase.GoogleLoginUseCase
import br.com.velhatech.viewmodel.common.VelhaTechViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val defaultLoginUseCase: DefaultLoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
) : VelhaTechViewModel(context) {

    private val _uiState: MutableStateFlow<LoginUIState> = MutableStateFlow(LoginUIState())
    val uiState get() = _uiState.asStateFlow()

    override fun onShowError(throwable: Throwable) {
        val message = when (throwable) {
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.validation_msg_invalid_credetials_login)
            else -> context.getString(R.string.unknown_error_message)
        }

        _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(message = message)
    }

    fun onLoginClick(onSuccess: () -> Unit, onFailure: () -> Unit) {
        launch {
            val username = _uiState.value.email.value
            val password = _uiState.value.password.value

            val validationsResult = defaultLoginUseCase(username, password)

            if (validationsResult.isEmpty()) {
                onSuccess()
            } else {
                showValidationMessages(validationsResult)
                onFailure()
            }
        }
    }

    private fun showValidationMessages(validationsResult: List<FieldValidationError<EnumValidatedLoginFields, EnumLoginValidationTypes>>) {
        val dialogValidations = validationsResult.firstOrNull { it.field == null }

        if (dialogValidations != null) {
            _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(dialogValidations.message)
            return
        }

        validationsResult.forEach {
            when(it.field!!) {
                EnumValidatedLoginFields.EMAIL -> {
                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(
                            errorMessage = it.message
                        )
                    )
                }
                EnumValidatedLoginFields.PASSWORD -> {
                    _uiState.value = _uiState.value.copy(
                        password = _uiState.value.password.copy(
                            errorMessage = it.message
                        )
                    )
                }
            }
        }

    }

    fun onLoginWithGoogleClick(onSuccess: () -> Unit, onFailure: () -> Unit) {
        launch {
            val result = googleLoginUseCase()

            if (result.success) {
                onSuccess()
            } else {
                _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(result.errorMessage!!)
                onFailure()
            }
        }
    }
}