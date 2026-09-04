package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

private data class Phase(val label: String, val title: String, val episodes: String, val status: String, val locked: Boolean)

private val phases = listOf(
    Phase("FASE 1 · SEM 1–4", "Fundamentos y Límites", "12 episodios", "100% offline listo", locked = false),
    Phase("FASE 2 · SEM 5–8", "Movilidad y Cuidados", "16 episodios", "6 descargados", locked = false),
    Phase("FASE 3 · SEM 9–11", "Comunicación y Seguridad", "12 episodios", "Disponible en 21 días", locked = true),
    Phase("FASE 4 · SEM 12–13", "Handoff y Empleabilidad", "8 episodios · certificación final", "Bloqueado", locked = true),
)

/** Mirrors /screens/biblioteca-audios-offline-13-semanas.html. */
@Composable
fun BibliotecaAudiosScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.BibliotecaAudios.title, onBack = onBack) {
        item {
            SectionCard {
                Text(
                    "Micro-Podcasts Gemini · 13 Semanas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Escucha práctica bilingüe sin gastar datos móviles.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LabeledProgress(
                    label = "18 de 52 episodios descargados",
                    progress = 18f / 52f,
                    trailingLabel = "420 MB / 1.2 GB",
                )
                Text(
                    "Descargar solo con Wi-Fi: activado",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Icon(Icons.Outlined.DownloadForOffline, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Descargar Fase 1 Completa (12 Audios)")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Todos (52)", "Descargados (18)", "Pendientes (34)", "Favoritos (6)").forEach {
                        StatusPill(text = it)
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Ruta 90 Días California · 13 semanas total",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                phases.forEach { phase ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (phase.locked) Icons.Outlined.Lock else Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = if (phase.locked) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                            )
                            Column {
                                Text(phase.label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                Text(phase.title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                                Text(phase.episodes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Text(phase.status, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Fase 1 · Semana 1: Rol, Dignidad y Alcance No Médico",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "3 audios conversacionales · 31 min total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Episodio 1.1 — Cómo decir “No Puedo Inyectar” sin sonar grosero",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            "Límites legales en California: negarse con empatía profesional y reporte al supervisor. · 9:45 min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(Icons.Outlined.CheckCircle, contentDescription = "Descargado", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
