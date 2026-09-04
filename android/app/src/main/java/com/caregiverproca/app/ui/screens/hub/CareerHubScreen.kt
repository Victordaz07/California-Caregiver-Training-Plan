package com.caregiverproca.app.ui.screens.hub

import androidx.compose.foundation.lazy.item
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.caregiverproca.app.content.Pathway
import com.caregiverproca.app.ui.components.HubScaffold
import com.caregiverproca.app.ui.components.ScreenNavCard
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.navigation.Screen

/**
 * "Carrera" tab root: ruta activa, requisitos, recursos y metas — ver
 * SCREEN_REQUIREMENTS.md ("Mi ruta"). No se verifica ningún registro o
 * credencial real; solo muestra la ruta que el usuario eligió en onboarding.
 */
@Composable
fun CareerHubScreen(pathway: Pathway, onChangePathway: () -> Unit, onOpenScreen: (Screen) -> Unit) {
    HubScaffold(title = "Carrera", subtitle = pathway.titleEs) {
        item {
            SectionCard {
                Text("Tu ruta", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Text(pathway.titleEs, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                Text(pathway.summaryEs, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                OutlinedButton(onClick = onChangePathway) { Text("Cambiar de ruta") }
            }
        }
        item {
            ScreenNavCard(
                title = Screen.RequisitosCertificacion.title,
                subtitle = Screen.RequisitosCertificacion.subtitle,
                icon = Screen.RequisitosCertificacion.icon,
                onClick = { onOpenScreen(Screen.RequisitosCertificacion) },
            )
        }
        item {
            ScreenNavCard(
                title = Screen.RecursosSemana.title,
                subtitle = Screen.RecursosSemana.subtitle,
                icon = Screen.RecursosSemana.icon,
                onClick = { onOpenScreen(Screen.RecursosSemana) },
            )
        }
        item {
            ScreenNavCard(
                title = Screen.AnalizadorVocacional.title,
                subtitle = Screen.AnalizadorVocacional.subtitle,
                icon = Screen.AnalizadorVocacional.icon,
                onClick = { onOpenScreen(Screen.AnalizadorVocacional) },
            )
        }
    }
}
