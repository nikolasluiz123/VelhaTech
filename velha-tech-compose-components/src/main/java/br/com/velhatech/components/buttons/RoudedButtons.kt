package br.com.velhatech.components.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.velhatech.compose.components.R
import br.com.velhatech.core.theme.VelhaTechTheme

/**
 * Botão arredondado com o ícone da Google
 *
 * @param modifier o modificador do botão
 * @param onClick o evento de clique do botão
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun RoundedGoogleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    IconButton(
        modifier = modifier.size(48.dp),
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Icon(
            painter = painterResource(id = br.com.velhatech.core.R.drawable.ic_google),
            contentDescription = stringResource(R.string.rounded_button_google_content_description),
        )
    }
}

@Preview
@Composable
private fun RoundedGoogleButtonPreview() {
    VelhaTechTheme {
        Surface {
            RoundedGoogleButton()
        }
    }
}