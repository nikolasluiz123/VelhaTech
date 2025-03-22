package br.com.velhatech.core.state

interface ILoadingUIState {
    val showLoading: Boolean
    val onToggleLoading: () -> Unit
}