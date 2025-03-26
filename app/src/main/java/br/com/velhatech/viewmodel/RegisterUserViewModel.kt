package br.com.velhatech.viewmodel

import android.R.id.message
import android.content.Context
import br.com.velhatech.R
import br.com.velhatech.components.fields.state.TextField
import br.com.velhatech.core.callback.showErrorDialog
import br.com.velhatech.core.exceptions.NoLoggingException
import br.com.velhatech.core.state.MessageDialogState
import br.com.velhatech.core.validation.FieldValidationError
import br.com.velhatech.firebase.auth.exception.EmailNotVerifiedException
import br.com.velhatech.screen.registeruser.enums.EnumValidatedUserFields
import br.com.velhatech.state.RegisterUserUIState
import br.com.velhatech.usecase.SaveUserUseCase
import br.com.velhatech.viewmodel.common.VelhaTechViewModel
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val saveUserUseCase: SaveUserUseCase
): VelhaTechViewModel(context) {

    private val _uiState: MutableStateFlow<RegisterUserUIState> = MutableStateFlow(RegisterUserUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        initialLoadUIState()
    }

    override fun onShowError(throwable: Throwable) {
        _uiState.value.onToggleLoading()

        val message = when (throwable) {
            is FirebaseAuthWeakPasswordException -> context.getString(R.string.register_user_screen_weak_password_message)
            is FirebaseAuthUserCollisionException -> context.getString(R.string.register_user_screen_user_collision_message)
            else -> context.getString(R.string.unknown_error_message)
        }

        _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(message = message)
    }

    private fun initialLoadUIState() {
        _uiState.update { currentState ->
            currentState.copy(
                title = context.getString(R.string.register_user_screen_title),
                name = initializeNameTextField(),
                email = initializeEmailTextField(),
                password = initializePasswordTextField(),
                messageDialogState = initializeMessageDialogState(),
                onToggleLoading = {
                    _uiState.value = _uiState.value.copy(
                        showLoading = !_uiState.value.showLoading
                    )
                }
            )
        }
    }

    private fun initializeNameTextField(): TextField {
        return TextField(onChange = {
            _uiState.value = _uiState.value.copy(
                name = _uiState.value.name.copy(
                    value = it,
                    errorMessage = ""
                ),
                user = _uiState.value.user.copy(name = it)
            )
        })
    }

    private fun initializeEmailTextField(): TextField {
        return TextField(onChange = {
            _uiState.value = _uiState.value.copy(
                email = _uiState.value.email.copy(
                    value = it,
                    errorMessage = ""
                ),
                user = _uiState.value.user.copy(email = it)
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
                user = _uiState.value.user.copy(password = it)
            )
        })
    }

    private fun initializeMessageDialogState(): MessageDialogState {
        return MessageDialogState(
            onShowDialog = { type, message, onConfirm, onCancel ->
                _uiState.value = _uiState.value.copy(
                    messageDialogState = _uiState.value.messageDialogState.copy(
                        dialogType = type,
                        dialogMessage = message,
                        showDialog = true,
                        onConfirm = onConfirm,
                        onCancel = onCancel
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
    
    fun saveUser(onSuccess: () -> Unit, onComplete: () -> Unit) {
        launch {
            val validationResults = saveUserUseCase(_uiState.value.user)

            if (validationResults.isEmpty()) {
                onSuccess()
            } else {
                showValidationMessages(validationResults)
            }

            onComplete()
        }
    }

    private fun showValidationMessages(validationResults: List<FieldValidationError<EnumValidatedUserFields>>) {
        validationResults.forEach {
            when (it.field) {
                EnumValidatedUserFields.NAME -> {
                    _uiState.value = _uiState.value.copy(
                        name = _uiState.value.name.copy(errorMessage = it.message)
                    )
                }

                EnumValidatedUserFields.EMAIL -> {
                    _uiState.value = _uiState.value.copy(
                        email = _uiState.value.email.copy(errorMessage = it.message)
                    )
                }

                EnumValidatedUserFields.PASSWORD -> {
                    _uiState.value = _uiState.value.copy(
                        password = _uiState.value.password.copy(errorMessage = it.message)
                    )
                }
            }
        }
    }
}