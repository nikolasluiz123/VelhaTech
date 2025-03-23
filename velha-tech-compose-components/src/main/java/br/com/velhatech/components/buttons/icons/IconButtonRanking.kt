package br.com.velhatech.components.buttons.icons

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.velhatech.compose.components.R
import br.com.velhatech.core.theme.VelhaTechTheme

@Composable
fun IconButtonRanking(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescriptionResId: Int? = R.string.label_ranking,
    onClick: () -> Unit = { }
) {
    VelhaTechIconButton(
        modifier = modifier,
        resId = br.com.velhatech.core.R.drawable.ic_ranking_24dp,
        enabled = enabled,
        contentDescriptionResId = contentDescriptionResId,
        onClick = onClick
    )
}


@Preview(device = "id:small_phone")
@Composable
fun IconButtonRankingPreview() {
    VelhaTechTheme {
        Surface {
            IconButtonRanking()
        }
    }
}