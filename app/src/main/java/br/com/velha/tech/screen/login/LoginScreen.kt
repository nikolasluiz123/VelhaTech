package br.com.velha.tech.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.velha.tech.R
import br.com.velha.tech.components.buttons.RoundedGoogleButton
import br.com.velha.tech.components.buttons.VelhaTechButton
import br.com.velha.tech.components.buttons.VelhaTechOutlinedButton
import br.com.velha.tech.components.dialog.VelhaTechMessageDialog
import br.com.velha.tech.components.fields.OutlinedTextFieldPasswordValidation
import br.com.velha.tech.components.fields.OutlinedTextFieldValidation
import br.com.velha.tech.components.loading.VelhaTechLinearProgressIndicator
import br.com.velha.tech.core.keyboard.EmailKeyboardOptions
import br.com.velha.tech.core.keyboard.LastPasswordKeyboardOptions
import br.com.velha.tech.core.theme.VelhaTechTheme
import br.com.velha.tech.firebase.apis.analytics.logButtonClick
import br.com.velha.tech.screen.login.callback.OnLoginClick
import br.com.velha.tech.screen.login.callback.OnLoginWithGoogleClick
import br.com.velha.tech.state.LoginUIState
import br.com.velha.tech.screen.login.enums.EnumLoginScreenTags
import br.com.velha.tech.viewmodel.LoginViewModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRoomList: () -> Unit,
    onNavigateToRegisterUser: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LoginScreen(
        state = state,
        onLoginWithGoogleClick = viewModel::onLoginWithGoogleClick,
        onLoginClick = viewModel::onLoginClick,
        onNavigateToRoomList = onNavigateToRoomList,
        onNavigateToRegisterUser = onNavigateToRegisterUser
    )
}

@Composable
fun LoginScreen(
    state: LoginUIState = LoginUIState(),
    onLoginWithGoogleClick: OnLoginWithGoogleClick? = null,
    onLoginClick: OnLoginClick? = null,
    onNavigateToRoomList: () -> Unit = { },
    onNavigateToRegisterUser: () -> Unit = { }
) {
    Scaffold { padding ->
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val (loadingRef, containerRef) = createRefs()

            ConstraintLayout(
                Modifier.fillMaxWidth()
            ) {
                VelhaTechLinearProgressIndicator(
                    state.showLoading,
                    Modifier
                        .constrainAs(loadingRef) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                )
            }

            Column(
                modifier = Modifier
                    .constrainAs(containerRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(loadingRef.bottom, margin = 16.dp)
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                ConstraintLayout(Modifier.fillMaxWidth()) {
                    val (emailRef, passwordRef, loginButtonRef, registerButtonRef, googleButtonRef) = createRefs()

                    VelhaTechMessageDialog(state = state.messageDialogState)

                    OutlinedTextFieldValidation(
                        modifier = Modifier
                            .constrainAs(emailRef) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(parent.top)

                                width = Dimension.fillToConstraints
                            },
                        field = state.email,
                        label = stringResource(R.string.login_screen_label_email),
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
                        label = stringResource(R.string.login_screen_label_password),
                        keyboardOptions = LastPasswordKeyboardOptions,
                    )

                    createHorizontalChain(registerButtonRef, loginButtonRef)

                    VelhaTechButton(
                        modifier = Modifier
                            .constrainAs(loginButtonRef) {
                                start.linkTo(parent.start)
                                top.linkTo(passwordRef.bottom, margin = 8.dp)

                                horizontalChainWeight = 0.45F

                                width = Dimension.fillToConstraints
                            }
                            .padding(start = 8.dp),
                        label = stringResource(R.string.login_screen_label_button_login),
                        onClickListener = {
                            Firebase.analytics.logButtonClick(EnumLoginScreenTags.LOGIN_SCREEN_LOGIN_BUTTON)

                            state.onToggleLoading()

                            onLoginClick?.onExecute(
                                onSuccess = {
                                    onNavigateToRoomList()
                                },
                                onFailure = {
                                    state.onToggleLoading()
                                }
                            )
                        }
                    )

                    VelhaTechOutlinedButton(
                        modifier = Modifier
                            .constrainAs(registerButtonRef) {
                                end.linkTo(parent.end)
                                top.linkTo(passwordRef.bottom, margin = 8.dp)

                                horizontalChainWeight = 0.45F

                                width = Dimension.fillToConstraints
                            },
                        label = stringResource(R.string.login_screen_label_button_register),
                        onClickListener = {
                            Firebase.analytics.logButtonClick(EnumLoginScreenTags.LOGIN_SCREEN_REGISTER_BUTTON)
                            onNavigateToRegisterUser()
                        }
                    )

                    RoundedGoogleButton(
                        modifier = Modifier
                            .constrainAs(googleButtonRef) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(loginButtonRef.bottom)
                            }
                            .padding(end = 8.dp, top = 8.dp),
                        onClick = {
                            Firebase.analytics.logButtonClick(EnumLoginScreenTags.LOGIN_SCREEN_GOOGLE_BUTTON)

                            state.onToggleLoading()

                            onLoginWithGoogleClick?.onExecute(
                                onSuccess = {
                                    state.onToggleLoading()
                                    onNavigateToRoomList()
                                },
                                onFailure = {
                                    state.onToggleLoading()
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun LoginScreenPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            LoginScreen()
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun LoginScreenPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            LoginScreen()
        }
    }
}