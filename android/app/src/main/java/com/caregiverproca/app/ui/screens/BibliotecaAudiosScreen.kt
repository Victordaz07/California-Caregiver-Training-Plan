package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.AudioEpisodeOutlines
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.navigation.Screen

private data class Phase(val label: String, val title: String, val weeks: IntRange)

private val phases = listOf(
    Phase("FASE 1", "Fundamentos y Rutas", 1..4),
    Phase("FASE 2", "Seguridad, ADL y Movilidad", 5..8),
    Phase("FASE 3", "Comunicación y Documentación", 9..11),
    Phase("FASE 4", "Medicación y Carrera", 12..13),
)

/**
 * Mirrors /screens/biblioteca-audios-offline-13-semanas.html. Rewritten per
 * A-025: the previous "18 de 52 episodios descargados / 420 MB" state and the
 * "Descargar" button were fabricated (no real download ever happened). This
 * pack ships 13 real script outlines (content/AudioEpisodes.kt), bundled
 * directly in the app, so they are genuinely available offline already — no
 * download step exists or is needed. See docs/caregiver_upgrade/DECISIONS.md
 * ADR-006 for why there is no playable narrated audio yet.
 */
@Composable
fun BibliotecaAudiosScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.BibliotecaAudios.title, onBack = onBack) {
        item {
            SectionCard {
                Text(
                    "Guiones de Práctica · 13 Semanas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "Los ${AudioEpisodeOutlines.size} guiones están incluidos en la app: disponibles sin conexión, sin nada que descargar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        phases.forEach { phase ->
            item {
                SectionCard {
                    Text(
                        "${phase.label} · ${phase.title}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    AudioEpisodeOutlines.filter { it.week in phase.weeks }.forEach { episode ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Semana ${episode.week} · ${episode.titleEs}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    "${episode.targetMinutes} min · “${episode.keyPhraseEn}”",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Icon(Icons.Outlined.CheckCircle, contentDescription = "Disponible sin conexión", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
