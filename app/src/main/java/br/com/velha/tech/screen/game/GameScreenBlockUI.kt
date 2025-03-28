package br.com.velha.tech.screen.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.velha.tech.core.theme.MessageBlockUI
import br.com.velha.tech.core.theme.TitleMessageBlockUI
import br.com.velha.tech.core.theme.VelhaTechTheme
import br.com.velha.tech.state.BlockUIMessageState

@Composable
fun BlockUI(state: BlockUIMessageState, modifier: Modifier = Modifier) {
    if (state.visible) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center,
        ) {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    text = state.title,
                    style = TitleMessageBlockUI,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.message,
                    style = MessageBlockUI,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun BlockUIPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            BlockUI(
                state = BlockUIMessageState(
                    title = "O Jogador Josnei entrou!",
                    message = "O jogo iniciará automaticamente em 10 segundos...",
                    visible = true
                )
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun BlockUIPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            BlockUI(
                state = BlockUIMessageState(
                    title = "O Jogador Josnei entrou!",
                    message = "O jogo iniciará automaticamente em 10 segundos...",
                    visible = true
                ),
            )
        }
    }
}