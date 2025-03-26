package br.com.velha.tech.components.filter

data class SimpleFilterState(
    val onSimpleFilterChange: (String) -> Unit = { },
    val onExpandedChange: (Boolean) -> Unit = { },
    val expanded: Boolean = false,
    var filterValue: String = ""
)
