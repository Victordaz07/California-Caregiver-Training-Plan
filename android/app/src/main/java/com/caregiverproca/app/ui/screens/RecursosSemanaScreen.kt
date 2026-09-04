package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.ApsStatewidePhone
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.content.PathwayId
import com.caregiverproca.app.content.officialResourcesFor
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

private data class WeekResource(
    val week: String,
    val topic: String,
    val tag: String,
    val body: String,
    val credential: String,
    val credentialNote: String,
    val actionLabel: String,
)

private val weekResources = listOf(
    WeekResource(
        "Semana 1 · Fundamentos", "Rol, Dignidad y Alcance Legal", "CDSS HCSB",
        "California Health and Safety Code § 1796.12 y CDSS Title 22 Division 6. Define el rango de asistencia no médica y la protección legal del usuario.",
        "CDSS Registered Home Care Aide", "Vía Guardian Portal CA", "Ver Guía",
    ),
    WeekResource(
        "Semana 2 · Bioseguridad", "Control de Infecciones y Patógenos", "Gratuito",
        "Guías CDC Project Firstline para personal clínico y Cal/OSHA Bloodborne Pathogens Standard § 5193.",
        "Certificado CDC Project Firstline", "Micro-credencial online gratuita", "Comenzar",
    ),
    WeekResource(
        "Semana 3 · Soporte Vital", "Emergencias, SVB y Primeros Auxilios", "Presencial",
        "AHA BLS (Basic Life Support) y American Red Cross Adult/Pediatric CPR/AED. Puede ser recomendado o exigido por tu empleador, programa o puesto — HSC §1796.43 no establece un mandato general de CPR/AED; verifica tu ruta.",
        "Cruz Roja / AHA BLS Provider", "Práctica presencial obligatoria", "Sedes CA",
    ),
    WeekResource(
        "Semana 5 · Movilidad", "Mecánica Corporal y Transferencias", "NIOSH / OSHA",
        "Directrices NIOSH Safe Patient Handling y ergonomía clínica de Cal/OSHA para transferencias con sábana deslizante, arnés Hoyer y prevención de lumbalgias.",
        "NIOSH Safe Patient Handling", "Guía técnica gratuita", "Ver Guía",
    ),
    WeekResource(
        "Semana 8 · Especialidad", "Alzheimer, Demencias y Comportamiento", "NIA & CA Cares",
        "National Institute on Aging (NIA/NIH) Guidelines + Certificación CA Cares Dementia Specialist de Alzheimer's Association (essentiALZ).",
        "CA Cares Dementia Specialist", "essentiALZ · Alzheimer's Association", "Comenzar",
    ),
)

/**
 * Mirrors /screens/recursos-oficiales-certificaciones-semana.html. Corrected
 * per A-042 (fecha de verificación actualizada) y A-017 (se retira el
 * "+35% Est." de salario sin fuente/condado/fecha/método, per A-018).
 */
@Composable
fun RecursosSemanaScreen(pathwayId: PathwayId, onBack: () -> Unit) {
    DetailScaffold(title = Screen.RecursosSemana.title, onBack = onBack) {
        item { DisclaimerBanner(text = Disclaimers.Sources) }
        item {
            SectionCard {
                StatusPill(text = "REVISADO 2026-09-04 (VER FECHA POR FUENTE)")
                Text(
                    "Recursos Oficiales, Licencias y Certificaciones",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Ecosistema regulatorio oficial (CDSS, CDPH, CDC, CMS, OSHA, Cruz Roja) y certificaciones de alto valor para planificar tu futuro profesional en California.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    listOf(
                        "PORTALES CA" to "6 Activos",
                        "MICRO-CRÉDITOS" to "4 Gratuitos",
                        "LÍNEA APS" to ApsStatewidePhone,
                    ).forEach { (label, value) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Filtros de Catálogo — Mostrando 6 semanas y 4 portales",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Todos (28)", "Estatal CA", "Federal", "Certificaciones").forEach {
                        StatusPill(text = it)
                    }
                }
                Text(
                    "Vínculo con Plan 90D — Semana Activa: 1 a 4. Cada semana del programa se fundamenta en códigos de California y guías de salud pública federales para otorgarte validez institucional inmediata.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(weekResources) { resource ->
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        resource.week,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                    StatusPill(text = resource.tag)
                }
                Text(
                    resource.topic,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    resource.body,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    resource.credential,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    resource.credentialNote,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                )
                OutlinedButton(onClick = { }) { Text(resource.actionLabel) }
            }
        }

        item {
            Text(
                "Enlaces oficiales de tu ruta",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        items(officialResourcesFor(pathwayId)) { resource ->
            SectionCard {
                Text(resource.titleEs, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(resource.agency, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
                Text(resource.url, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                if (resource.phone != null) {
                    Text("Teléfono: ${resource.phone}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}
