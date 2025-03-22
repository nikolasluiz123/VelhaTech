package br.com.velhatech.components.buttons.fab

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
 * FAB que representa a ação de adicionar.
 *
 * @param modifier O modificador para aplicar ao componente.
 * @param onClick A ação a ser realizada quando o botão é clicado.
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun FloatingActionButtonAdd(
    modifier: Modifier = Modifier,
    iconColor: Color = LocalContentColor.current,
    onClick: () -> Unit = { }
) {
    FitnessProFloatingActionButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = br.com.velhatech.core.R.drawable.ic_add_24dp),
            contentDescription = stringResource(R.string.label_add),
            tint = iconColor
        )
    }
}

@Preview
@Composable
fun FloatingActionButtonAddPreview() {
    VelhaTechTheme {
        Surface {
            FloatingActionButtonAdd()
        }
    }
}