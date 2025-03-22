package br.com.velhatech.components.buttons.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun VelhaTechIconButton(
    resId: Int,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescriptionResId: Int? = null,
    onClick: () -> Unit = { }
) {
    IconButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
    ) {
        Icon(
            modifier = iconModifier,
            painter = painterResource(id = resId),
            contentDescription = contentDescriptionResId?.let { stringResource(it) }
        )
    }
}