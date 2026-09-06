package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.audio.AudioNarrationController
import com.caregiverproca.app.audio.audioResIdFor
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
 * pack ships 13 real narrated episodes, generated offline with Google Cloud
 * Text-to-Speech and bundled as static res/raw resources — see
 * docs/caregiver_upgrade/AUDIO_GENERATION_PROMPT.md — so they are genuinely
 * available offline already, with real playback, no download step needed.
 */
@Composable
fun BibliotecaAudiosScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val narrationController = remember { AudioNarrationController(context) }
    var playingEpisodeId by remember { mutableStateOf<String?>(null) }
    DisposableEffect(Unit) { onDispose { narrationController.release() } }

    DetailScaffold(title = Screen.BibliotecaAudios.title, onBack = onBack) {
        item {
            SectionCard {
                Text(
                    "Guiones Narrados · 13 Semanas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.PlayCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        "Los ${AudioEpisodeOutlines.size} episodios están incluidos en la app con audio narrado real: disponibles sin conexión, sin nada que descargar.",
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
                            val resId = remember(episode.episodeId) { audioResIdFor(episode.episodeId) }
                            val isPlaying = playingEpisodeId == episode.episodeId
                            if (resId != null) {
                                IconButton(onClick = {
                                    if (isPlaying) {
                                        narrationController.stop()
                                        playingEpisodeId = null
                                    } else {
                                        narrationController.play(resId) { playingEpisodeId = null }
                                        playingEpisodeId = episode.episodeId
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Outlined.StopCircle else Icons.Outlined.PlayCircle,
                                        contentDescription = if (isPlaying) "Detener narración" else "Escuchar narración real",
                                        tint = MaterialTheme.colorScheme.primary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
