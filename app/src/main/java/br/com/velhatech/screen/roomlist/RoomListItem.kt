package br.com.velhatech.screen.roomlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.velhatech.R
import br.com.velhatech.components.LabeledText
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.firebase.models.RoomDocument
import br.com.velhatech.firebase.enums.EnumDifficultLevel
import br.com.velhatech.firebase.to.TORoom

@Composable
fun RoomListItem(room: TORoom) {
    val context = LocalContext.current

    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val (roomNameRef, roundsRef, playersRef, difficultLevelRef, authIconRef, dividerRef) = createRefs()

        createHorizontalChain(roomNameRef, roundsRef, playersRef, authIconRef)

        LabeledText(
            modifier = Modifier
                .padding(start = 8.dp)
                .constrainAs(roomNameRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.3f
            },
            label = stringResource(R.string.room_list_item_label_room),
            value = room.roomName!!
        )

        LabeledText(
            modifier = Modifier.constrainAs(roundsRef) {
                top.linkTo(parent.top)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.2f
            },
            label = stringResource(R.string.room_list_item_label_rounds),
            value = room.roundsCount.toString()
        )

        LabeledText(
            modifier = Modifier.constrainAs(playersRef) {
                top.linkTo(parent.top)
                horizontalChainWeight = 0.2f

                width = Dimension.fillToConstraints
            },
            label = stringResource(R.string.room_list_item_label_players),
            value = stringResource(
                R.string.room_list_item_players_value,
                room.playersCount!!,
                room.maxPlayers!!
            )
        )

        Icon(
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(authIconRef) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)

                width = Dimension.wrapContent
            },
            painter = getLockIconPainter(room),
            contentDescription = getLockIconDescription(room)
        )

        LabeledText(
            modifier = Modifier
                .padding(start = 8.dp)
                .constrainAs(difficultLevelRef) {
                top.linkTo(roomNameRef.bottom, margin = 8.dp)
                start.linkTo(parent.start)
            },
            label = stringResource(R.string.room_list_item_label_difficult_level),
            value = room.difficultLevel!!.getLabel(context)!!
        )

        HorizontalDivider(
            modifier = Modifier.constrainAs(dividerRef) {
                top.linkTo(difficultLevelRef.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)

                verticalBias = 0f
            }
        )
    }
}

@Composable
private fun getLockIconPainter(room: TORoom): Painter {
    val lockPainter = if (room.password.isNullOrEmpty()) {
        painterResource(br.com.velhatech.core.R.drawable.ic_lock_open_24dp)
    } else {
        painterResource(br.com.velhatech.core.R.drawable.ic_lock_24dp)
    }
    return lockPainter
}

@Composable
private fun getLockIconDescription(room: TORoom): String = if (room.password.isNullOrEmpty()) {
    stringResource(R.string.room_list_item_lock_open_icon_description)
} else {
    stringResource(R.string.room_list_item_lock_icon_description)
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomListItemPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            RoomListItem(
                room = TORoom(
                    roomName = "Sala da Loucura",
                    roundsCount = 5,
                    maxPlayers = 2,
                    playersCount = 1,
                    difficultLevel = EnumDifficultLevel.EASY
                )
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomListItemPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            RoomListItem(
                room = TORoom(
                    roomName = "Sala da Loucura",
                    roundsCount = 5,
                    maxPlayers = 2,
                    playersCount = 1,
                    difficultLevel = EnumDifficultLevel.EASY
                )
            )
        }
    }
}