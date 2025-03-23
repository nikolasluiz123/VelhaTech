package br.com.velhatech.components.filter

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.velhatech.components.filter.EnumSimpleFilterTags.SIMPLE_FILTER_SEARCH_BAR
import br.com.velhatech.components.filter.EnumSimpleFilterTags.SIMPLE_FILTER_SEARCH_BAR_INPUT_FIELD
import br.com.velhatech.components.filter.EnumSimpleFilterTags.SIMPLE_FILTER_SEARCH_BAR_INPUT_FIELD_PLACEHOLDER
import br.com.velhatech.compose.components.R
import br.com.velhatech.core.theme.ValueTextStyle
import br.com.velhatech.core.theme.VelhaTechTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleFilter(
    placeholderResId: Int,
    modifier: Modifier = Modifier,
    state: SimpleFilterState = SimpleFilterState(),
    content: @Composable () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    SearchBar(
        modifier = modifier,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary,
            dividerColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = SearchBarDefaults.fullScreenShape,
        inputField = {
            SearchBarDefaults.InputField(
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = br.com.velhatech.core.R.drawable.ic_search_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(placeholderResId),
                        style = ValueTextStyle,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                query = text,
                onQueryChange = {
                    text = it
                    state.onSimpleFilterChange(text)
                },
                onSearch = {
                    state.onSimpleFilterChange(text)
                },
                expanded = state.expanded,
                onExpandedChange = state.onExpandedChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.onPrimary,
                    selectionColors = TextSelectionColors(
                        handleColor = MaterialTheme.colorScheme.onPrimary,
                        backgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )
                )
            )
        },
        windowInsets = WindowInsets(0.dp),
        expanded = state.expanded,
        onExpandedChange = state.onExpandedChange,
        content = {
            content()
        }
    )
}

@Preview(device = "id:small_phone")
@Composable
private fun SimpleFilterPreview() {
    VelhaTechTheme {
        Surface {
            SimpleFilter(
                placeholderResId = R.string.label_placeholder_example,
            ) {
            }
        }
    }
}