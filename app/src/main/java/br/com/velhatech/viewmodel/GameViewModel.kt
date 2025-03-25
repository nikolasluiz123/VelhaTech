package br.com.velhatech.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import br.com.velhatech.R
import br.com.velhatech.core.callback.showErrorDialog
import br.com.velhatech.core.extensions.fromJsonNavParamToArgs
import br.com.velhatech.core.state.MessageDialogState
import br.com.velhatech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velhatech.navigation.GameScreenArgs
import br.com.velhatech.navigation.gameScreenArgument
import br.com.velhatech.repository.RoomRepository
import br.com.velhatech.state.GameUIState
import br.com.velhatech.viewmodel.common.VelhaTechViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.jvm.java

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val roomRepository: RoomRepository,
    private val commonFirebaseAuthenticationService: CommonFirebaseAuthenticationService,
    savedStateHandle: SavedStateHandle
): VelhaTechViewModel(context) {

    private val _uiState = MutableStateFlow(GameUIState())
    val uiState = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[gameScreenArgument]

    init {
        initialLoadUIState()
        loadUIStateWithDatabaseInfos()
    }

    private fun initialLoadUIState() {
        _uiState.update {
            it.copy(
                messageDialogState = initializeMessageDialogState(),
                onChangePaused = { isPaused ->
                    _uiState.value = _uiState.value.copy(
                        isPaused = isPaused
                    )
                }
            )
        }
    }

    private fun loadUIStateWithDatabaseInfos() {
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

        launch {
            val room = roomRepository.findRoomById(args.roomId)!!

            roomRepository.addAuthenticatedPlayerToRoom(roomId = args.roomId)

            _uiState.value = _uiState.value.copy(
                title = room.roomName!!,
            )
        }
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

    override fun onShowError(throwable: Throwable) {
        _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(
            message = context.getString(R.string.unknown_error_message)
        )
    }

    fun onBackClick(onSuccess: () -> Unit) {
        launch {
            val user = commonFirebaseAuthenticationService.getAuthenticatedUser()
            val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

            roomRepository.removePlayerFromRoom(roomId = args.roomId)
            onSuccess()
        }
    }
}