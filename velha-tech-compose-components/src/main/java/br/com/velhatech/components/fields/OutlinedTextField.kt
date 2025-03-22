package br.com.velhatech.components.fields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import br.com.velhatech.components.fields.state.ITextField
import br.com.velhatech.components.fields.state.TextField
import br.com.velhatech.compose.components.R
import br.com.velhatech.core.theme.FieldErrorTextStyle
import br.com.velhatech.core.theme.InputTextStyle
import br.com.velhatech.core.theme.VelhaTechTheme
import kotlin.properties.Delegates

@Composable
fun OutlinedTextFieldValidation(
    field: ITextField,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = field.errorMessage.isNotEmpty(),
    trailingIcon: @Composable (() -> Unit)? = {
        if (field.errorMessage.isNotEmpty())
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "error",
            )
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    maxLength: Int? = null,
) {
    OutlinedTextFieldValidation(
        value = field.value,
        onValueChange = field.onChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = { Text(text = label, style = InputTextStyle) },
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        error = field.errorMessage,
        isError = isError,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        maxLength = maxLength,
    )
}

@Composable
fun OutlinedTextFieldValidation(
    field: TextField,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = field.errorMessage.isNotEmpty(),
    trailingIcon: @Composable (() -> Unit)? = {
        if (field.errorMessage.isNotEmpty())
            Icon(
                Icons.Default.Warning,
                "error",
            )
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
) {
    OutlinedTextFieldValidation(
        value = field.value,
        onValueChange = field.onChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        error = field.errorMessage,
        isError = isError,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
    )
}

/**
 * Campo de texto com comportamentos necessários para feedback de validações.
 *
 * @param value Valor digitado.
 * @param onValueChange Listener executado ao alterar o valor.
 * @param modifier Modificadores do componente.
 * @param enabled Flag para habilitar ou desabilitar o campo.
 * @param readOnly Flag para bloquear a escrita.
 * @param textStyle Estilo do texto.
 * @param label Label do campo.
 * @param placeholder Texto exibido como dica.
 * @param leadingIcon Ícone a direita.
 * @param error Mensagem de erro de alguma validação.
 * @param isError Flag que indica se o campo está com erro.
 * @param trailingIcon Ícone a esquerda do campo.
 * @param visualTransformation Alterações visuais da saída do texto digitado no campo.
 * @param keyboardOptions Opções de teclado.
 * @param keyboardActions Ações específicas que deverão ser exibidas no teclado.
 * @param singleLine Flag que indica se o campo se expandirá ao digitar mais que uma linha.
 * @param maxLines Número máximo de linhas que o campo poderá se expandir. Caso single line seja false.
 * @param interactionSource Personalização da aparência ou comportamento do campo dependendo do estado.
 * @param shape Forma do campo.
 * @param colors Cores do campo.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun OutlinedTextFieldValidation(
    value: String?,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = InputTextStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    error: String = "",
    isError: Boolean = error.isNotEmpty(),
    trailingIcon: @Composable (() -> Unit)? = {
        if (error.isNotEmpty())
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "error",
            )
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    maxLength: Int? = null,
    writeCondition: Boolean = true
) {
    OutlinedTextField(
        enabled = enabled,
        readOnly = readOnly,
        modifier = modifier,
        value = value ?: "",
        onValueChange = {
            if(maxLength == null || it.length <= maxLength) {
                onValueChange(it)
            }
        },
        singleLine = singleLine,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        supportingText = {
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = FieldErrorTextStyle
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
    )
}

@Composable
fun OutlinedTextFieldPasswordValidation(
    field: TextField,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    maxLength: Int? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextFieldPasswordValidation(
        value = field.value,
        onValueChange = field.onChange,
        error = field.errorMessage,
        modifier = modifier,
        label = { Text(text = label, style = InputTextStyle) },
        keyboardOptions = keyboardOptions,
        maxLength = maxLength,
        keyboardActions = keyboardActions
    )
}

@Composable
fun OutlinedTextFieldPasswordValidation(
    field: TextField,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    label: @Composable (() -> Unit)? = null,
    maxLength: Int? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextFieldPasswordValidation(
        value = field.value,
        onValueChange = field.onChange,
        error = field.errorMessage,
        modifier = modifier,
        label = label,
        keyboardOptions = keyboardOptions,
        maxLength = maxLength,
        keyboardActions = keyboardActions
    )
}

/**
 * Campo de texto com comportamento padrão para manipulação de senhas.
 *
 * @param value Valor digitado.
 * @param onValueChange Listener executado ao alterar o valor.
 * @param modifier Modificadores do componente.
 * @param label Label do campo.
 * @param error Mensagem de erro de alguma validação.
 * @param keyboardOptions Opções de teclado.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun OutlinedTextFieldPasswordValidation(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    error: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLength: Int? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextFieldValidation(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        error = error,
        label = label,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            lateinit var description: String
            var resource by Delegates.notNull<Int>()

            if (passwordVisible) {
                resource = br.com.velhatech.core.R.drawable.ic_visibility_24dp
                description = stringResource(R.string.description_icon_hide_password)
            } else {
                resource = br.com.velhatech.core.R.drawable.ic_visibility_off_24dp
                description = stringResource(R.string.description_icon_show_password)
            }

            IconButton(
                onClick = { passwordVisible = !passwordVisible }
            ) {
                Icon(painter = painterResource(resource), description, tint = MaterialTheme.colorScheme.onBackground)
            }
        },
        maxLength = maxLength,
        keyboardActions = keyboardActions
    )
}

@Preview
@Composable
fun OutlinedTextFieldValidationPreview() {
    VelhaTechTheme {
        Surface {
            OutlinedTextFieldValidation(
                label = {
                    Text("Label")
                },
                value = "Valor digitado pelo usuário",
                onValueChange = { },
                error = "Mensagem de erro."
            )
        }
    }
}

@Preview
@Composable
fun OutlinedTextFieldValidationWithoutErrorPreview() {
    VelhaTechTheme {
        Surface {
            OutlinedTextFieldValidation(
                label = {
                    Text("Label")
                },
                value = "Valor digitado pelo usuário",
                onValueChange = { }
            )
        }
    }
}