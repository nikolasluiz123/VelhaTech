package br.com.velhatech.viewmodel

import android.content.Context
import br.com.velhatech.R
import br.com.velhatech.components.buttons.radio.RadioButtonOption
import br.com.velhatech.components.fields.state.RadioButtonField
import br.com.velhatech.components.fields.state.TextField
import br.com.velhatech.core.callback.showErrorDialog
import br.com.velhatech.core.state.MessageDialogState
import br.com.velhatech.core.validation.FieldValidationError
import br.com.velhatech.firebase.enums.EnumDifficultLevel
import br.com.velhatech.screen.common.callback.OnSaveLessFailureCallback
import br.com.velhatech.screen.roomcreation.enums.EnumRoundType
import br.com.velhatech.screen.roomcreation.enums.EnumValidatedRoomCreationFields
import br.com.velhatech.screen.roomcreation.enums.EnumValidatedRoomCreationFields.*
import br.com.velhatech.state.RoomUIState
import br.com.velhatech.usecase.SaveRoomUseCase
import br.com.velhatech.viewmodel.common.VelhaTechViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RoomViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val saveRoomUseCase: SaveRoomUseCase
) : VelhaTechViewModel(context) {

    private val _uiState: MutableStateFlow<RoomUIState> = MutableStateFlow(RoomUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        initialLoadUIState()

    }

    override fun onShowError(throwable: Throwable) {
        _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(
            message = context.getString(R.string.unknown_error_message)
        )
    }

    private fun initialLoadUIState() {
        _uiState.update {
            it.copy(
                roomName = initializeRoomNameTextField(),
                roomPassword = initializeRoomPasswordTextField(),
                rounds = initializeRoundsRadioButtonField(),
                difficultLevel = initializeDifficultLevelRadioButtonField(),
                messageDialogState = initializeMessageDialogState(),
                onToggleLoading = {
                    _uiState.value = _uiState.value.copy(
                        showLoading = !_uiState.value.showLoading
                    )
                }
            )
        }
    }

    private fun initializeRoomNameTextField(): TextField {
        return TextField(
            onChange = {
                _uiState.value = _uiState.value.copy(
                    roomName = _uiState.value.roomName.copy(
                        value = it,
                        errorMessage = ""
                    ),
                    toRoom = _uiState.value.toRoom.copy(
                        roomName = it
                    )
                )
            }
        )
    }

    private fun initializeRoomPasswordTextField(): TextField {
        return TextField(
            onChange = {
                _uiState.value = _uiState.value.copy(
                    roomPassword = _uiState.value.roomPassword.copy(
                        value = it,
                        errorMessage = ""
                    ),
                    toRoom = _uiState.value.toRoom.copy(
                        password = it
                    )
                )
            }
        )
    }

    private fun initializeRoundsRadioButtonField(): RadioButtonField<EnumRoundType> {
        return RadioButtonField(
            onOptionSelected = {
                _uiState.value = _uiState.value.copy(
                    toRoom = _uiState.value.toRoom.copy(
                        roundsCount = getRoundsCountFrom(it)
                    )
                )
            },
            options = EnumRoundType.entries.map(::RadioButtonOption)
        )
    }

    private fun getRoundsCountFrom(option: RadioButtonOption<EnumRoundType>): Int {
        return when (option.value) {
            EnumRoundType.MD_3 -> 3
            EnumRoundType.MD_5 -> 5
        }
    }

    private fun initializeDifficultLevelRadioButtonField(): RadioButtonField<EnumDifficultLevel> {
        return RadioButtonField(
            onOptionSelected = {
                _uiState.value = _uiState.value.copy(
                    toRoom = _uiState.value.toRoom.copy(
                        difficultLevel = it.value
                    )
                )
            },
            options = EnumDifficultLevel.entries.map(::RadioButtonOption)
        )
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

    fun saveRoom(onSuccess: () -> Unit, onComplete: () -> Unit) {
        launch {
            val validationResults = saveRoomUseCase(_uiState.value.toRoom)

            if (validationResults.isEmpty()) {
                onSuccess()
            } else {
                showValidationMessages(validationResults)
            }

            onComplete()
        }
    }

    private fun showValidationMessages(errors: List<FieldValidationError<EnumValidatedRoomCreationFields>>) {
        val dialogValidations = errors.firstOrNull { it.field == null }

        if (dialogValidations != null) {
            _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(dialogValidations.message)
            return
        }

        errors.forEach {
            when (it.field) {
                NAME -> {
                    _uiState.value = _uiState.value.copy(
                        roomName = _uiState.value.roomName.copy(errorMessage = it.message)
                    )
                }

                PASSWORD -> {
                    _uiState.value = _uiState.value.copy(
                        roomPassword = _uiState.value.roomPassword.copy(errorMessage = it.message)
                    )
                }
            }
        }
    }

}