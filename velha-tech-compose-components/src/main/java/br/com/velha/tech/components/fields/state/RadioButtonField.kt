package br.com.velha.tech.components.fields.state

import br.com.velha.tech.components.buttons.radio.RadioButtonOption
import br.com.velha.tech.core.enums.IEnumLabeled

data class RadioButtonField<T: IEnumLabeled>(
    val options: List<RadioButtonOption<T>> = emptyList(),
    var selectedOption: RadioButtonOption<T>? = null,
    val onOptionSelected: (RadioButtonOption<T>) -> Unit = {}
)
