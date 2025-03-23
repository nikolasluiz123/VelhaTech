package br.com.velhatech.components.topbar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import br.com.velhatech.core.theme.TopAppBarSubtitleTextStyle
import br.com.velhatech.core.theme.TopAppBarTitleTextStyle

/**
 * Top App Bar com uma passagem facilitada de título e
 * subtítulo.
 *
 * @see VelhaTechTopAppBar
 *
 * @param title String com o título da barra
 * @param subtitle String com o subtítulo da barra
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
fun SimpleVelhaTechTopAppBar(
    title: String,
    subtitle: String? = null,
    onLogoutClick: () -> Unit = { },
    onBackClick: () -> Unit = { },
    actions: @Composable () -> Unit = { },
    menuItems: @Composable () -> Unit = { },
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
    ),
    showNavigationIcon: Boolean = true,
    customNavigationIcon: (@Composable () -> Unit)? = null,
    showMenu: Boolean = false
) {
    VelhaTechTopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = TopAppBarTitleTextStyle,
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = TopAppBarSubtitleTextStyle,
                    )
                }
            }
        },
        colors = colors,
        actions = actions,
        menuItems = menuItems,
        showNavigationIcon = showNavigationIcon,
        customNavigationIcon = customNavigationIcon,
        onBackClick = onBackClick,
        onLogoutClick = onLogoutClick,
        showMenu = showMenu
    )
}