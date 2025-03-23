package br.com.velhatech.components.filter

data class SimpleFilterState(
    val onSimpleFilterChange: (String) -> Unit = { },
    val onExpandedChange: (Boolean) -> Unit = { },
    val expanded: Boolean = false,
)
