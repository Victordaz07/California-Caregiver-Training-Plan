package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.NumberedStep
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

private data class Dimension(val label: String, val title: String, val score: Float, val description: String, val tags: List<String>)

private val dimensions = listOf(
    Dimension(
        "DIMENSIÓN A · PRIORIDAD ALTA", "Enfoque Humano y Compañía", 0.85f,
        "Cuidado domiciliario privado, apoyo emocional en demencias tempranas, cocina terapéutica y acompañamiento 1 a 1 sin la prisa de turnos hospitalarios saturados.",
        listOf("Empatía Activa", "Nutrición Geriátrica", "Match Ideal"),
    ),
    Dimension(
        "DIMENSIÓN B · ALTO POTENCIAL", "Independencia y Emprendimiento", 0.75f,
        "Negociación directa con familias privadas (Private Pay), registro IHSS independiente o proyección hacia tu propia microagencia de cuidadores en California.",
        listOf("Contratos Directos", "Tarifa Propia"),
    ),
    Dimension(
        "DIMENSIÓN C · EN CRECIMIENTO", "Ruta Técnica y Clínica", 0.60f,
        "Monitoreo preciso de signos vitales, administración de fármacos asistida, y aspiración formal a CNA, LVN o Enfermería supervisada.",
        listOf("CDPH Protocol", "Vía NNAAP"),
    ),
)

/**
 * Mirrors /screens/analizador-vocacional-metas-carrera.html. Corrected per
 * A-017/A-018: unsourced salary figures ($38/h, $24–$28/hora) are removed —
 * COPY_REPLACEMENTS.csv requires a source, county, date and method before any
 * dollar amount can be shown. The "CA REGISTRY VALIDADO" pill is also removed:
 * this app does not verify or validate a real CDSS registry entry.
 */
@Composable
fun AnalizadorVocacionalScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.AnalizadorVocacional.title, onBack = onBack) {
        item { DisclaimerBanner(text = Disclaimers.Sources) }
        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Text(
                    "Tu Diagnóstico de Carrera",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    "Afinidad Máxima: Especialista Humano",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    "Los rangos salariales varían por condado, empleador y experiencia. Consulta fuentes oficiales antes de tomar decisiones económicas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            "3 Dimensiones de Afinidad",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            "Resultado de tu test de vocación y ritmo laboral",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    OutlinedButton(onClick = { }) { Text("Calibrar") }
                }
                dimensions.forEach { dimension ->
                    Column {
                        Text(dimension.label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        LabeledProgress(
                            label = dimension.title,
                            progress = dimension.score,
                            trailingLabel = "${(dimension.score * 100).toInt()}%",
                        )
                        Text(
                            dimension.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            dimension.tags.forEach { StatusPill(text = it) }
                        }
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Rutas de Carrera Recomendadas — Basadas en California",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                StatusPill(
                    text = "RECOMENDADA SEGÚN TUS RESPUESTAS",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    "Cuidador Senior en Demencia y Cuidados Paliativos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Tiempo Estimado: 6 a 12 meses. El salario varía por condado, empleador y experiencia — consulta fuentes oficiales antes de decidir.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "4 Pasos Clave para Consolidarlo",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                NumberedStep(1, "Concluir Plan 90D", "Registro HCA vigente en Guardian (CDSS), si tu ruta lo requiere.")
                NumberedStep(2, "Certificación essentiALZ", "Certificación de especialista en demencia de Alzheimer's Association.")
            }
        }
    }
}
