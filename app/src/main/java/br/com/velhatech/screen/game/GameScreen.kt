package br.com.velhatech.screen.game

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.velhatech.R
import br.com.velhatech.components.bottombar.VelhaTechBottomAppBar
import br.com.velhatech.components.dialog.VelhaTechMessageDialog
import br.com.velhatech.components.topbar.SimpleVelhaTechTopAppBar
import br.com.velhatech.core.callback.showConfirmationDialog
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.state.GameUIState
import br.com.velhatech.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    GameScreen(
        state = state,
        onBackClick = {
            viewModel.onBackClick(onBackClick)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    state: GameUIState = GameUIState(),
    onBackClick : () -> Unit = { },
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleVelhaTechTopAppBar(
                title = state.title,
                subtitle = state.subtitle,
                onBackClick = {
                    state.messageDialogState.onShowDialog?.showConfirmationDialog(
                        message = context.getString(R.string.game_screen_room_exit_confirm_message),
                        onConfirm = {
                            onBackClick()
                        }
                    )
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
        contentWindowInsets = WindowInsets(0.dp)
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (gameBoardRef, playedRoundsRef) = createRefs()

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
        }
    }
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