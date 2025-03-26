package br.com.velha.tech.components.buttons.fab

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * FAB padrão do aplicativo.
 *
 * @param modifier O modificador para aplicar ao componente.
 * @param onClick A ação a ser realizada quando o botão é clicado.
 * @param content O conteúdo do botão, geralmente um ícone.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun FitnessProFloatingActionButton(
    modifier: Modifier = Modifier,
    isBottomBar: Boolean = false,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = if (isBottomBar) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
        contentColor = if (isBottomBar) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary,
    ) {
        content()
    }
}