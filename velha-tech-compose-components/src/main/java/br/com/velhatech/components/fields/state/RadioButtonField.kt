package br.com.velhatech.components.fields.state

import br.com.velhatech.components.buttons.radio.RadioButtonOption
import br.com.velhatech.core.enums.IEnumLabeled

data class RadioButtonField<T: IEnumLabeled>(
    val options: List<RadioButtonOption<T>> = emptyList(),
    var selectedOption: RadioButtonOption<T>? = null,
    val onOptionSelected: (RadioButtonOption<T>) -> Unit = {}
)
