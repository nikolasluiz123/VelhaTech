package br.com.velhatech.components.loading

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Indicador de progresso em linha.
 *
 * @param show Flag que indica se deve ou não ser exibido o componente.
 * @param modifier Modificadores específicos.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun VelhaTechLinearProgressIndicator(show: Boolean, modifier: Modifier = Modifier) {
    if (show) {
        LinearProgressIndicator(modifier.fillMaxWidth())
    }
}