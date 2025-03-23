package br.com.velhatech.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.velhatech.core.theme.LabelTextStyle

/**
 * Componente de listagem vertical.
 *
 * @param T Tipo do dado exibido.
 * @param modifier Modificadores específicos.
 * @param items Lista de itens que serão carregados.
 * @param verticalArrangementSpace Espaço entre cada item.
 * @param contentPadding Espaço ao redor da lista.
 * @param itemList Composable que define qual será o card do item. Pode ser usado outro tipo de container além do card.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun <T> LazyVerticalList(
    items: List<T>,
    emptyMessageResId: Int?,
    modifier: Modifier = Modifier,
    verticalArrangementSpace: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
    reverseLayout: Boolean = false,
    itemList: @Composable (T) -> Unit
) {
    if (items.isNotEmpty()) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(verticalArrangementSpace),
            contentPadding = PaddingValues(contentPadding),
            reverseLayout = reverseLayout
        ) {
            items(items = items) { item ->
                itemList(item)
            }
        }
    } else {
        emptyMessageResId?.let { emptyMessage ->
            Box(
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = emptyMessage),
                    style = LabelTextStyle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}