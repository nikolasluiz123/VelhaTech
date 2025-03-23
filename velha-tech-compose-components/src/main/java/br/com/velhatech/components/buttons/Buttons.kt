package br.com.velhatech.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.velhatech.core.theme.ButtonTextStyle
import br.com.velhatech.core.theme.TextButtonTextStyle
import br.com.velhatech.core.theme.VelhaTechTheme

/**
 * Botão padrão com as definições de estilo do app.
 *
 * @param label Texto do botão.
 * @param onClickListener Ação a ser executada quando o botão for clicado.
 * @param modifier Modificador do botão.
 * @param enabled Indica se o botão está habilitado.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun VelhaTechButton(
    label: String,
    onClickListener: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        onClick = onClickListener,
        enabled = enabled,
    ) {
        Text(
            text = label,
            style = ButtonTextStyle,
        )
    }
}

/**
 * Botão secundário com as definições de estilo do app.
 *
 * @param label Texto do botão.
 * @param onClickListener Ação a ser executada quando o botão for clicado.
 * @param modifier Modificador do botão.
 * @param enabled Indica se o botão está habilitado.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun VelhaTechOutlinedButton(
    label: String,
    onClickListener: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClickListener,
        enabled = enabled,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = label,
            style = ButtonTextStyle
        )
    }
}

/**
 * Botão somente com o texto, usado normalmente em dialogs.
 */
@Composable
fun VelhaTechTextButton(
    label: String,
    onClickListener: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        modifier = modifier,
        onClick = onClickListener,
        enabled = enabled,
    ) {
        Text(text = label, style = TextButtonTextStyle)
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun FitnessProButtonPreview() {
    VelhaTechTheme {
        Surface {
            VelhaTechButton(
                label = "Button",
                onClickListener = {}
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun FitnessProOutlinedButtonPreview() {
    VelhaTechTheme {
        Surface {
            VelhaTechOutlinedButton(
                label = "Button",
                onClickListener = {}
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun FitnessProTextButtonPreview() {
    VelhaTechTheme {
        Surface {
            VelhaTechTextButton(
                label = "Button",
                onClickListener = {}
            )
        }
    }
}