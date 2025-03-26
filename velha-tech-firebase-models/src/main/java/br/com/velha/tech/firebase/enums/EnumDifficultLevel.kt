package br.com.velha.tech.firebase.enums

import android.content.Context
import br.com.velha.tech.core.enums.IEnumLabeled
import br.com.velha.tech.firebase.models.R

enum class EnumDifficultLevel: IEnumLabeled {
    EASY {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_difficult_level_easy)
        }
    },
    MEDIUM {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_difficult_level_medium)
        }
    },
    HARD {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_difficult_level_hard)
        }
    }
}