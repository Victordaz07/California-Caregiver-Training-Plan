package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

private data class Chapter(val time: String, val title: String, val description: String, val status: String)

private val chapters = listOf(
    Chapter("00:00", "1. El Escenario Real", "Qué se siente cuando Don Arturo aparta el plato o niega con firmeza la asistencia matutina.", "Escuchado"),
    Chapter("02:30", "2. Desglose Conversacional", "Por qué la técnica de validación emocional en 3 pasos desactiva la frustración antes del conflicto.", "En curso"),
    Chapter("06:15", "3. Diálogos Modelo: lo que NUNCA debes decir", "Comparativa sonora: la frase que genera resistencia vs. la Frase Mágica de Calma.", "Pendiente"),
    Chapter("09:30", "4. Tres Puntos de Apoyo Centrado en la Persona", "Autonomía del cliente, no forzamiento y práctica de reporte DAR, explicados en lenguaje sencillo.", "Pendiente"),
)

/**
 * Mirrors /screens/audio-lecciones-gemini-manos-libres.html. Corrected per
 * A-028: renamed from "Audio-Tutor Gemini" a "orientación guiada" — no hay
 * backend de IA real conectado en esta app todavía, así que no se puede
 * llamar "IA en vivo" ni atribuir la narración a un modelo específico.
 */
@Composable
fun AudioLeccionesScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.AudioLecciones.title, onBack = onBack) {
        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(
                        "Orientación Guiada: Audio Conversacional",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Text(
                    "Narración pre-grabada en tono cálido y bilingüe, como un micro-podcast para escuchar mientras cocinas, caminas o te trasladas entre turnos. Sin tecnicismos vacíos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Mic, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text(
                        "Manos Libres Activo — di “Pausa” o “Repite punto”",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Semana 1 · Plan 90 Días — Audio Conversacional",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                Text(
                    "Lección 1.3: Cómo actuar ante el rechazo sin confrontar",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "La regla de oro de la empatía práctica en el cuidado diario",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LabeledProgress(label = "Capítulo 2 de 4", progress = 3.72f / 11.75f, trailingLabel = "03:42 / 11:45")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    OutlinedButton(onClick = { }) { Text("-15s") }
                    OutlinedButton(onClick = { }) { Text("Pausa") }
                    OutlinedButton(onClick = { }) { Text("+30s") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatusPill(text = "1.0x")
                    StatusPill(text = "1.2x")
                    StatusPill(text = "1.5x")
                    StatusPill(text = "ES (CÁLIDO)")
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Estructura del Episodio Conversacional · 4 Capítulos",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Sin lecturas áridas. Cada segmento está diseñado para enseñarte con historias reales de turnos en California.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                chapters.forEach { chapter ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "${chapter.time} · ${chapter.title}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                chapter.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            chapter.status,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Repaso Formal al Terminar Turno",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Práctica guiada y fuentes oficiales verificadas — no es un requisito impuesto por CDSS.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
