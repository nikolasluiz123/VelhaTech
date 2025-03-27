package br.com.velha.tech.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import br.com.velha.tech.R
import br.com.velha.tech.core.callback.showErrorDialog
import br.com.velha.tech.core.extensions.fromJsonNavParamToArgs
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.navigation.GameScreenArgs
import br.com.velha.tech.navigation.gameScreenArgument
import br.com.velha.tech.repository.RoomPlayersRepository
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
    private val roomPlayersRepository: RoomPlayersRepository,
    savedStateHandle: SavedStateHandle
): VelhaTechViewModel(context) {

    private val _uiState = MutableStateFlow(GameUIState())
    val uiState = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[gameScreenArgument]

    init {
        initialLoadUIState()
        loadUIStateWithDatabaseInfos()
        addRoomPlayerListListener()
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
            roomPlayersRepository.addAuthenticatedPlayerToRoom(roomId = args.roomId)

            val room = roomRepository.findRoomById(args.roomId)!!

            _uiState.value = _uiState.value.copy(
                title = room.roomName!!,
            )
        }
    }

    private fun addRoomPlayerListListener() {
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

        roomPlayersRepository.addRoomPlayerListListener(
            roomId = args.roomId,
            onSuccess = { players ->
                _uiState.value = _uiState.value.copy(
                    subtitle = getSubtitle(players)
                )
            },
            onError = { exception ->
                onShowError(exception)
                onError(exception)
            }
        )
    }

    private fun getSubtitle(players: List<TOPlayer>): String {
        return if (players.size == 1) {
            getAbbreviatedName(players[0].name)
        } else {
            context.getString(
                R.string.game_screen_subtitle_two_players,
                getAbbreviatedName(players[0].name),
                getAbbreviatedName(players[1].name)
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

    override fun onCleared() {
        super.onCleared()
        roomPlayersRepository.removeRoomPlayerListListener()
    }

    fun onBackClick(onSuccess: () -> Unit) {
        launch {
            val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!
            roomPlayersRepository.removePlayerFromRoom(roomId = args.roomId)
            onSuccess()
        }
    }
}