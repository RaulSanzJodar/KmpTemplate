package rsanzjo.entreteclasytinta.template.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val primaryLight = Color(0xFF8F4C38)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFFFDBD1)
val onPrimaryContainerLight = Color(0xFF723523)
val secondaryLight = Color(0xFF77574E)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFFFDBD1)
val onSecondaryContainerLight = Color(0xFF5D4037)
val tertiaryLight = Color(0xFF6C5D2F)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFF5E1A7)
val onTertiaryContainerLight = Color(0xFF534619)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF93000A)
val backgroundLight = Color(0xFFFAF8F7)
val onBackgroundLight = Color(0xFF1F1B1A)
val surfaceLight = Color(0xFFFAF8F7)
val onSurfaceLight = Color(0xFF1F1B1A)
val surfaceVariantLight = Color(0xFFEFE6E3)
val onSurfaceVariantLight = Color(0xFF4F4542)
val outlineLight = Color(0xFF80756F)
val outlineVariantLight = Color(0xFFD3C8C4)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF34302F)
val inverseOnSurfaceLight = Color(0xFFF8F0EE)
val inversePrimaryLight = Color(0xFFFFB5A0)
val surfaceDimLight = Color(0xFFE2DBD8)
val surfaceBrightLight = Color(0xFFFAF8F7)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF6F2F0)
val surfaceContainerLight = Color(0xFFF1ECEA)
val surfaceContainerHighLight = Color(0xFFEBE6E3)
val surfaceContainerHighestLight = Color(0xFFE5E0DD)

val primaryDark = Color(0xFFFFB5A0)
val onPrimaryDark = Color(0xFF561F0F)
val primaryContainerDark = Color(0xFF723523)
val onPrimaryContainerDark = Color(0xFFFFDBD1)
val secondaryDark = Color(0xFFE7BDB2)
val onSecondaryDark = Color(0xFF442A22)
val secondaryContainerDark = Color(0xFF5D4037)
val onSecondaryContainerDark = Color(0xFFFFDBD1)
val tertiaryDark = Color(0xFFD8C58D)
val onTertiaryDark = Color(0xFF3B2F05)
val tertiaryContainerDark = Color(0xFF534619)
val onTertiaryContainerDark = Color(0xFFF5E1A7)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF151211)
val onBackgroundDark = Color(0xFFEAE1DF)
val surfaceDark = Color(0xFF151211)
val onSurfaceDark = Color(0xFFEAE1DF)
val surfaceVariantDark = Color(0xFF4F4542)
val onSurfaceVariantDark = Color(0xFFD3C8C4)
val outlineDark = Color(0xFF9B8F8B)
val outlineVariantDark = Color(0xFF4F4542)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFEAE1DF)
val inverseOnSurfaceDark = Color(0xFF34302F)
val inversePrimaryDark = Color(0xFF8F4C38)
val surfaceDimDark = Color(0xFF151211)
val surfaceBrightDark = Color(0xFF3B3736)
val surfaceContainerLowestDark = Color(0xFF100D0C)
val surfaceContainerLowDark = Color(0xFF1D1A19)
val surfaceContainerDark = Color(0xFF211E1D)
val surfaceContainerHighDark = Color(0xFF2C2827)
val surfaceContainerHighestDark = Color(0xFF373332)

val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

/** Colores propios de la app que no existen en el esquema de Material (tarjetas, shimmer...). */
@Immutable
data class DefaultColors(
    val cardBackground: Color,
    val cardBorder: Color,
    val shimmerBase: Color,
    val shimmerHighlight: Color
)

val LightDefaultColors = DefaultColors(
    cardBackground = Color(0xFFFFFFFF),
    cardBorder = Color(0x1F000000),
    shimmerBase = Color(0xFFB8B5B5),
    shimmerHighlight = Color(0xFF8F8B8B)
)

val DarkDefaultColors = DefaultColors(
    cardBackground = Color(0xFF211E1D),
    cardBorder = Color(0x29FFFFFF),
    shimmerBase = Color(0xFF3B3736),
    shimmerHighlight = Color(0xFF2C2827)
)

val LocalDefaultColors = staticCompositionLocalOf { LightDefaultColors }
