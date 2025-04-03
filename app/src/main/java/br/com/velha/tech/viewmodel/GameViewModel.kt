package br.com.velha.tech.viewmodel

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import br.com.velha.tech.R
import br.com.velha.tech.core.callback.showCustomDialog
import br.com.velha.tech.core.callback.showErrorDialog
import br.com.velha.tech.core.enums.EnumDialogType
import br.com.velha.tech.core.extensions.fromJsonNavParamToArgs
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.firebase.auth.implementations.CommonFirebaseAuthenticationService
import br.com.velha.tech.firebase.to.TOPlayer
import br.com.velha.tech.firebase.to.TORound
import br.com.velha.tech.navigation.GameScreenArgs
import br.com.velha.tech.navigation.gameScreenArgument
import br.com.velha.tech.repository.RoomPlayersRepository
import br.com.velha.tech.repository.RoomRepository
import br.com.velha.tech.repository.RoomRoundRepository
import br.com.velha.tech.repository.RoundGameBoardRepository
import br.com.velha.tech.state.GamePlayedRoundsListState
import br.com.velha.tech.state.GameRoundItem
import br.com.velha.tech.state.GameUIState
import br.com.velha.tech.viewmodel.common.VelhaTechViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    private val jsonArgs: String? = savedStateHandle[gameScreenArgument]

    init {
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!
        val authenticatedUserId = getAuthenticatedUserId()!!

        initialLoadUIState()
        loadUIStateWithRoomInfos(args)
        addAuthenticatedPlayerToRoom(args)
        addRoomPlayerListListener(args)
        addPlayerListener(roomId = args.roomId, playerId = authenticatedUserId)
        addPlayerTimerListener(roomId = args.roomId, playerId = authenticatedUserId)
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
                    onInputBoardClick = ::onInputBoardClick
                ),
            )
        }
    }

    private fun onInputBoardClick(rowIndex: Int, columnIndex: Int) {
        launch {
            val authenticatedUserId = getAuthenticatedUserId()
            val player = _uiState.value.players.first { it.userId == authenticatedUserId }

            if (player.playing) {
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

                    selectPlayerToPlay(roomId)
                }
            }
        }
    }

    fun getPlayerFigure(): Int {
        val authenticatedPlayer = _uiState.value.players.firstOrNull {
            it.userId == getAuthenticatedUserId()
        }

        return authenticatedPlayer?.figure!!
    }

    private fun loadUIStateWithRoomInfos(args: GameScreenArgs) {
        launch {
            val room = roomRepository.findRoomById(args.roomId)!!

            _uiState.value = _uiState.value.copy(
                title = room.roomName!!,
                gamePlayedRoundsListState = _uiState.value.gamePlayedRoundsListState.copy(
                    totalRounds = room.roundsCount!!,
                )
            )
        }
    }

    private fun addAuthenticatedPlayerToRoom(args: GameScreenArgs) {
        launch {
            roomPlayersRepository.addAuthenticatedPlayerToRoom(roomId = args.roomId)
        }
    }

    private fun addRoomPlayerListListener(args: GameScreenArgs) {
        roomPlayersRepository.addRoomPlayerListListener(
            roomId = args.roomId,
            onSuccess = { players ->
                val authenticatedPlayer = getAuthenticatedPlayer(players)

                _uiState.value = _uiState.value.copy(
                    subtitle = getSubtitle(players),
                    players = players,
                )

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

    private fun addPlayerListener(playerId: String, roomId: String) {
        roomPlayersRepository.addPlayerListener(
            roomId = roomId,
            playerId = playerId,
            onSuccess = {
                launch {
                    if (it.playing) {
                        val message = context.getString(
                            R.string.game_screen_message_your_round_start,
                            it.name.split(" ").first()
                        )

                        _snackbarMessage.emit(message)

                        roomPlayersRepository.reducePlayerTimer(roomId, playerId)
                    }
                }
            },
            onError = {
                onShowError(it)
                onError(it)
            }
        )
    }

    private fun addPlayerTimerListener(playerId: String, roomId: String) {
        roomPlayersRepository.addPlayerTimerListener(
            roomId = roomId,
            playerId = playerId,
            onSuccess = { timer ->
                _uiState.value = _uiState.value.copy(
                    gamePlayedRoundsListState = _uiState.value.gamePlayedRoundsListState.copy(
                        time = timer
                    )
                )
            },
            onError = {
                onShowError(it)
                onError(it)
            }
        )
    }

    private fun getAuthenticatedPlayer(players: List<TOPlayer>): TOPlayer? = players.firstOrNull {
        it.userId == getAuthenticatedUserId()
    }

    private fun getAuthenticatedUserId(): String? {
        return commonFirebaseAuthenticationService.getAuthenticatedUser()!!.id
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
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

        roundRepository.addRoundListener(
            roomId = args.roomId,
            onSuccess = { round ->
                onRoundChangeListener(round, args, roomOwner)
            },
            onError = {
                onShowError(it)
                onError(it)
            }
        )
    }

    private fun onRoundChangeListener(round: TORound, args: GameScreenArgs, roomOwner: Boolean) {
        launch {
            loadGameBoard(args.roomId)
            addRoundTimerListener(roomId = args.roomId, roundId = round.id!!, roomOwner = roomOwner)
            addPlayerListener(roomId = args.roomId, playerId = getAuthenticatedUserId()!!)
            addPlayerTimerListener(roomId = args.roomId, playerId = getAuthenticatedUserId()!!)

            if (roomOwner && round.preparingToStart) {
                reduceTimerStartingGame(args.roomId, round.id!!)
            }

            if (round.playing) {
                addBoardListener(args.roomId)
            }

//            if (round.finished) {
//                _uiState.value = _uiState.value.copy(
//                    gamePlayedRoundsListState = _uiState.value.gamePlayedRoundsListState.copy(
//                        playedRounds = getPlayedRounds(round)
//                    )
//                )
//            }
        }
    }

    private fun getPlayedRounds(round: TORound): List<GameRoundItem> {
        val winnerPlayer = _uiState.value.players.firstOrNull { it.userId == round.winnerPlayerId }
        val item = GameRoundItem(roundNumber = round.roundNumber!!, winnerPlayer = winnerPlayer)

        val playedRounds = _uiState.value.gamePlayedRoundsListState.playedRounds.toMutableList()
        playedRounds.add(item)

        return playedRounds
    }

    private suspend fun addRoundTimerListener(roomId: String, roundId: String, roomOwner: Boolean) {
        roundRepository.addRoundTimerListener(
            roomId = roomId,
            roundId = roundId,
            onSuccess = { timer ->
                if (timer > 0) {
                    showBlockUIStartingGame(timer)
                } else {
                    launch {
                        hideBlockUI()

                        if (roomOwner) {
                            selectPlayerToPlay(roomId)
                        }
                    }
                }
            },
            onError = {
                onShowError(it)
                onError(it)
            }
        )
    }

    private suspend fun selectPlayerToPlay(roomId: String) {
        roomPlayersRepository.selectPlayerToPlay(roomId)
    }

    private suspend fun addBoardListener(roomId: String) {
        gameBoardRepository.addBoardListener(
            roomId = roomId,
            onSuccess = { boardFigures ->
                _uiState.value = _uiState.value.copy(
                    gameBoardState = _uiState.value.gameBoardState.copy(
                        boardFigures = boardFigures
                    )
                )

                verifyWinner(boardFigures)
            },
            onError = {
                onShowError(it)
                onError(it)
            }
        )
    }

    private fun verifyWinner(boardFigures: Array<Array<Int>>) {
        val figureWinner = getWinnerFigure(boardFigures)
        val fullBoard = isFullBoard(boardFigures)
        val gamePlayedRoundsListState = _uiState.value.gamePlayedRoundsListState

        val totalRounds = gamePlayedRoundsListState.totalRounds
        val playedRounds = gamePlayedRoundsListState.playedRounds
        val decisiveRounds = if (totalRounds % 2 != 0) (totalRounds / 2) + 1 else null

        if (decisiveRounds != null && playedRounds.size >= decisiveRounds) {
//            processGameFinalization(
//                playedRounds = playedRounds,
//                decisiveRounds = decisiveRounds,
//                totalRounds = totalRounds,
//                figureWinner = figureWinner,
//                gamePlayedRoundsListState = gamePlayedRoundsListState,
//                fullBoard = fullBoard
//            )

        } else {
            processRoundFinalization(
                figureWinner = figureWinner,
                gamePlayedRoundsListState = gamePlayedRoundsListState,
                fullBoard = fullBoard
            )
        }
    }

    private fun processGameFinalization(
        playedRounds: List<GameRoundItem>,
        decisiveRounds: Int,
        totalRounds: Int,
        figureWinner: Int?,
        gamePlayedRoundsListState: GamePlayedRoundsListState,
        fullBoard: Boolean
    ) {
        val roomId = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!.roomId
        val authenticatedPlayer = getAuthenticatedPlayer(_uiState.value.players)!!
        val authenticatedPlayerWins = playedRounds.count { it.winnerPlayer?.userId == authenticatedPlayer.userId }
        val authenticatedPlayerLooses = playedRounds.count { it.winnerPlayer?.userId != authenticatedPlayer.userId && it.winnerPlayer != null }
        val drawRounds = playedRounds.count { it.winnerPlayer == null }

        when {
            authenticatedPlayerWins >= decisiveRounds -> {
                val winnerUserId = authenticatedPlayer.userId
                showMessageGameWinner()
                finishGame(roomId, winnerUserId)
            }

            authenticatedPlayerLooses >= decisiveRounds -> {
                val winnerUserId = _uiState.value.players.first { it.userId != authenticatedPlayer.userId }.userId
                showMessageGameLoser()
                finishGame(roomId, winnerUserId)
            }

            drawRounds >= decisiveRounds && playedRounds.size < totalRounds -> {
                processRoundFinalization(figureWinner, gamePlayedRoundsListState, fullBoard)
            }

            else -> {
                showMessageGameDraw()
                finishGame(roomId)
            }
        }
    }

    private fun finishGame(roomId: String, winnerUserId: String? = null) {
        launch {
            roomRepository.finishGame(roomId = roomId, winnerUserId = winnerUserId)
        }
    }

    private fun processRoundFinalization(
        figureWinner: Int?,
        gamePlayedRoundsListState: GamePlayedRoundsListState,
        fullBoard: Boolean
    ) {
        val authenticatedPlayer = getAuthenticatedPlayer(_uiState.value.players)!!

        if (figureWinner != null) {
            val winner = _uiState.value.players.first { it.figure!! == figureWinner }

//            if (winner.userId == authenticatedPlayer.userId) {
//                showMessageRoundWinner(gamePlayedRoundsListState.roundsToPlay)
//            } else {
//                showMessageRoundLoser(gamePlayedRoundsListState.roundsToPlay)
//            }

            prepareNextRound(authenticatedPlayer, winner)
        } else if (fullBoard) {
//            showMessageRoundDraw(gamePlayedRoundsListState.roundsToPlay)
//            prepareNextRound(authenticatedPlayer, null)
        }
    }

    private fun prepareNextRound(authenticatedPlayer: TOPlayer, winner: TOPlayer?) {
        launch {
            val roomId = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!.roomId

            if (authenticatedPlayer.roomOwner) {
                roundRepository.prepareNextRound(roomId, winner)
            }

            resetListenersNextRound(authenticatedPlayer)
            clearGameBoardNextRound()
        }
    }

    private fun resetListenersNextRound(authenticatedPlayer: TOPlayer) {
        val args = jsonArgs?.fromJsonNavParamToArgs(GameScreenArgs::class.java)!!

        roomPlayersRepository.removeRoomPlayerListListener()
        roomPlayersRepository.removePlayerListener()
        roomPlayersRepository.removePlayerTimerListener()
        roundRepository.removeRoundListener()
        roundRepository.removeRoundTimerListener()
        gameBoardRepository.removeBoardListener()

        addRoomPlayerListListener(args)
        addPlayerListener(roomId = args.roomId, playerId = getAuthenticatedUserId()!!)
        addPlayerTimerListener(roomId = args.roomId, playerId = getAuthenticatedUserId()!!)
        addRoundListener(authenticatedPlayer.roomOwner)
    }

    private fun clearGameBoardNextRound() {
        _uiState.value = _uiState.value.copy(
            gameBoardState = _uiState.value.gameBoardState.copy(
                boardFigures = Array(3) { arrayOf(0,0,0) }
            )
        )
    }

    private fun getWinnerFigure(boardFigures: Array<Array<Int>>): Int? {
        val size = boardFigures.size

        for (i in 0 until size) {
            if (isLineFull(boardFigures, i)) {
                return boardFigures[i][0]
            }

            if (isColumnFull(boardFigures, i, size)) {
                return boardFigures[0][i]
            }
        }

        if (isMainDiagonalFull(boardFigures, size)) {
            return boardFigures[0][0]
        }

        if (isSecondaryDiagonalFull(boardFigures, size)) {
            return boardFigures[0][size - 1]
        }

        return null
    }

    private fun isFullBoard(boardFigures: Array<Array<Int>>): Boolean {
        return boardFigures.all { row -> row.all { it != 0 } }
    }

    private fun isLineFull(boardFigures: Array<Array<Int>>, i: Int): Boolean {
        return boardFigures[i][0] != 0 && boardFigures[i].all { it == boardFigures[i][0] }
    }

    private fun isColumnFull(boardFigures: Array<Array<Int>>, i: Int, size: Int): Boolean {
        return boardFigures[0][i] != 0 &&
                (0 until size).all { boardFigures[it][i] == boardFigures[0][i] }
    }

    private fun isMainDiagonalFull(boardFigures: Array<Array<Int>>, size: Int): Boolean {
        return boardFigures[0][0] != 0 &&
                (0 until size).all { boardFigures[it][it] == boardFigures[0][0] }
    }

    private fun isSecondaryDiagonalFull(boardFigures: Array<Array<Int>>, size: Int): Boolean {
        return boardFigures[0][size - 1] != 0 &&
                (0 until size).all { boardFigures[it][size - 1 - it] == boardFigures[0][size - 1] }
    }

    private fun showMessageRoundWinner(roundsToPlay: Int) {
        _uiState.value.messageDialogState.onShowDialog?.showCustomDialog(
            type = EnumDialogType.INFORMATION,
            customTitle = context.getString(R.string.game_screen_winner_title),
            message = context.getString(R.string.game_screen_round_winner_message, roundsToPlay),
            onConfirm = { },
            onCancel = { }
        )
    }

    private fun showMessageRoundLoser(roundsToPlay: Int) {
        _uiState.value.messageDialogState.onShowDialog?.showCustomDialog(
            type = EnumDialogType.INFORMATION,
            customTitle = context.getString(R.string.game_screen_loser_title),
            message = context.getString(R.string.game_screen_loser_message, roundsToPlay),
            onConfirm = { },
            onCancel = { }
        )
    }

    private fun showMessageRoundDraw(roundsToPlay: Int) {
        _uiState.value.messageDialogState.onShowDialog?.showCustomDialog(
            type = EnumDialogType.INFORMATION,
            customTitle = context.getString(R.string.game_screen_draw_title),
            message = context.getString(R.string.game_screen_draw_message, roundsToPlay),
            onConfirm = { },
            onCancel = { }
        )
    }

    private fun showMessageGameWinner() {
        _uiState.value.messageDialogState.onShowDialog?.showCustomDialog(
            type = EnumDialogType.INFORMATION,
            customTitle = context.getString(R.string.game_screen_winner_title),
            message = context.getString(R.string.game_screen_game_winner_message),
            onConfirm = { },
            onCancel = { }
        )
    }

    private fun showMessageGameLoser() {
        _uiState.value.messageDialogState.onShowDialog?.showCustomDialog(
            type = EnumDialogType.INFORMATION,
            customTitle = context.getString(R.string.game_screen_loser_title),
            message = context.getString(R.string.game_screen_game_loser_message),
            onConfirm = { },
            onCancel = { }
        )
    }

    private fun showMessageGameDraw() {
        _uiState.value.messageDialogState.onShowDialog?.showCustomDialog(
            type = EnumDialogType.INFORMATION,
            customTitle = context.getString(R.string.game_screen_draw_title),
            message = context.getString(R.string.game_screen_game_draw_message),
            onConfirm = { },
            onCancel = { }
        )
    }

    private suspend fun loadGameBoard(roomId: String) {
        val playersWithFigures = roomPlayersRepository.sortFiguresToPlayers(roomId)

        _uiState.value = _uiState.value.copy(
            players = playersWithFigures,
            gameBoardState = _uiState.value.gameBoardState.copy(
                playersFigure = getFiguresPairList(playersWithFigures)
            )
        )
    }

    private fun getFiguresPairList(playersWithFigures: List<TOPlayer>): List<Pair<String, Drawable>> {
        return playersWithFigures.map {
            Pair(it.userId, ContextCompat.getDrawable(context, it.figure!!)!!)
        }
    }

    private fun showBlockUIStartingGame(timerToStart: Int?) {
        _uiState.value = _uiState.value.copy(
            blockUIMessageState = _uiState.value.blockUIMessageState.copy(
                title = context.getString(R.string.game_screen_adversary_player_entered_title),
                message = context.getString(R.string.game_screen_adversary_player_entered_message, timerToStart),
                visible = true
            )
        )
    }

    private suspend fun reduceTimerStartingGame(roomId: String, roundId: String) {
        roundRepository.reduceRoundTimer(roomId, roundId)
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

    override fun onShowError(throwable: Throwable) {
        _uiState.value.messageDialogState.onShowDialog?.showErrorDialog(
            message = context.getString(R.string.unknown_error_message)
        )
    }

    override fun onCleared() {
        super.onCleared()
        roomPlayersRepository.removeRoomPlayerListListener()
        roomPlayersRepository.removePlayerListener()
        roomPlayersRepository.removePlayerTimerListener()
        roundRepository.removeRoundListener()
        roundRepository.removeRoundTimerListener()
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