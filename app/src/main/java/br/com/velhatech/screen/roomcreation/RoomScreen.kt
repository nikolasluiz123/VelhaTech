package br.com.velhatech.screen.roomcreation

import android.content.Context
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.velhatech.R
import br.com.velhatech.components.buttons.fab.FloatingActionButtonSave
import br.com.velhatech.components.buttons.radio.RadioButtonSession
import br.com.velhatech.components.dialog.VelhaTechMessageDialog
import br.com.velhatech.components.fields.OutlinedTextFieldPasswordValidation
import br.com.velhatech.components.fields.OutlinedTextFieldValidation
import br.com.velhatech.components.loading.VelhaTechLinearProgressIndicator
import br.com.velhatech.components.topbar.SimpleVelhaTechTopAppBar
import br.com.velhatech.core.keyboard.NormalTextKeyboardOptions
import br.com.velhatech.core.keyboard.PasswordKeyboardOptions
import br.com.velhatech.core.theme.SnackBarTextStyle
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.navigation.GameScreenArgs
import br.com.velhatech.screen.common.callback.OnSaveLessFailureCallback
import br.com.velhatech.screen.game.callback.OnNavigateToGame
import br.com.velhatech.state.RoomUIState
import br.com.velhatech.viewmodel.RoomViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun RoomScreen(
    viewModel: RoomViewModel,
    onBackClick: () -> Unit,
    onNavigateToGame: OnNavigateToGame
) {
    val state by viewModel.uiState.collectAsState()

    RoomScreen(
        state = state,
        onBackClick = onBackClick,
        onSave = viewModel::saveRoom,
        onNavigateToGame = onNavigateToGame
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    state: RoomUIState = RoomUIState(),
    onBackClick: () -> Unit = { },
    onSave: OnSaveLessFailureCallback? = null,
    onNavigateToGame: OnNavigateToGame? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SimpleVelhaTechTopAppBar(
                title = stringResource(R.string.room_screen_title),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButtonSave(
                onClick = {
                    state.onToggleLoading()

                    onSave?.onExecute(
                        onSuccess = {
                            showSaveSuccessMessage(coroutineScope, snackbarHostState, context)
                            onNavigateToGame?.onExecute(GameScreenArgs(state.toRoom.id!!))
                        },
                        onCompleted = {
                            state.onToggleLoading()
                        }
                    )
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
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (loadingRef, containerRef) = createRefs()

            ConstraintLayout(Modifier.fillMaxWidth()) {
                VelhaTechLinearProgressIndicator(
                    state.showLoading,
                    Modifier.constrainAs(loadingRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                )
            }

            ConstraintLayout(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .constrainAs(containerRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(loadingRef.bottom, margin = 16.dp)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                val (roomNameRef, passwordRef, roundsRef, difficultLevelRef) = createRefs()

                VelhaTechMessageDialog(state = state.messageDialogState)

                OutlinedTextFieldValidation(
                    field = state.roomName,
                    label = stringResource(R.string.room_screen_label_room_name),
                    keyboardOptions = NormalTextKeyboardOptions,
                    modifier = Modifier.constrainAs(roomNameRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints
                    }
                )

                OutlinedTextFieldPasswordValidation(
                    field = state.roomPassword,
                    label = stringResource(R.string.room_screen_label_password),
                    keyboardOptions = PasswordKeyboardOptions,
                    modifier = Modifier.constrainAs(passwordRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(roomNameRef.bottom, margin = 8.dp)

                        width = Dimension.fillToConstraints
                    }
                )

                RadioButtonSession(
                    sessionLabel = stringResource(R.string.room_screen_label_rounds),
                    field = state.rounds,
                    modifier = Modifier.constrainAs(roundsRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(passwordRef.bottom, margin = 8.dp)
                    }
                )

                RadioButtonSession(
                    sessionLabel = stringResource(R.string.room_screen_label_difficult_level),
                    field = state.difficultLevel,
                    modifier = Modifier.constrainAs(difficultLevelRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(roundsRef.bottom, margin = 8.dp)
                    }
                )
            }
        }
    }
}

private fun showSaveSuccessMessage(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    coroutineScope.launch {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.room_screen_success_message)
        )
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomScreenPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            RoomScreen(
                state = defaultRoomState
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomScreenPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            RoomScreen(state = defaultRoomState)
        }
    }
}