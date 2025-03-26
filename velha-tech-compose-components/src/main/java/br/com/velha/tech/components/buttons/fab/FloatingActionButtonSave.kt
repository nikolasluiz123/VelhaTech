package br.com.velha.tech.components.buttons.fab

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.velha.tech.compose.components.R
import br.com.velha.tech.core.theme.VelhaTechTheme

/**
 * FAB que representa a ação de salvar.
 *
 * @param modifier O modificador para aplicar ao componente.
 * @param onClick A ação a ser realizada quando o botão é clicado.
 * @uthor Nikolas Luiz Schmitt
 */
@Composable
fun FloatingActionButtonSave(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBottomBar: Boolean = false,
) {
    FitnessProFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        isBottomBar = isBottomBar
    ) {
        Icon(
            painter = painterResource(id = br.com.velha.tech.core.R.drawable.ic_check_24dp),
            contentDescription = stringResource(R.string.label_save),
            tint = if (isBottomBar) MaterialTheme.colorScheme.onPrimaryContainer else LocalContentColor.current
        )
    }
}



@Preview(device = "id:small_phone")
@Composable
fun FloatingActionButtonSavePreview() {
    VelhaTechTheme {
        Surface {
            FloatingActionButtonSave(
                onClick = { }
            )
        }
    }
}
