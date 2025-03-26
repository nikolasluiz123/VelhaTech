package br.com.velha.tech.components.buttons.icons

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.velha.tech.compose.components.R
import br.com.velha.tech.core.theme.VelhaTechTheme

@Composable
fun IconButtonAccount(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescriptionResId: Int? = R.string.label_account,
    onClick: () -> Unit = { }
) {
    VelhaTechIconButton(
        modifier = modifier,
        resId = br.com.velha.tech.core.R.drawable.ic_account_circle_24dp,
        enabled = enabled,
        contentDescriptionResId = contentDescriptionResId,
        onClick = onClick
    )
}


@Preview(device = "id:small_phone")
@Composable
fun IconButtonAccountPreview() {
    VelhaTechTheme {
        Surface {
            IconButtonAccount()
        }
    }
}