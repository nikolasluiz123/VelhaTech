package br.com.velha.tech.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.velha.tech.components.dialog.enums.EnumVelhaTechMessageDialogTestTags
import br.com.velha.tech.compose.components.R
import br.com.velha.tech.core.enums.EnumDialogType
import br.com.velha.tech.core.enums.EnumDialogType.*
import br.com.velha.tech.core.state.MessageDialogState
import br.com.velha.tech.core.theme.VelhaTechTheme
import br.com.velha.tech.firebase.apis.analytics.logButtonClick
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


@Composable
fun VelhaTechMessageDialog(state: MessageDialogState) {
    VelhaTechMessageDialog(
        type = state.dialogType,
        show = state.showDialog,
        onDismissRequest = state.onHideDialog,
        message = state.dialogMessage,
        onConfirm = state.onConfirm,
        onCancel = state.onCancel
    )
}

/**
 * Dialog genérica para exibir qualquer mensagem necessária de acordo com as regras da tela
 * que estiver sendo implementada.
 *
 * @param type Tipo da Dialog, baseado nesse tipo são feitas configurações específicas.
 * @param show Indica se a Dialog deve ser exibida.
 * @param onDismissRequest Callback para fechar a Dialog.
 * @param message Mensagem a ser exibida na Dialog.
 * @param onConfirm Callback para confirmar a Dialog.
 * @param onCancel Callback para cancelar a Dialog.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun VelhaTechMessageDialog(
    type: EnumDialogType,
    show: Boolean,
    onDismissRequest: () -> Unit,
    message: String,
    onConfirm: () -> Unit = { },
    onCancel: () -> Unit = { }
) {
    val scrollState = rememberScrollState()

    if (show) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = stringResource(type.titleResId),
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = {
                Box(modifier = Modifier.verticalScroll(state = scrollState)) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                when (type) {
                    ERROR, INFORMATION -> {
                        DialogTextButton(
                            labelResId = R.string.label_ok,
                            onDismissRequest = onDismissRequest,
                            onClick = {
                                Firebase.analytics.logButtonClick(EnumVelhaTechMessageDialogTestTags.VELHA_TECH_MESSAGE_DIALOG_OK_BUTTON)
                                onConfirm()
                            }
                        )
                    }

                    CONFIRMATION -> {
                        DialogTextButton(
                            labelResId = R.string.label_confirm,
                            onDismissRequest = onDismissRequest,
                            onClick = {
                                Firebase.analytics.logButtonClick(EnumVelhaTechMessageDialogTestTags.VELHA_TECH_MESSAGE_DIALOG_CONFIRM_BUTTON)
                                onConfirm()
                            }
                        )
                    }
                }
            },
            dismissButton = {
                when (type) {
                    CONFIRMATION -> {
                        DialogTextButton(
                            labelResId = R.string.label_cancel,
                            onDismissRequest = onDismissRequest,
                            onClick = {
                                Firebase.analytics.logButtonClick(EnumVelhaTechMessageDialogTestTags.VELHA_TECH_MESSAGE_DIALOG_CANCEL_BUTTON)
                                onCancel()
                            }
                        )
                    }

                    ERROR, INFORMATION -> {}
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            textContentColor = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun DialogTextButton(
    labelResId: Int,
    onDismissRequest: () -> Unit,
    onClick: () -> Unit
) {
    TextButton(
        onClick = {
            onDismissRequest()
            onClick()
        }
    ) {
        Text(text = stringResource(id = labelResId))
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun VelhaTechDialogMessageErrorPreview() {
    VelhaTechTheme {
        Surface {
            VelhaTechMessageDialog(
                type = ERROR,
                show = true,
                onDismissRequest = { },
                message = "Mensagem de erro"
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun VelhaTechDialogMessageConfirmationPreview() {
    VelhaTechTheme {
        Surface {
            VelhaTechMessageDialog(
                type = CONFIRMATION,
                show = true,
                onDismissRequest = { },
                message = "Mensagem de confirmação"
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun VelhaTechDialogMessageInformationPreview() {
    VelhaTechTheme {
        Surface {
            VelhaTechMessageDialog(
                type = INFORMATION,
                show = true,
                onDismissRequest = { },
                message = "Mensagem de informação"
            )
        }
    }
}