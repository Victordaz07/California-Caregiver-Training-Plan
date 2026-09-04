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
import com.caregiverproca.app.ui.components.DetailScaffold
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
        "AHA BLS (Basic Life Support) y American Red Cross Adult/Pediatric CPR/AED. Obligatorio según § 1796.43 antes de la asignación domiciliaria independiente.",
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

/** Mirrors /screens/recursos-oficiales-certificaciones-semana.html. */
@Composable
fun RecursosSemanaScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.RecursosSemana.title, onBack = onBack) {
        item {
            SectionCard {
                StatusPill(text = "100% VERIFICADO CA 2025 · REV. MARZO 2025")
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
                        "IMPACTO SALARIO" to "+35% Est.",
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
    }
}
