package br.com.velha.tech.screen.roomcreation.enums

import android.content.Context
import br.com.velha.tech.core.enums.IEnumLabeled
import br.com.velha.tech.firebase.models.R

enum class EnumRoundType: IEnumLabeled {
    MD_3 {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_round_type_md3)
        }
    },
    MD_5 {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_round_type_md5)
        }
    }
}