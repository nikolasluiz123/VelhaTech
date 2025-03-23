package br.com.velhatech.components.buttons.radio

import br.com.velhatech.core.enums.IEnumLabeled

data class RadioButtonOption<T: IEnumLabeled>(val value: T)
