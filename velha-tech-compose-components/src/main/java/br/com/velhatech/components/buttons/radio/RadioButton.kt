package br.com.velhatech.components.buttons.radio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import br.com.velhatech.components.fields.state.RadioButtonField
import br.com.velhatech.core.enums.IEnumLabeled
import br.com.velhatech.core.theme.LabelTextStyle
import br.com.velhatech.core.theme.ValueTextStyle
import br.com.velhatech.core.theme.VelhaTechTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T: IEnumLabeled> RadioButtonSession(
    sessionLabel: String,
    field: RadioButtonField<T>,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Text(text = sessionLabel, style = LabelTextStyle)

        FlowRow {
            field.options.forEach { radioOption ->
                LabeledRadioButton(
                    label = radioOption.value.getLabel(LocalContext.current)!!,
                    selected = radioOption.checked,
                    onClick = {
                        checkOption(field, radioOption)
                        field.onOptionSelected(radioOption)
                    }
                )
            }
        }
    }
}

private fun <T: IEnumLabeled> checkOption(
    field: RadioButtonField<T>,
    radioOption: RadioButtonOption<T>
) {
    field.options.forEach {
        it.checked = it.value == radioOption.value
    }
}

@Composable
fun LabeledRadioButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, style = ValueTextStyle)
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RadioButtonSessionTwoOptionsLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            RadioButtonSession(
                sessionLabel = "Session Label",
                field = radioButtonFieldTwoOptions
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RadioButtonSessionTwoOptionsDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            RadioButtonSession(
                sessionLabel = "Session Label",
                field = radioButtonFieldTwoOptions
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RadioButtonSessionFiveOptionsLight() {
    VelhaTechTheme(darkTheme = false) {
        Surface {
            RadioButtonSession(
                sessionLabel = "Session Label",
                field = radioButtonFieldFiveOptions
            )
        }
    }
}

@Preview(device = "id:small_phone")
@Composable
private fun RadioButtonSessionFiveOptionsDark() {
    VelhaTechTheme(darkTheme = true) {
        Surface {
            RadioButtonSession(
                sessionLabel = "Session Label",
                field = radioButtonFieldFiveOptions
            )
        }
    }
}