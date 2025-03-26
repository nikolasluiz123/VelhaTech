package br.com.velha.tech.core.state

interface ILoadingUIState {
    val showLoading: Boolean
    val onToggleLoading: () -> Unit
}