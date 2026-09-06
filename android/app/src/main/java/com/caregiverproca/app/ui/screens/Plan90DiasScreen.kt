package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.ui.components.ChecklistRow
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.HubScaffold
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill

/**
 * "Plan" tab root. Mirrors /screens/plan-90-dias-evaluaciones.html, corrected
 * per A-004/A-005/A-011/A-030: hours are labeled "internas" (not
 * "acreditadas"), the goal is internal preparation for a chosen pathway (not
 * a fused "Certificación IHSS/CNA"), and week/day progress are shown as
 * separate, explicitly-labeled fractions instead of one blended percentage.
 *
 * The 90-day curriculum content itself (CURRICULO_90_DIAS.csv, 90 rows) is
 * not imported in this session — this screen still shows week 1 only. See
 * docs/caregiver_upgrade/DECISIONS.md (A-022).
 */
@Composable
fun Plan90DiasScreen() {
    HubScaffold(title = "Plan", subtitle = "90 días · 13 semanas") {
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            "Tu trayectoria de 90 días",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                        )
                        Text(
                            "Plan Maestro 90 Días",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    StatusPill(text = "SEMANA 1 ACTIVA")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    listOf(
                        "DURACIÓN" to "13 sem. (90 días corr.)",
                        "CARGA" to "110 h internas · práctica + teoría",
                        "SEMANA" to "1 / 13",
                    ).forEach { (label, value) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                LabeledProgress(label = "Progreso del plan (días)", progress = 1f / 90f, trailingLabel = "Día 1 / 90")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    listOf("Fase 1: Fundamentos", "Día 30", "Día 60", "Día 90").forEach {
                        Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    }
                }
                Text(
                    "Meta: preparación interna para la ruta que elegiste (Carrera > Mi ruta). " + Disclaimers.Certificate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Column {
                        Text(
                            "CRITERIO INTERNO DE SEGURIDAD",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        Text(
                            "Un único error crítico de seguridad (contaminación cruzada grave, movilización sin frenos de silla o demora mayor a 60s en reporte de emergencia) invalida la evaluación interna en curso de forma inmediata. Esto es un criterio de esta app, no una política de CDSS.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }

        item { DisclaimerBanner(text = Disclaimers.FictionalCase) }

        item {
            SectionCard {
                Text(
                    "Hitos de Práctica Interna",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Evaluaciones internas por trimestre de entrenamiento (no gubernamentales)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text("30D · Evaluación Inicial — Bloque Fundacional (por desbloquear)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                ChecklistRow(done = false, label = "Conocimientos normativos de tu ruta", trailing = "85%")
                ChecklistRow(done = false, label = "Higiene de manos y emergencias (estricto)", trailing = "100%")
                ChecklistRow(done = false, label = "5 demostraciones explicadas (Técnica Feynman)", trailing = "Pendiente")
                ChecklistRow(done = false, label = "CPR / First Aid según tu empleador o programa", trailing = "Pendiente")

                Text("60D · Evaluación Intermedia — Supervisión e Integración de Turno (bloqueado)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                ChecklistRow(done = false, label = "Escenarios ficticios intercalados", trailing = "90%")
                ChecklistRow(done = false, label = "10 procedimientos prácticos demostrados", trailing = "Bloqueado")
                ChecklistRow(done = false, label = "10 notas de práctica de registro diario (DAR)", trailing = "Bloqueado")

                Text("90D · Evaluación Final — Certificado interno de finalización (bloqueado)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                ChecklistRow(done = false, label = "Simulación integral de turno de 8 horas", trailing = "Bloqueado")
                ChecklistRow(done = false, label = "Cero errores críticos en evaluación final", trailing = "Bloqueado")
            }
        }
    }
}
