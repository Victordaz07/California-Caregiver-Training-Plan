package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.ui.components.DetailScaffold
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

/** Mirrors /screens/analizador-vocacional-metas-carrera.html. */
@Composable
fun AnalizadorVocacionalScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.AnalizadorVocacional.title, onBack = onBack) {
        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                StatusPill(
                    text = "CA REGISTRY VALIDADO",
                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                    contentColor = MaterialTheme.colorScheme.onPrimaryFixedVariant,
                )
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
                    "Potencial de ingreso hasta \$38/h en CA",
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
                    text = "RECOMENDADA PARA TI · MATCH 94%",
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    "Cuidador Senior en Demencia y Cuidados Paliativos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Rango Salarial CA: \$24–\$28/hora · Tiempo Estimado: 6 a 12 meses",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "4 Pasos Clave para Consolidarlo",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                NumberedStep(1, "Concluir Plan 90D", "Registro HCA Guardian activo en CDSS.")
                NumberedStep(2, "Certificación essentiALZ", "Certificación de especialista en demencia de Alzheimer's Association.")
            }
        }
    }
}
