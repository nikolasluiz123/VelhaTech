package br.com.velhatech.screen.game

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.velhatech.core.R

@Composable
internal fun FloatingActionButtonPause(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_pause_24dp),
            contentDescription = stringResource(br.com.velhatech.R.string.game_screen_pause_icon_description)
        )
    }
}

@Composable
internal fun FloatingActionButtonPlay(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_play_24dp),
            contentDescription = stringResource(br.com.velhatech.R.string.game_screen_play_icon_description)
        )
    }
}