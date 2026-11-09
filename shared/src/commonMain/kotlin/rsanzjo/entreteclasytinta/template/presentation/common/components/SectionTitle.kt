package rsanzjo.entreteclasytinta.template.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import rsanzjo.entreteclasytinta.template.ui.theme.AppTheme

/**
 * Título de sección de la app (asistente de creación, hojas de detalle, perfil...).
 * El espaciado lo decide cada pantalla con [modifier]; por defecto deja un pequeño margen superior.
 */
@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier.padding(top = 8.dp),
    color: Color = MaterialTheme.colorScheme.primary
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier.semantics { heading() }
    )
}

@PreviewLightDark
@Composable
private fun SectionTitlePreview() {
    AppTheme {
        Column { SectionTitle("Rasgos") }
    }
}
