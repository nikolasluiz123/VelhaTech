package br.com.velhatech.screen.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.velhatech.core.theme.VelhaTechTheme
import br.com.velhatech.state.GameBoardState

@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    state: GameBoardState = GameBoardState()
) {
    val linesColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier.border(
            border = BorderStroke(
                width = 3.dp,
                color = linesColor
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        Row {
            repeat(3) { rowIndex ->
                BoardColumn(
                    linesColor = linesColor,
                    drawLine = rowIndex < 2,
                    rowIndex = rowIndex,
                    state = state
                )
            }
        }
    }
}

@Composable
private fun BoardColumn(
    linesColor: Color,
    drawLine: Boolean,
    rowIndex: Int,
    state: GameBoardState
) {
    Column(
        Modifier
            .drawBehind {
                if (drawLine) {
                    val strokeWidth = 3.dp.toPx()

                    drawLine(
                        color = linesColor,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }
    ) {
        repeat(3) { columnIndex ->
            BoardInput(
                linesColor = linesColor,
                drawLine = columnIndex < 2,
                rowIndex = rowIndex,
                columnIndex = columnIndex,
                state = state
            )
        }
    }
}

@Composable
private fun BoardInput(
    linesColor: Color,
    drawLine: Boolean,
    rowIndex: Int,
    columnIndex: Int,
    state: GameBoardState
) {
    Box(
        Modifier
            .size(100.dp)
            .drawBehind {
                if (drawLine) {
                    val strokeWidth = 3.dp.toPx()

                    drawLine(
                        color = linesColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }
            .clickable {
                state.onInputBoardClick(rowIndex, columnIndex)
            }
    ) {
        val figure = try {
            state.boardFigures[rowIndex][columnIndex]
        } catch (_: IndexOutOfBoundsException) {
            null
        }

        figure?.let { resId ->
            Image(
                painter = painterResource(resId),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center),
                colorFilter = getIconColorFilter(resId)
            )
        }
    }
}

@Composable
private fun getIconColorFilter(resId: Int): ColorFilter {
    val color = if (resId == br.com.velhatech.core.R.drawable.ic_x) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

    return ColorFilter.tint(color)
}

@Preview(device = "id:small_phone")
@Composable
private fun GameBoardEmptyPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            GameBoard(state = gameBoardEmptyState)
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun GameBoardFinishedPreviewDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            GameBoard(state = gameBoardFinishedState)
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun GameBoardEmptyPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            GameBoard(state = gameBoardEmptyState)
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun GameBoardFinishedPreviewLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            GameBoard(state = gameBoardFinishedState)
        }
    }
}