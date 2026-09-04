package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
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
import com.caregiverproca.app.ui.components.VoiceRecordCard
import com.caregiverproca.app.ui.navigation.Screen

/**
 * Mirrors /screens/simulacion-de-turno-y-handoff.html. Corrected per A-014 /
 * A-028 / A-039: DAR is labeled as a practice format (not an official
 * CDSS/SOC form), "Elena Morales" is marked fictional, the "supervisión
 * CDSS"/"alcance CDSS Title 22" mis-attributions are removed, the fake
 * "Criterios Evaluados en Tiempo Real" (100% scores nothing actually
 * measured) becomes a self-check, and the fake recording UI is replaced by
 * VoiceRecordCard (real local recording).
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
                    "Práctica oral con rúbrica interna. Intenta no pasar de 60 segundos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item { VoiceRecordCard(promptLabel = "Graba tu entrega de turno de 60 segundos: situación, hechos, acciones y pendiente.") }

        item {
            SectionCard {
                Text(
                    "Autoevaluación del Handoff",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Escucha tu grabación y marca honestamente qué incluiste. Nadie más evalúa esto por ti.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                ChecklistRow(done = false, label = "Identificación clara del cliente, hora y estado de alerta", trailing = "Autorevisar")
                ChecklistRow(done = false, label = "Hechos observables y signos objetivos (sin diagnósticos)", trailing = "Autorevisar")
                ChecklistRow(done = false, label = "Acciones según el plan de cuidado y tu alcance de función", trailing = "Autorevisar")
                ChecklistRow(done = false, label = "Transferencia clara de tareas pendientes a Marta S.", trailing = "Autorevisar")
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
