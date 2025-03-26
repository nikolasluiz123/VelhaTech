package br.com.velha.tech.components.topbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.velha.tech.components.buttons.icons.IconButtonArrowBack
import br.com.velha.tech.components.buttons.icons.MenuIconButton
import br.com.velha.tech.core.theme.VelhaTechTheme

/**
 * TopAppBar padrão do APP.
 *
 * @param title Título da app bar
 * @param onBackClick Ação ao clicar no ícone da esquerda.
 * @param actions Ações exibidas a direita da barra.
 * @param menuItems Itens de menu exibidos dentro do MoreOptions.
 * @param colors Cores da barra.
 * @param showNavigationIcon Flag para exibir ícone de navação ou não.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VelhaTechTopAppBar(
    title: @Composable () -> Unit,
    onBackClick: () -> Unit,
    actions: @Composable () -> Unit = { },
    menuItems: @Composable () -> Unit = { },
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        titleContentColor = MaterialTheme.colorScheme.onSecondary,
        actionIconContentColor = MaterialTheme.colorScheme.onSecondary,
        navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
    ),
    showNavigationIcon: Boolean = true,
    customNavigationIcon: (@Composable () -> Unit)? = null,
    showMenu: Boolean = false,
    windowInsets: WindowInsets = WindowInsets(0.dp),
) {
    TopAppBar(
        title = title,
        colors = colors,
        windowInsets = windowInsets,
        navigationIcon = {
            if (showNavigationIcon) {
                if (customNavigationIcon != null) {
                    customNavigationIcon()
                } else {
                    IconButtonArrowBack(
                        onClick = onBackClick,
                    )
                }
            }
        },
        actions = {
            actions()

            if (showMenu) {
                MenuIconButton(menuItems)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = "id:small_phone")
@Composable
fun FitnessProTopAppBarPreview() {
    VelhaTechTheme {
        Surface {
            VelhaTechTopAppBar(
                title = { Text("Título da Tela") },
                onBackClick = { },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = "id:small_phone")
@Composable
fun FitnessProTopAppBarWithSubtitlePreview() {
    VelhaTechTheme {
        Surface {
            SimpleVelhaTechTopAppBar(
                title = "Título da Tela",
                subtitle = "Subtitulo da Tela",
            )
        }
    }
}