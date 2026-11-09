package rsanzjo.entreteclasytinta.template.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object AppTheme {
    val colors: DefaultColors
        @Composable
        @ReadOnlyComposable
        get() = LocalDefaultColors.current
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme
    val defaultColors = if (darkTheme) DarkDefaultColors else LightDefaultColors

    CompositionLocalProvider(
        LocalDefaultColors provides defaultColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = appTypography(),
            content = content
        )
    }
}
