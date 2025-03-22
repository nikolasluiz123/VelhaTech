package br.com.velhatech.components.buttons.fab

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.velhatech.compose.components.R
import br.com.velhatech.core.theme.VelhaTechTheme

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
    iconColor: Color = Color.White
) {
    FitnessProFloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = br.com.velhatech.core.R.drawable.ic_check_24dp),
            contentDescription = stringResource(R.string.label_save),
            tint = iconColor
        )
    }
}



@Preview
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
