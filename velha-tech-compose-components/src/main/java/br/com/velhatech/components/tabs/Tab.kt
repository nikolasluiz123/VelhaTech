package br.com.velhatech.components.tabs

/**
 * Classe para representar uma Tab no [FitnessProTabRow] e [FitnessProHorizontalPager]
 */
data class Tab(
    val enum: IEnumTab,
    var selected: Boolean,
    val enabled: Boolean
)