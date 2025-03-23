package br.com.velhatech.core.enums

import android.content.Context

interface IEnumLabeled {
    fun  getLabel(context: Context): String? = null

    fun getPluralLabel(context: Context, quantity: Int): String? = null
}