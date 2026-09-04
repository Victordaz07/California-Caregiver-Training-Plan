package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.ui.components.ChecklistRow
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

/**
 * Mirrors /screens/simulacion-de-turno-y-handoff.html. Corrected per A-014 /
 * A-039: DAR is labeled as a practice format (not an official CDSS/SOC form),
 * "Elena Morales" is marked fictional, and the "supervisión CDSS" and
 * "alcance CDSS Title 22" mis-attributions from COPY_REPLACEMENTS.csv are
 * removed.
 */
@Composable
fun SimulacionTurnoScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.SimulacionTurno.title, onBack = onBack) {
        item { DisclaimerBanner(text = Disclaimers.FictionalCase) }
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            "Semana 12 · Módulo Práctico",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                        )
                        Text(
                            "Simulación #2 de 3",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    StatusPill(text = "DÍA 60")
                }
                Text(
                    "Sra. Elena Morales, 82 años",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "IHSS / Cuidado Domiciliario (Los Ángeles) · Turno 14:00 – 22:00",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Deterioro Cognitivo Leve", "Hipertensión", "Asistencia AVDS Nivel 2").forEach {
                        StatusPill(
                            text = it,
                            containerColor = MaterialTheme.colorScheme.tertiaryFixed,
                            contentColor = MaterialTheme.colorScheme.onTertiaryFixedVariant,
                        )
                    }
                }
            }
        }

        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.secondaryFixed) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Column {
                        Text(
                            "REGLA DE SEGURIDAD CRÍTICA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryFixedVariant,
                        )
                        Text(
                            "Cero omisiones en entrega de guardia (Handoff). Todo evento imprevisto se practica aquí con la estructura DAR (Datos, Acción, Respuesta) — una técnica de práctica que debes adaptar al formato real de tu empleador y plan de cuidado, sin diagnósticos no autorizados.",
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
                    "Reto Handoff < 60 Segundos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Práctica oral con rúbrica interna",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "00:48 / 01:00 max",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        "Grabando informe verbal · Audio WAV (16kHz)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                ) {
                    Icon(Icons.Outlined.Mic, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Pausar / Finalizar Audio de Entrega")
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Criterios Evaluados en Tiempo Real",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                ChecklistRow(done = true, label = "Identificación clara del cliente, hora y estado de alerta", trailing = "100%")
                ChecklistRow(done = true, label = "Hechos observables y signos objetivos (sin diagnósticos)", trailing = "100%")
                ChecklistRow(done = true, label = "Acciones según el plan de cuidado y tu alcance de función", trailing = "100%")
                ChecklistRow(done = false, label = "Transferencia clara de tareas pendientes a Marta S.", trailing = "Pendiente")
            }
        }

        item {
            SectionCard {
                Text(
                    "Práctica de Bitácora del Turno (DAR) · 3 Entradas Clave",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "15:30 · Nutrición e Hidratación",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "Cliente consumió 80% de sopa de verduras casera y 250 ml de agua tibia. Medicación oral vespertina autoadministrada por la usuaria con recordatorio verbal conforme al plan IHSS.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "17:45 · Incidente / Cambio Objetivo (DAR Estricto)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "[D] Sin diagnóstico clínico emitido. Presión arterial no tomada (fuera de alcance). Pulso radial regular; piel tibia, sin diaforesis ni temblores.\n" +
                        "[A] Asistencia inmediata a posición supina en cama baja, barandilla elevada, 150 ml de agua fresca. Llamada informativa a la hija apoderada a las 17:52.\n" +
                        "[R] El mareo cesó a los 15 minutos de reposo; usuaria orientada en persona, tiempo y espacio.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
