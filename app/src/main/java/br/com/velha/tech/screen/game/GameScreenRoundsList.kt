package br.com.velha.tech.screen.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.velha.tech.R
import br.com.velha.tech.core.enums.EnumDateTimePatterns
import br.com.velha.tech.core.extensions.format
import br.com.velha.tech.core.theme.LabelTextStyle
import br.com.velha.tech.core.theme.VelhaTechTheme
import br.com.velha.tech.state.GamePlayedRoundsListState
import br.com.velha.tech.state.GameRoundItem

@Composable
fun TopBarExpandableList(
    state: GamePlayedRoundsListState,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        val (actualRoundRef, iconRef, roundsRef, timeRef, dividerRef) = createRefs()

        createHorizontalChain(actualRoundRef, timeRef)

        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .constrainAs(actualRoundRef) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.5f

                },
            text = stringResource(
                R.string.game_screen_rounds_list_label_actual_round,
                state.playedRounds.lastOrNull()?.roundNumber ?: 1,
                state.totalRounds
            ),
            style = LabelTextStyle,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(timeRef) {
                    top.linkTo(parent.top, margin = 8.dp)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints

                    horizontalChainWeight = 0.5f
                },
            text = state.time.format(EnumDateTimePatterns.TIME_WITH_SECONDS),
            style = LabelTextStyle,
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onPrimary
        )

        if (state.expanded) {
            HorizontalDivider(
                modifier = Modifier.constrainAs(dividerRef) {
                    top.linkTo(actualRoundRef.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = MaterialTheme.colorScheme.onPrimary,
                thickness = 0.5.dp
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .constrainAs(roundsRef) {
                    val divider = if (state.expanded) dividerRef else actualRoundRef

                    top.linkTo(divider.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            state.playedRounds.forEach { gameRoundState ->
                AnimatedVisibility(
                    visible = state.expanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    PlayedRoundItem(gameRoundState)
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable { state.onToggleExpand() }
                .constrainAs(iconRef) {
                    top.linkTo(roundsRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.fillToConstraints
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = getPainterIcon(state.expanded),
                contentDescription = getDescriptionIcon(state.expanded),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun PlayedRoundItem(state: GameRoundItem) {
    ConstraintLayout(Modifier.fillMaxWidth()) {
        val (roundRef, winnerRef, dividerRef) = createRefs()

        createHorizontalChain(roundRef, winnerRef)

        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .constrainAs(roundRef) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.4f
            },
            text = stringResource(R.string.game_screen_rounds_list_label_round, state.roundNumber),
            style = LabelTextStyle,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(winnerRef) {
                top.linkTo(parent.top, margin = 8.dp)
                end.linkTo(parent.end)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.6f
            },
            text = getWinnerText(state),
            style = LabelTextStyle,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.End
        )

        HorizontalDivider(
            modifier = Modifier.constrainAs(dividerRef) {
                top.linkTo(winnerRef.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            color = MaterialTheme.colorScheme.onPrimary,
            thickness = 0.5.dp
        )
    }
}

@Composable
private fun getWinnerText(state: GameRoundItem): String {
    return state.winnerName?.let {
        stringResource(R.string.game_screen_rounds_list_label_winner, it)
    } ?: stringResource(R.string.game_screen_rounds_list_label_draw)
}

@Composable
private fun getDescriptionIcon(expanded: Boolean): String = if (expanded) {
    stringResource(R.string.game_screen_rounds_list_label_collapse)
} else {
    stringResource(R.string.game_screen_rounds_list_label_expand)
}

@Composable
private fun getPainterIcon(expanded: Boolean): Painter = if (expanded) {
    painterResource(br.com.velha.tech.core.R.drawable.ic_arrow_up_24dp)
} else {
    painterResource(br.com.velha.tech.core.R.drawable.ic_arrow_down_24dp)
}

@Preview(device = "id:small_phone")
@Composable
private fun TopBarExpandableListClosedStartedGamePreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            TopBarExpandableList(
                state = gamePlayedRoundsListStateThreeRoundsClosed
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun TopBarExpandableClosedStartedGameListPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            TopBarExpandableList(
                state = gamePlayedRoundsListStateThreeRoundsClosed
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun TopBarExpandableListExpandedStartedGamePreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            TopBarExpandableList(
                state = gamePlayedRoundsListStateThreeRoundsOpened
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun TopBarExpandableExpandedStartedGameListPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            TopBarExpandableList(
                state = gamePlayedRoundsListStateThreeRoundsOpened
            )
        }
    }
}