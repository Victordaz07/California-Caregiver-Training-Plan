package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
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
import com.caregiverproca.app.content.AudioEpisodeOutline
import com.caregiverproca.app.content.AudioEpisodeOutlines
import com.caregiverproca.app.content.audioEpisodeFor
import com.caregiverproca.app.content.curriculumDayFor
import com.caregiverproca.app.data.UserPreferencesRepository
import com.caregiverproca.app.data.UserPreferencesState
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.components.VoiceRecordCard
import com.caregiverproca.app.ui.navigation.Screen

/**
 * Mirrors /screens/audio-lecciones-gemini-manos-libres.html. Corrected per
 * A-028: renamed from "Audio-Tutor Gemini" to "orientación guiada" (no AI
 * backend is connected). Shows the real 13-episode bank (content/AudioEpisodes.kt)
 * with two real, distinct actions per episode: (1) listen to the narrated audio,
 * generated offline with Google Cloud Text-to-Speech and bundled as a static
 * res/raw resource — see docs/caregiver_upgrade/AUDIO_GENERATION_PROMPT.md — and
 * (2) record yourself repeating the key phrase via VoiceRecordCard. Neither uses
 * any backend at runtime; the TTS key was only ever used offline to produce these
 * files. See docs/caregiver_upgrade/DECISIONS.md ADR-006 for the full history.
 */
@Composable
fun AudioLeccionesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val prefs by userPreferencesRepository.state.collectAsState(
        initial = UserPreferencesState(pathwayId = null, onboardingCompleted = false, currentPlanDay = 1, completedBlocksToday = 0),
    )
    val currentWeek = curriculumDayFor(prefs.currentPlanDay)?.week ?: 1
    val todaysEpisode = audioEpisodeFor(currentWeek) ?: AudioEpisodeOutlines.first()

    val narrationController = remember { AudioNarrationController(context) }
    var playingEpisodeId by remember { mutableStateOf<String?>(null) }
    DisposableEffect(Unit) { onDispose { narrationController.release() } }

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
                    "Guiones narrados bilingües, ya incluidos sin conexión: escúchalos y luego grábate practicando, como preparación para el trabajo mientras cocinas, caminas o te trasladas entre turnos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        item {
            SectionCard {
                Text(
                    "Semana $currentWeek · Episodio de Hoy",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                Text(
                    todaysEpisode.titleEs,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NarrationPlayButton(
                        episode = todaysEpisode,
                        controller = narrationController,
                        playingEpisodeId = playingEpisodeId,
                        onPlayingEpisodeIdChange = { playingEpisodeId = it },
                    )
                    Text(
                        "${todaysEpisode.targetMinutes} min sugeridos · ${todaysEpisode.learnerPromptEs}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text("Frase clave en inglés", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(
                    "“${todaysEpisode.keyPhraseEn}”",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Pronunciación aproximada: ${todaysEpisode.pronunciationEs}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item { VoiceRecordCard(promptLabel = "Escucha la narración y luego grábate repitiendo la frase clave de esta semana.") }

        item {
            Text(
                "Biblioteca de Guiones · 13 Semanas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        items(AudioEpisodeOutlines) { episode ->
            AudioEpisodeCard(
                episode = episode,
                controller = narrationController,
                playingEpisodeId = playingEpisodeId,
                onPlayingEpisodeIdChange = { playingEpisodeId = it },
            )
        }
    }
}

@Composable
private fun NarrationPlayButton(
    episode: AudioEpisodeOutline,
    controller: AudioNarrationController,
    playingEpisodeId: String?,
    onPlayingEpisodeIdChange: (String?) -> Unit,
) {
    val resId = remember(episode.episodeId) { audioResIdFor(episode.episodeId) } ?: return
    val isPlaying = playingEpisodeId == episode.episodeId
    IconButton(onClick = {
        if (isPlaying) {
            controller.stop()
            onPlayingEpisodeIdChange(null)
        } else {
            controller.play(resId) { onPlayingEpisodeIdChange(null) }
            onPlayingEpisodeIdChange(episode.episodeId)
        }
    }) {
        Icon(
            imageVector = if (isPlaying) Icons.Outlined.StopCircle else Icons.Outlined.PlayCircle,
            contentDescription = if (isPlaying) "Detener narración" else "Escuchar narración real",
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun AudioEpisodeCard(
    episode: AudioEpisodeOutline,
    controller: AudioNarrationController,
    playingEpisodeId: String?,
    onPlayingEpisodeIdChange: (String?) -> Unit,
) {
    SectionCard {
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
                    "“${episode.keyPhraseEn}”",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            NarrationPlayButton(episode, controller, playingEpisodeId, onPlayingEpisodeIdChange)
            StatusPill(text = "${episode.targetMinutes} MIN")
        }
    }
}
