package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.ui.components.ChecklistRow
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

/** Mirrors /screens/plan-90-dias-evaluaciones.html. */
@Composable
fun Plan90DiasScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.Plan90Dias.title, onBack = onBack) {
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            "Trayectoria CDSS California",
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
                        "CARGA" to "110 h · práctica + teoría",
                        "PROGRESO" to "8% (1/13 semanas)",
                    ).forEach { (label, value) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                LabeledProgress(label = "Inicio: Día 1 · Meta: Certificación IHSS/CNA", progress = 0.08f, trailingLabel = "Día 90")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    listOf("Fase 1: Fundamentos", "Día 30", "Día 60", "Día 90").forEach {
                        Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }

        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.secondaryFixed) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Column {
                        Text(
                            "MANDATO DE SEGURIDAD — REGLA DE ORO EN EVALUACIONES",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryFixedVariant,
                        )
                        Text(
                            "Un único error crítico de seguridad (contaminación cruzada grave, movilización sin frenos de silla o demora mayor a 60s en reporte de emergencia) invalida la evaluación en curso de forma inmediata.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Hitos de Dominio Clínico",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Evaluaciones obligatorias por trimestre de entrenamiento",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Text("30D · Evaluación Inicial — Bloque Fundacional (por desbloquear)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                ChecklistRow(done = false, label = "Conocimientos normativos CDSS", trailing = "85%")
                ChecklistRow(done = false, label = "Higiene de manos y emergencias (estricto)", trailing = "100%")
                ChecklistRow(done = false, label = "5 demostraciones explicadas (Técnica Feynman)", trailing = "Pendiente")
                ChecklistRow(done = false, label = "CPR / First Aid con reserva confirmada en CA", trailing = "Pendiente")

                Text("60D · Evaluación Intermedia — Supervisión e Integración de Turno (bloqueado)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                ChecklistRow(done = false, label = "Escenarios clínicos intercalados", trailing = "90%")
                ChecklistRow(done = false, label = "10 procedimientos prácticos demostrados", trailing = "Bloqueado")
                ChecklistRow(done = false, label = "10 notas objetivas de registro diario (DAR/SOAP)", trailing = "Bloqueado")

                Text("90D · Evaluación Final — Certificación IHSS/CNA (bloqueado)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                ChecklistRow(done = false, label = "Simulación integral de turno de 8 horas", trailing = "Bloqueado")
                ChecklistRow(done = false, label = "Cero errores críticos en evaluación final", trailing = "Bloqueado")
            }
        }
    }
}
