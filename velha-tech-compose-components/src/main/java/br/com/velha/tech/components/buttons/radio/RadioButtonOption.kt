package br.com.velha.tech.components.buttons.radio

import br.com.velha.tech.core.enums.IEnumLabeled

data class RadioButtonOption<T: IEnumLabeled>(val value: T)
