package br.com.velha.tech.screen.game

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.velha.tech.R
import br.com.velha.tech.components.bottombar.VelhaTechBottomAppBar
import br.com.velha.tech.components.dialog.VelhaTechMessageDialog
import br.com.velha.tech.components.topbar.SimpleVelhaTechTopAppBar
import br.com.velha.tech.core.callback.showConfirmationDialog
import br.com.velha.tech.core.theme.SnackBarTextStyle
import br.com.velha.tech.core.theme.VelhaTechTheme
import br.com.velha.tech.state.GameUIState
import br.com.velha.tech.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    GameScreen(
        state = state,
        snackbarMessageFlow = viewModel.snackbarMessage,
        onBackClick = {
            viewModel.onBackClick(onBackClick)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameUIState = GameUIState(),
    snackbarMessageFlow: SharedFlow<String>? = null,
    onBackClick : () -> Unit = { },
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        onBackClickWithConfirmation(state, context, onBackClick)
    }

    Scaffold(
        topBar = {
            SimpleVelhaTechTopAppBar(
                title = state.title,
                subtitle = state.subtitle,
                onBackClick = {
                    onBackClickWithConfirmation(state, context, onBackClick)
                }
            )
        },
        bottomBar = {
            VelhaTechBottomAppBar(
                floatingActionButton = {
                    if (state.isPaused) {
                        FloatingActionButtonPlay(
                            onClick = {
                                state.onChangePaused(false)
                            }
                        )
                    } else {
                        FloatingActionButtonPause(
                            onClick = {
                                state.onChangePaused(true)
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(modifier = Modifier.padding(8.dp)) {
                    Text(text = it.visuals.message, style = SnackBarTextStyle)
                }
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (gameBoardRef, playedRoundsRef, blockUIRef) = createRefs()

            LaunchedEffect(snackbarMessageFlow) {
                snackbarMessageFlow?.collectLatest { message ->
                    coroutineScope.launch {
                        val job = launch {
                            snackbarHostState.showSnackbar(message = message)
                        }

                        delay(1000)
                        job.cancel()
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }

            VelhaTechMessageDialog(state = state.messageDialogState)

            TopBarExpandableList(
                state = state.gamePlayedRoundsListState,
                modifier = Modifier.constrainAs(playedRoundsRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            GameBoard(
                state = state.gameBoardState,
                modifier = Modifier.constrainAs(gameBoardRef) {
                    top.linkTo(playedRoundsRef.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            BlockUI(
                state = state.blockUIMessageState,
                modifier = Modifier.constrainAs(blockUIRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

private fun onBackClickWithConfirmation(state: GameUIState, context: Context, onBackClick: () -> Unit) {
    state.messageDialogState.onShowDialog?.showConfirmationDialog(
        message = context.getString(R.string.game_screen_room_exit_confirm_message),
        onConfirm = {
            onBackClick()
        }
    )
}

@Preview(device = "id:small_phone")
@Composable
private fun GameScreenPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            GameScreen(
                state = GameUIState(
                    title = "Sala da Loucura",
                    subtitle = "Nikolas vs Josnei",
                    gamePlayedRoundsListState = gamePlayedRoundsListStateThreeRoundsClosed,
                    gameBoardState = gameBoardFinishedState
                )
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun GameScreenPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            GameScreen(
                state = GameUIState(
                    title = "Sala da Loucura",
                    subtitle = "Nikolas vs Josnei",
                    gamePlayedRoundsListState = gamePlayedRoundsListStateThreeRoundsClosed,
                    gameBoardState = gameBoardFinishedState
                )
            )
        }
    }
}