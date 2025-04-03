package br.com.velha.tech.firebase.enums

import android.content.Context
import br.com.velha.tech.core.enums.IEnumLabeled
import br.com.velha.tech.firebase.models.R

enum class EnumGameStatus: IEnumLabeled {
    VICTORY {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_game_status_victory)
        }
    },
    LOSE {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_game_status_lose)
        }
    },
    DRAW {
        override fun getLabel(context: Context): String {
            return context.getString(R.string.enum_game_status_draw)
        }
    }
}