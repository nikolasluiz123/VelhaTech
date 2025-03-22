package br.com.velhatech.components.bottombar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VelhaTechBottomAppBar(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = { },
    floatingActionButton: @Composable () -> Unit = { }
) {
    BottomAppBar(
        modifier = modifier,
        actions = actions,
        floatingActionButton = floatingActionButton,
        windowInsets = WindowInsets(0.dp)
    )
}