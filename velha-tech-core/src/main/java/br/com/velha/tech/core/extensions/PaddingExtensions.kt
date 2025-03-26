package br.com.velha.tech.core.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

fun PaddingValues.calculatePaddingTopWithoutMargin() = calculateTopPadding() - 8.dp