package br.com.velhatech.screen.roomcreation.enums

import android.content.Context
import br.com.velhatech.core.enums.IEnumLabeled
import br.com.velhatech.firebase.models.R

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