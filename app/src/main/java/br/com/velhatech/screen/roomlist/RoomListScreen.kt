package br.com.velhatech.screen.roomlist

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.velhatech.R
import br.com.velhatech.components.bottombar.VelhaTechBottomAppBar
import br.com.velhatech.components.buttons.fab.FloatingActionButtonAdd
import br.com.velhatech.components.buttons.icons.IconButtonAccount
import br.com.velhatech.components.buttons.icons.IconButtonLogout
import br.com.velhatech.components.buttons.icons.IconButtonRanking
import br.com.velhatech.components.filter.SimpleFilter
import br.com.velhatech.components.list.LazyVerticalList
import br.com.velhatech.components.topbar.SimpleVelhaTechTopAppBar
import br.com.velhatech.core.extensions.calculatePaddingTopWithoutMargin
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.firebase.apis.analytics.logButtonClick
import br.com.velhatech.screen.game.callback.OnNavigateToGame
import br.com.velhatech.screen.roomlist.enums.EnumRoomListTags
import br.com.velhatech.state.RoomListUIState
import br.com.velhatech.viewmodel.RoomListViewModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

@Composable
fun RoomListScreen(
    viewModel: RoomListViewModel,
    onNavigateToRoomCreation: () -> Unit,
    onNavigateToGame: OnNavigateToGame,
    onLogoutClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    RoomListScreen(
        state = state,
        onNavigateToRoomCreation = onNavigateToRoomCreation,
        onNavigateToGame = onNavigateToGame,
        onLogoutClick = {
            viewModel.logout()
            onLogoutClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    state: RoomListUIState = RoomListUIState(),
    onNavigateToRoomCreation: () -> Unit = { },
    onNavigateToGame: OnNavigateToGame? = null,
    onLogoutClick: () -> Unit = { }
) {
    Scaffold(
        topBar = {
            SimpleVelhaTechTopAppBar(
                title = stringResource(R.string.room_list_screen_title),
                subtitle = state.subtitle,
                customNavigationIcon = {
                    IconButtonAccount(
                        onClick = {
                            Firebase.analytics.logButtonClick(EnumRoomListTags.ROOM_LIST_SCREEN_ACCOUNT_BUTTON)

                            // TODO - Perfil
                        }
                    )
                },
                actions = {
                    IconButtonLogout(onClick = onLogoutClick)
                }
            )
        },
        bottomBar = {
            VelhaTechBottomAppBar(
                actions = {
                    IconButtonRanking(
                        onClick = {
                            Firebase.analytics.logButtonClick(EnumRoomListTags.ROOM_LIST_SCREEN_RANKING_BUTTON)

                            // TODO - Rankin
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(
                        isBottomBar = true,
                        onClick = {
                            Firebase.analytics.logButtonClick(EnumRoomListTags.ROOM_LIST_SCREEN_ADD_ROOM_BUTTON)

                            onNavigateToRoomCreation()
                        }
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) { padding ->
        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculatePaddingTopWithoutMargin(),
                    bottom = padding.calculateBottomPadding(),
                )
        ) {
            val (filterRef, listRef) = createRefs()

            SimpleFilter(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(filterRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                placeholderResId = R.string.room_list_screen_filter_placeholder,
                state = state.simpleFilterState
            ) {
                RoomLazyVerticalList(
                    state = state,
                    onNavigateToGame = onNavigateToGame
                )
            }

            RoomLazyVerticalList(
                modifier = Modifier.constrainAs(listRef) {
                    top.linkTo(filterRef.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                },
                state = state,
                onNavigateToGame = onNavigateToGame
            )
        }
    }
}

@Composable
private fun RoomLazyVerticalList(
    state: RoomListUIState,
    modifier: Modifier = Modifier,
    onNavigateToGame: OnNavigateToGame?
) {
    LazyVerticalList(
        modifier = modifier,
        items = state.filteredRooms,
        emptyMessageResId = R.string.room_list_screen_empty_message,
    ) { toRoom ->
        RoomListItem(
            room = toRoom,
            onNavigateToGame = onNavigateToGame
        )
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomListScreenEmptyPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            RoomListScreen(state = emptyUIState)
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomListScreenPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            RoomListScreen(state = populatedUIState)
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomListScreenEmptyPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            RoomListScreen(state = emptyUIState)
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RoomListScreenPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            RoomListScreen(state = populatedUIState)
        }
    }
}