package br.com.velhatech.components.buttons.radio

import android.content.Context
import br.com.velhatech.components.fields.state.RadioButtonField
import br.com.velhatech.core.enums.IEnumLabeled

internal val radioButtonFieldTwoOptions = RadioButtonField(
    options = EnumRadioTwoOptions.entries.map(::RadioButtonOption)
)

internal val radioButtonFieldFiveOptions = RadioButtonField(
    options = EnumRadioFiveOptions.entries.map(::RadioButtonOption)
)

internal enum class EnumRadioTwoOptions: IEnumLabeled {
    OPTION_1 {
        override fun getLabel(context: Context): String {
            return "Option 1"
        }
    },
    OPTION_2 {
        override fun getLabel(context: Context): String {
            return "Option 2"
        }
    }
}

internal enum class EnumRadioFiveOptions: IEnumLabeled {
    OPTION_1 {
        override fun getLabel(context: Context): String {
            return "Option 1"
        }
    },
    OPTION_2 {
        override fun getLabel(context: Context): String {
            return "Option 2"
        }
    },
    OPTION_3 {
        override fun getLabel(context: Context): String {
            return "Option 3"
        }
    },
    OPTION_4 {
        override fun getLabel(context: Context): String {
            return "Option 4"
        }
    },
    OPTION_5 {
        override fun getLabel(context: Context): String {
            return "Option 5"
        }
    }
}