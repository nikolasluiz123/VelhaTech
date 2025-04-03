package br.com.velha.tech.viewmodel

import android.content.Context
import br.com.velha.tech.R
import br.com.velha.tech.components.fields.state.TextField
import br.com.velha.tech.core.callback.showErrorDialog
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.core.validation.FieldValidationError
import br.com.velha.tech.firebase.auth.exception.EmailNotVerifiedException
import br.com.velha.tech.screen.login.enums.EnumValidatedLoginFields
import br.com.velha.tech.state.LoginUIState
import br.com.velha.tech.usecase.DefaultLoginUseCase
import br.com.velha.tech.usecase.GoogleLoginUseCase
import br.com.velha.tech.viewmodel.common.VelhaTechViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val defaultLoginUseCase: DefaultLoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
) : VelhaTechViewModel(context) {

    private val _uiState: MutableStateFlow<LoginUIState> = MutableStateFlow(LoginUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        initialLoadUIState()
    }

    override fun onShowError(throwable: Throwable) {
        val message = when (throwable) {
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.validation_msg_invalid_credetials_login)
            is EmailNotVerifiedException -> throwable.message!!
            else -> context.getString(R.string.unknown_error_message)
        }

        _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(message = message)
    }

    private fun initialLoadUIState() {
        _uiState.update { currentState ->
            currentState.copy(
                email = initializeEmailTextField(),
                password = initializePasswordTextField(),
                messageDialogState = initializeMessageDialogState()
            )
        }
    }

    private fun initializeEmailTextField(): TextField {
        return TextField(onChange = {
            _uiState.value = _uiState.value.copy(
                email = _uiState.value.email.copy(
                    value = it,
                    errorMessage = ""
                ),
            )
        })
    }

    private fun initializePasswordTextField(): TextField {
        return TextField(onChange = {
            _uiState.value = _uiState.value.copy(
                password = _uiState.value.password.copy(
                    value = it,
                    errorMessage = ""
                ),
            )
        })
    }

    private fun initializeMessageDialogState(): MessageDialogState {
        return MessageDialogState(
            onShowDialog = { type, message, onConfirm, onCancel, customTitle ->
                _uiState.value = _uiState.value.copy(
                    messageDialogState = _uiState.value.messageDialogState.copy(
                        dialogType = type,
                        dialogMessage = message,
                        showDialog = true,
                        onConfirm = onConfirm,
                        onCancel = onCancel,
                        customTitle = customTitle
                    )
                )
            },
            onHideDialog = {
                _uiState.value = _uiState.value.copy(
                    messageDialogState = _uiState.value.messageDialogState.copy(
                        showDialog = false
                    )
                )
            }
        )
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

    private fun showValidationMessages(validationsResult: List<FieldValidationError<EnumValidatedLoginFields>>) {
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