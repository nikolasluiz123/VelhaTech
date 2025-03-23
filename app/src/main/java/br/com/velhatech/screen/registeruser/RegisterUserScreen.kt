package br.com.velhatech.screen.registeruser

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import br.com.velhatech.components.dialog.VelhaTechMessageDialog
import br.com.velhatech.components.fields.OutlinedTextFieldPasswordValidation
import br.com.velhatech.components.fields.OutlinedTextFieldValidation
import br.com.velhatech.components.loading.VelhaTechLinearProgressIndicator
import br.com.velhatech.components.topbar.SimpleVelhaTechTopAppBar
import br.com.velhatech.core.keyboard.EmailKeyboardOptions
import br.com.velhatech.core.keyboard.LastPasswordKeyboardOptions
import br.com.velhatech.core.keyboard.PersonNameKeyboardOptions
import br.com.velhatech.core.theme.SnackBarTextStyle
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.screen.common.callback.OnSaveLessFailureCallback
import br.com.velhatech.state.RegisterUserUIState
import br.com.velhatech.viewmodel.RegisterUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RegisterUserScreen(
    viewModel: RegisterUserViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    RegisterUserScreen(
        state = state,
        onBackClick = onBackClick,
        onSave = viewModel::saveUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserScreen(
    state: RegisterUserUIState = RegisterUserUIState(),
    onBackClick: () -> Unit = { },
    onSave: OnSaveLessFailureCallback? = null,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SimpleVelhaTechTopAppBar(
                title = state.title,
                onBackClick = onBackClick,
            )
        },
        floatingActionButton = {
            FloatingActionButtonSave(
                onClick = {
                    state.onToggleLoading()

                    onSave?.onExecute(
                        onSuccess = {
                            showSaveSuccessMessage(coroutineScope, snackbarHostState, context)
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
                val (nameRef, emailRef, passwordRef) = createRefs()

                VelhaTechMessageDialog(state = state.messageDialogState)

                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .constrainAs(nameRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)

                            width = Dimension.fillToConstraints
                        },
                    field = state.email,
                    label = stringResource(R.string.register_user_screen_label_name),
                    keyboardOptions = PersonNameKeyboardOptions,
                )

                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .constrainAs(emailRef) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(nameRef.bottom, margin = 8.dp)

                            width = Dimension.fillToConstraints
                        },
                    field = state.email,
                    label = stringResource(R.string.register_user_screen_label_email),
                    keyboardOptions = EmailKeyboardOptions,
                )

                OutlinedTextFieldPasswordValidation(
                    modifier = Modifier
                        .constrainAs(passwordRef) {
                            start.linkTo(emailRef.start)
                            end.linkTo(emailRef.end)
                            top.linkTo(emailRef.bottom, margin = 8.dp)

                            width = Dimension.fillToConstraints
                        },
                    field = state.password,
                    label = stringResource(R.string.register_user_login_screen_label_password),
                    keyboardOptions = LastPasswordKeyboardOptions,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            state.onToggleLoading()

                            onSave?.onExecute(
                                onSuccess = {
                                    showSaveSuccessMessage(coroutineScope, snackbarHostState, context)
                                },
                                onCompleted = {
                                    state.onToggleLoading()
                                }
                            )
                        }
                    )
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
            message = context.getString(R.string.register_user_screen_success_message)
        )
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RegisterUserScreenPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            RegisterUserScreen(state = RegisterUserUIState(title = "Novo Jogador"))
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RegisterUserScreenPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            RegisterUserScreen(state = RegisterUserUIState(title = "Novo Jogador"))
        }
    }
}