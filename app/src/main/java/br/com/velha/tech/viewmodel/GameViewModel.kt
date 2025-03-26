package br.com.velha.tech.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import br.com.velha.tech.R
import br.com.velha.tech.core.callback.showErrorDialog
import br.com.velha.tech.core.extensions.fromJsonNavParamToArgs
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.to.TORoom
import br.com.velha.tech.navigation.GameScreenArgs
import br.com.velha.tech.navigation.gameScreenArgument
import br.com.velha.tech.repository.RoomRepository
import br.com.velha.tech.state.GameUIState
import br.com.velha.tech.viewmodel.common.VelhaTechViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
            roomRepository.addAuthenticatedPlayerToRoom(roomId = args.roomId)

            val room = roomRepository.findRoomById(args.roomId)!!

            _uiState.value = _uiState.value.copy(
                title = room.roomName!!,
                subtitle = getSubtitle(room)
            )
        }
    }

    private fun getSubtitle(room: TORoom): String {
        return if (room.playersCount == 1) {
            getAbbreviatedName(room.players[0].name)
        } else {
            context.getString(
                R.string.game_screen_subtitle_two_players,
                getAbbreviatedName(room.players[0].name),
                getAbbreviatedName(room.players[1].name)
            )
        }
    }

    private fun getAbbreviatedName(name: String): String {
        val first = name.split(" ").first()
        val last = name.split(" ").last()

        return "$first ${last[0]}."
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