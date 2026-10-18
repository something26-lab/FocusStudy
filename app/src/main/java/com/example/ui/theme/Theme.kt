package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    LIQUID_GLASS
}

val LiquidGlassColorScheme = darkColorScheme(
    primary = GlassPrimary,
    onPrimary = GlassOnPrimary,
    primaryContainer = GlassPrimaryContainer,
    onPrimaryContainer = GlassOnPrimaryContainer,
    secondary = GlassSecondary,
    onSecondary = GlassOnSecondary,
    secondaryContainer = GlassSecondaryContainer,
    onSecondaryContainer = GlassOnSecondaryContainer,
    tertiary = GlassTertiary,
    onTertiary = GlassOnTertiary,
    tertiaryContainer = GlassTertiaryContainer,
    onTertiaryContainer = GlassOnTertiaryContainer,
    background = GlassBackground,
    onBackground = GlassOnBackground,
    surface = GlassSurface,
    onSurface = GlassOnSurface,
    surfaceVariant = GlassSurfaceVariant,
    onSurfaceVariant = GlassOnSurfaceVariant,
    outline = GlassOutline,
    outlineVariant = GlassOutlineVariant,
    inverseSurface = GlassInverseSurface,
    inverseOnSurface = GlassInverseOnSurface,
    inversePrimary = GlassInversePrimary,
    error = GlassError,
    onError = GlassOnError,
    errorContainer = GlassErrorContainer,
    onErrorContainer = GlassOnErrorContainer,
    surfaceDim = GlassSurfaceDim,
    surfaceBright = GlassSurfaceBright,
    surfaceContainerLowest = GlassSurfaceContainerLowest,
    surfaceContainerLow = GlassSurfaceContainerLow,
    surfaceContainer = GlassSurfaceContainer,
    surfaceContainerHigh = GlassSurfaceContainerHigh,
    surfaceContainerHighest = GlassSurfaceContainerHighest
)

val LateNightDarkColorScheme = darkColorScheme(
    primary = NightPrimary,
    onPrimary = NightOnPrimary,
    primaryContainer = NightPrimaryContainer,
    onPrimaryContainer = NightOnPrimaryContainer,
    secondary = NightSecondary,
    onSecondary = NightOnSecondary,
    secondaryContainer = NightSecondaryContainer,
    onSecondaryContainer = NightOnSecondaryContainer,
    tertiary = NightTertiary,
    onTertiary = NightOnTertiary,
    tertiaryContainer = NightTertiaryContainer,
    onTertiaryContainer = NightOnTertiaryContainer,
    background = NightBackground,
    onBackground = NightOnBackground,
    surface = NightSurface,
    onSurface = NightOnSurface,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = NightOnSurfaceVariant,
    outline = NightOutline,
    outlineVariant = NightOutlineVariant,
    inverseSurface = NightInverseSurface,
    inverseOnSurface = NightInverseOnSurface,
    inversePrimary = NightInversePrimary,
    error = NightError,
    onError = NightOnError,
    errorContainer = NightErrorContainer,
    onErrorContainer = NightOnErrorContainer,
    surfaceDim = NightSurfaceDim,
    surfaceBright = NightSurfaceBright,
    surfaceContainerLowest = NightSurfaceContainerLowest,
    surfaceContainerLow = NightSurfaceContainerLow,
    surfaceContainer = NightSurfaceContainer,
    surfaceContainerHigh = NightSurfaceContainerHigh,
    surfaceContainerHighest = NightSurfaceContainerHighest
)

val DayFocusLightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    inversePrimary = LightInversePrimary,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    surfaceDim = LightSurfaceDim,
    surfaceBright = LightSurfaceBright,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest
)

val LocalIsLiquidGlass = compositionLocalOf { true }

@Composable
fun FocusStudyTheme(
    themeMode: ThemeMode = ThemeMode.LIQUID_GLASS,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        ThemeMode.SYSTEM -> darkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.LIQUID_GLASS -> true
    }

    val colorScheme = when {
        themeMode == ThemeMode.LIQUID_GLASS -> LiquidGlassColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            val isDarkDynamic = when (themeMode) {
                ThemeMode.SYSTEM -> darkTheme
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.LIQUID_GLASS -> true
            }
            if (isDarkDynamic) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeMode == ThemeMode.DARK -> LateNightDarkColorScheme
        themeMode == ThemeMode.LIGHT -> DayFocusLightColorScheme
        darkTheme -> LateNightDarkColorScheme
        else -> DayFocusLightColorScheme
    }

    CompositionLocalProvider(LocalIsLiquidGlass provides (themeMode == ThemeMode.LIQUID_GLASS)) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Retain alias for backward compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    FocusStudyTheme(
        themeMode = if (darkTheme) ThemeMode.DARK else ThemeMode.LIGHT,
        dynamicColor = dynamicColor,
        content = content
    )
}
