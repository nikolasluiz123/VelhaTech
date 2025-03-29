package br.com.velha.tech.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import br.com.velha.tech.R
import br.com.velha.tech.core.callback.showErrorDialog
import br.com.velha.tech.core.extensions.fromJsonNavParamToArgs
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.enums.EnumDifficultLevel
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORound
import br.com.velha.tech.navigation.GameScreenArgs
import br.com.velha.tech.navigation.gameScreenArgument
import br.com.velha.tech.repository.RoomPlayersRepository
import br.com.velha.tech.repository.RoomRepository
import br.com.velha.tech.repository.RoomRoundRepository
import br.com.velha.tech.repository.RoundGameBoardRepository
import br.com.velha.tech.state.GameUIState
import br.com.velha.tech.viewmodel.common.VelhaTechViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime

@HiltViewModel
class GameViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val roomRepository: RoomRepository,
    private val roomPlayersRepository: RoomPlayersRepository,
    private val roundRepository: RoomRoundRepository,
    private val gameBoardRepository: RoundGameBoardRepository,
    private val commonFirebaseAuthenticationService: CommonFirebaseAuthenticationService,
    savedStateHandle: SavedStateHandle
): VelhaTechViewModel(context) {

    private val _uiState = MutableStateFlow(GameUIState())
    val uiState = _uiState.asStateFlow()

    private val jsonArgs: String? = savedStateHandle[gameScreenArgument]

    init {
        initialLoadUIState()
        loadUIStateWithRoomInfos()
        addAuthenticatedPlayerToRoom()
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
                },
                gameBoardState = _uiState.value.gameBoardState.copy(
                    onInputBoardClick = { rowIndex: Int, columnIndex: Int ->
                        launch {
                            val currentBoard = _uiState.value.gameBoardState.boardFigures
                            val figureInPosition = currentBoard[rowIndex][columnIndex]

                            if (figureInPosition == 0) {
                                val roomId = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!.roomId

                                val newBoard = currentBoard.map { it.copyOf() }.toTypedArray()
                                newBoard[rowIndex][columnIndex] = getPlayerFigure()

                                gameBoardRepository.updateBoard(
                                    roomId = roomId,
                                    boardFigures = newBoard
                                )
                            }
                        }
                    }
                )
            )
        }
    }

    fun getPlayerFigure(): Int {
        val authenticatedPlayer = _uiState.value.players.firstOrNull {
            it.userId == commonFirebaseAuthenticationService.getAuthenticatedUser()!!.id
        }

        return authenticatedPlayer?.figure!!
    }

    private fun loadUIStateWithRoomInfos() {
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

        launch {
            val room = roomRepository.findRoomById(args.roomId)!!

            _uiState.value = _uiState.value.copy(
                title = room.roomName!!,
                gamePlayedRoundsListState = _uiState.value.gamePlayedRoundsListState.copy(
                    totalRounds = room.roundsCount!!,
                    time = getTimeForDifficultLevel(room.difficultLevel!!)
                )
            )
        }
    }

    private fun getTimeForDifficultLevel(level: EnumDifficultLevel): LocalTime {
        return when (level) {
            EnumDifficultLevel.EASY -> LocalTime.of(0, 0, 10)
            EnumDifficultLevel.MEDIUM -> LocalTime.of(0, 0, 5)
            EnumDifficultLevel.HARD -> LocalTime.of(0, 0, 2)
        }
    }

    private fun addAuthenticatedPlayerToRoom() {
        launch {
            val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!
            roomPlayersRepository.addAuthenticatedPlayerToRoom(roomId = args.roomId)
        }
    }

    private fun addRoomPlayerListListener() {
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

        roomPlayersRepository.addRoomPlayerListListener(
            roomId = args.roomId,
            onSuccess = { players ->
                _uiState.value = _uiState.value.copy(
                    subtitle = getSubtitle(players),
                    players = players
                )

                val authenticatedPlayer = players.firstOrNull {
                    it.userId == commonFirebaseAuthenticationService.getAuthenticatedUser()!!.id
                }

                if (players.size == 2 && authenticatedPlayer != null) {
                    startNewRound(authenticatedPlayer, args)
                }
            },
            onError = { exception ->
                onShowError(exception)
                onError(exception)
            }
        )
    }

    private fun startNewRound(authenticatedPlayer: TOPlayer, args: GameScreenArgs) {
        launch {
            val allFinished = roundRepository.getAllRoundsFinished(args.roomId)

            if (authenticatedPlayer.roomOwner && allFinished) {
                roundRepository.startNewRound(roomId = args.roomId, roundNumber = 1)
            }

            addRoundListener(authenticatedPlayer.roomOwner)
        }
    }

    private fun addRoundListener(roomOwner: Boolean) {
        launch {
            val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

            roundRepository.addRoundListener(
                roomId = args.roomId,
                onSuccess = { round ->
                    if (round.timerToStart == 10) {
                        loadGameBoard()
                    }

                    if (round.preparingToStart) {
                        showBlockUIStartingGame(round)

                        if (roomOwner) {
                            reduceTimerStartingGame(args, round)
                        }
                    } else {
                        addBoardListener()
                        hideBlockUI()
                    }
                },
                onError = {
                    onShowError(it)
                    onError(it)
                }
            )
        }
    }

    private fun addBoardListener() {
        launch {
            val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

            gameBoardRepository.addBoardListener(
                roomId = args.roomId,
                onSuccess = { boardFigures ->
                    _uiState.value = _uiState.value.copy(
                        gameBoardState = _uiState.value.gameBoardState.copy(
                            boardFigures = boardFigures
                        )
                    )
                },
                onError = {
                    onShowError(it)
                    onError(it)
                }
            )
        }
    }

    private fun loadGameBoard() {
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

        launch {
            val playersWithFigures = roomPlayersRepository.sortFiguresToPlayers(args.roomId)

            _uiState.value = _uiState.value.copy(
                players = playersWithFigures,
                gameBoardState = _uiState.value.gameBoardState.copy(
                    playersFigure = getFiguresPairList(playersWithFigures)
                )
            )
        }
    }

    private fun getFiguresPairList(playersWithFigures: List<TOPlayer>): List<Pair<String, Drawable>> {
        return playersWithFigures.map {
            Pair(it.userId, ContextCompat.getDrawable(context, it.figure!!)!!)
        }
    }

    private fun showBlockUIStartingGame(round: TORound) {
        _uiState.value = _uiState.value.copy(
            blockUIMessageState = _uiState.value.blockUIMessageState.copy(
                title = context.getString(R.string.game_screen_adversary_player_entered_title),
                message = context.getString(R.string.game_screen_adversary_player_entered_message, round.timerToStart),
                visible = true
            )
        )
    }

    private fun reduceTimerStartingGame(args: GameScreenArgs, round: TORound) {
        launch {
            roundRepository.reduceRoundTimer(args.roomId, round.id!!)
        }
    }

    private fun hideBlockUI() {
        _uiState.value = _uiState.value.copy(
            blockUIMessageState = _uiState.value.blockUIMessageState.copy(
                visible = false
            )
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
        gameBoardRepository.removeBoardListener()
    }

    fun onBackClick(onSuccess: () -> Unit) {
        launch {
            val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!
            val allFinished = roundRepository.getAllRoundsFinished(args.roomId)

            if (allFinished) {
                roomPlayersRepository.removePlayerFromRoom(roomId = args.roomId)
            }

            onSuccess()
        }
    }
}