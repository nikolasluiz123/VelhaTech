package br.com.velhatech.components.fields.state

import br.com.velhatech.components.tabs.Tab

data class TabState(
    val tabs: MutableList<Tab> = mutableListOf(),
    val onSelectTab: (Tab) -> Unit = { },
) {
    val selectedTab: Tab
        get() = tabs.first { it.selected }

    val tabsSize: Int
        get() = tabs.size
}