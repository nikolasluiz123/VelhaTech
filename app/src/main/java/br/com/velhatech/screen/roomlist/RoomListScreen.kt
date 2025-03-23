package br.com.velhatech.screen.roomlist

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
import br.com.velhatech.components.buttons.icons.IconButtonRanking
import br.com.velhatech.components.filter.SimpleFilter
import br.com.velhatech.components.list.LazyVerticalList
import br.com.velhatech.components.topbar.SimpleVelhaTechTopAppBar
import br.com.velhatech.core.extensions.calculatePaddingTopWithoutMargin
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.state.RoomListUIState
import br.com.velhatech.viewmodel.RoomListViewModel

@Composable
fun RoomListScreen(viewModel: RoomListViewModel) {
    val state by viewModel.uiState.collectAsState()

    RoomListScreen(
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(
    state: RoomListUIState = RoomListUIState(),
) {
    Scaffold(
        topBar = {
            SimpleVelhaTechTopAppBar(
                title = stringResource(R.string.room_list_screen_title),
                subtitle = state.subtitle,
                showMenuWithLogout = false,
                customNavigationIcon = {
                    IconButtonAccount(
                        onClick = {

                        }
                    )
                }
            )
        },
        bottomBar = {
            VelhaTechBottomAppBar(
                actions = {
                    IconButtonRanking(
                        onClick = {

                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButtonAdd(
                        isBottomBar = true,
                        onClick = {

                        }
                    )
                }
            )
        }
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
                RoomLazyVerticalList(state = state)
            }

            RoomLazyVerticalList(
                modifier = Modifier.constrainAs(listRef) {
                    top.linkTo(filterRef.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                    height = Dimension.fillToConstraints
                },
                state = state
            )
        }
    }
}

@Composable
private fun RoomLazyVerticalList(
    state: RoomListUIState,
    modifier: Modifier = Modifier
) {
    LazyVerticalList(
        modifier = modifier,
        items = state.rooms,
        emptyMessageResId = R.string.room_list_screen_empty_message,
    ) { toRoom ->
        RoomListItem(
            room = toRoom
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