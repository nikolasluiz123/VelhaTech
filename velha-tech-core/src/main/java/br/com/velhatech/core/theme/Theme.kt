package br.com.velhatech.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = COLOR_PRIMARY_DARK,
    onPrimary = COLOR_ON_PRIMARY_DARK,
    primaryContainer = COLOR_PRIMARY_CONTAINER_DARK,
    onPrimaryContainer = COLOR_ON_PRIMARY_CONTAINER_DARK,
    inversePrimary = COLOR_INVERSE_PRIMARY_DARK,
    secondary = COLOR_SECONDARY_DARK,
    onSecondary = COLOR_ON_SECONDARY_DARK,
    secondaryContainer = COLOR_SECONDARY_CONTAINER_DARK,
    onSecondaryContainer = COLOR_ON_SECONDARY_CONTAINER_DARK,
    tertiary = COLOR_TERTIARY_DARK,
    onTertiary = COLOR_ON_TERTIARY_DARK,
    tertiaryContainer = COLOR_TERTIARY_CONTAINER_DARK,
    onTertiaryContainer = COLOR_ON_TERTIARY_CONTAINER_DARK,
    background = COLOR_BACKGROUND_DARK,
    onBackground = COLOR_ON_BACKGROUND_DARK,
    surface = COLOR_SURFACE_DARK,
    onSurface = COLOR_ON_SURFACE_DARK,
    surfaceVariant = COLOR_SURFACE_VARIANT_DARK,
    onSurfaceVariant = COLOR_ON_SURFACE_VARIANT_DARK,
    surfaceTint = COLOR_SURFACE_TINT_DARK,
    inverseSurface = COLOR_INVERSE_SURFACE_DARK,
    inverseOnSurface = COLOR_INVERSE_ON_SURFACE_DARK,
    error = COLOR_ERROR_DARK,
    onError = COLOR_ON_ERROR_DARK,
    errorContainer = COLOR_ERROR_CONTAINER_DARK,
    onErrorContainer = COLOR_ON_ERROR_CONTAINER_DARK,
    outline = COLOR_OUTLINE_DARK,
    outlineVariant = COLOR_OUTLINE_VARIANT_DARK,
    scrim = COLOR_SCRIM_DARK,
    surfaceBright = COLOR_SURFACE_BRIGHT_DARK,
    surfaceContainer = COLOR_SURFACE_CONTAINER_DARK,
    surfaceContainerHigh = COLOR_SURFACE_CONTAINER_HIGH_DARK,
    surfaceContainerHighest = COLOR_SURFACE_CONTAINER_HIGHEST_DARK,
    surfaceContainerLow = COLOR_SURFACE_CONTAINER_LOW_DARK,
    surfaceContainerLowest = COLOR_SURFACE_CONTAINER_LOWEST_DARK,
    surfaceDim = COLOR_SURFACE_DIM_DARK,
)

private val LightColorScheme = lightColorScheme(
    primary = COLOR_PRIMARY_LIGHT,
    onPrimary = COLOR_ON_PRIMARY_LIGHT,
    primaryContainer = COLOR_PRIMARY_CONTAINER_LIGHT,
    onPrimaryContainer = COLOR_ON_PRIMARY_CONTAINER_LIGHT,
    inversePrimary = COLOR_INVERSE_PRIMARY_LIGHT,
    secondary = COLOR_SECONDARY_LIGHT,
    onSecondary = COLOR_ON_SECONDARY_LIGHT,
    secondaryContainer = COLOR_SECONDARY_CONTAINER_LIGHT,
    onSecondaryContainer = COLOR_ON_SECONDARY_CONTAINER_LIGHT,
    tertiary = COLOR_TERTIARY_LIGHT,
    onTertiary = COLOR_ON_TERTIARY_LIGHT,
    tertiaryContainer = COLOR_TERTIARY_CONTAINER_LIGHT,
    onTertiaryContainer = COLOR_ON_TERTIARY_CONTAINER_LIGHT,
    background = COLOR_BACKGROUND_LIGHT,
    onBackground = COLOR_ON_BACKGROUND_LIGHT,
    surface = COLOR_SURFACE_LIGHT,
    onSurface = COLOR_ON_SURFACE_LIGHT,
    surfaceVariant = COLOR_SURFACE_VARIANT_LIGHT,
    onSurfaceVariant = COLOR_ON_SURFACE_VARIANT_LIGHT,
    surfaceTint = COLOR_SURFACE_TINT_LIGHT,
    inverseSurface = COLOR_INVERSE_SURFACE_LIGHT,
    inverseOnSurface = COLOR_INVERSE_ON_SURFACE_LIGHT,
    error = COLOR_ERROR_LIGHT,
    onError = COLOR_ON_ERROR_LIGHT,
    errorContainer = COLOR_ERROR_CONTAINER_LIGHT,
    onErrorContainer = COLOR_ON_ERROR_CONTAINER_LIGHT,
    outline = COLOR_OUTLINE_LIGHT,
    outlineVariant = COLOR_OUTLINE_VARIANT_LIGHT,
    scrim = COLOR_SCRIM_LIGHT,
    surfaceBright = COLOR_SURFACE_BRIGHT_LIGHT,
    surfaceContainer = COLOR_SURFACE_CONTAINER_LIGHT,
    surfaceContainerHigh = COLOR_SURFACE_CONTAINER_HIGH_LIGHT,
    surfaceContainerHighest = COLOR_SURFACE_CONTAINER_HIGHEST_LIGHT,
    surfaceContainerLow = COLOR_SURFACE_CONTAINER_LOW_LIGHT,
    surfaceContainerLowest = COLOR_SURFACE_CONTAINER_LOWEST_LIGHT,
    surfaceDim = COLOR_SURFACE_DIM_LIGHT,
)

@Composable
fun VelhaTechTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val windowInsetsController = WindowCompat.getInsetsController(window, view)

            windowInsetsController.isAppearanceLightStatusBars = !darkTheme
            windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}