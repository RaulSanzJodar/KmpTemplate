package rsanzjo.entreteclasytinta.template.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.PreviewLightDark
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

private val DefaultCardShape = RoundedCornerShape(14.dp)

/** Tarjeta estándar de la app: contenedor de las secciones de la ficha. Clicable si se indica [onClick]. */
@Composable
fun DefaultCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardModifier = modifier.fillMaxWidth()
    val elevation = CardDefaults.cardElevation(2.dp)
    val colors = CardDefaults.cardColors(containerColor = AppTheme.colors.cardBackground)

    if (onClick != null) {
        OutlinedCard(
            onClick = onClick,
            modifier = cardModifier,
            shape = DefaultCardShape,
            elevation = elevation,
            colors = colors,
            content = { content() }
        )
    } else {
        OutlinedCard(
            modifier = cardModifier,
            shape = DefaultCardShape,
            elevation = elevation,
            colors = colors,
            content = { content() }
        )
    }
}

@PreviewLightDark
@Composable
private fun DefaultCardPreview() {
    AppTheme {
        DefaultCard { Text("Contenido de la tarjeta", modifier = Modifier.padding(16.dp)) }
    }
}
