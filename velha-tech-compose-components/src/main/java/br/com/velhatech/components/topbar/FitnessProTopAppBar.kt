package br.com.velhatech.components.topbar

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
import br.com.velhatech.components.buttons.icons.IconButtonArrowBack
import br.com.velhatech.components.buttons.icons.MenuIconButton
import br.com.velhatech.components.buttons.icons.MenuIconButtonWithDefaultActions
import br.com.velhatech.core.theme.VelhaTechTheme

/**
 * TopAppBar padrão do APP.
 *
 * @param title Título da app bar
 * @param onBackClick Ação ao clicar no ícone da esquerda.
 * @param onLogoutClick Ação ao clicar no item de menu Logout.
 * @param actions Ações exibidas a direita da barra.
 * @param menuItems Itens de menu exibidos dentro do MoreOptions.
 * @param colors Cores da barra.
 * @param showNavigationIcon Flag para exibir ícone de navação ou não.
 * @param showMenuWithLogout Flag para exibir o menu com a opção de Logout.
 *
 * @author Nikolas Luiz Schmitt
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessProTopAppBar(
    title: @Composable () -> Unit,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit = { },
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
    showMenuWithLogout: Boolean = true,
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

            if (showMenuWithLogout) {
                MenuIconButtonWithDefaultActions(
                    onLogoutClick = onLogoutClick,
                    menuItems = menuItems,
                )
            } else if (showMenu) {
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
            FitnessProTopAppBar(
                title = { Text("Título da Tela") },
                onBackClick = { },
                onLogoutClick = { }
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
                showMenuWithLogout = false
            )
        }
    }
}