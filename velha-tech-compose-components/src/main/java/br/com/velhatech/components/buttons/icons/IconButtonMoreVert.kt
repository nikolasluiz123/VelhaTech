package br.com.velhatech.components.buttons.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.velhatech.core.theme.VelhaTechTheme


/**
 * Botão com ícone de mais opções.
 *
 * @param onClick Ação ao clicar.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun IconButtonMoreVert(onClick: () -> Unit = { }) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
    }
}



@Preview
@Composable
fun IconButtonMoreVertPreview() {
    VelhaTechTheme {
        Surface {
            IconButtonMoreVert()
        }
    }
}