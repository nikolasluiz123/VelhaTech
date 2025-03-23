package br.com.velhatech.viewmodel

import android.content.Context
import br.com.velhatech.R
import br.com.velhatech.components.filter.SimpleFilterState
import br.com.velhatech.core.callback.showErrorDialog
import br.com.velhatech.core.state.MessageDialogState
import br.com.velhatech.firebase.apis.analytics.logSimpleFilterClick
import br.com.velhatech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velhatech.repository.RoomRepository
import br.com.velhatech.screen.roomlist.enums.EnumRoomListTags
import br.com.velhatech.state.RoomListUIState
import br.com.velhatech.viewmodel.common.VelhaTechViewModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RoomListViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val roomRepository: RoomRepository,
    private val commonFirebaseAuthService: CommonFirebaseAuthenticationService
): VelhaTechViewModel(context) {

    private val _uiState: MutableStateFlow<RoomListUIState> = MutableStateFlow(RoomListUIState())
    val uiState get() = _uiState.asStateFlow()

    init {
        initialLoadUIState()
        addRoomListListener()
    }

    private fun initialLoadUIState() {
        _uiState.update {
            it.copy(
                subtitle = commonFirebaseAuthService.getAuthenticatedUser()?.name,
                messageDialogState = initializeMessageDialogState(),
                simpleFilterState = initializeSimpleFilterState()
            )
        }
    }

    private fun initializeSimpleFilterState(): SimpleFilterState {
        return SimpleFilterState(
            onSimpleFilterChange = {
                // TODO - Pensar no filtro depois
            },
            onExpandedChange = {
                Firebase.analytics.logSimpleFilterClick(EnumRoomListTags.ROOM_LIST_SCREEN_FILTER)

                _uiState.value = _uiState.value.copy(
                    simpleFilterState = _uiState.value.simpleFilterState.copy(expanded = false)
                )
            }
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

    private fun addRoomListListener() {
        launch {
            roomRepository.addRoomListListener(
                onSuccess = { chats ->
                    _uiState.value = _uiState.value.copy(rooms = chats)
                },
                onError = { exception ->
                    onShowError(exception)
                    onError(exception)
                }
            )
        }
    }

    override fun onShowError(throwable: Throwable) {
        _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(
            message = context.getString(R.string.unknown_error_message)
        )
    }

    override fun onCleared() {
        super.onCleared()

        roomRepository.removeRoomListListener()
    }
}