package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
 * backend is connected). This screen now shows the real 13-episode outline
 * bank (content/AudioEpisodes.kt) as read-along scripts — there is no
 * narrated audio in the source pack to play, so the "-15s/Pausa/+30s" fake
 * transport controls from the previous mock are gone. What IS real: a
 * shadowing/recording exercise for today's key phrase via VoiceRecordCard.
 * See docs/caregiver_upgrade/DECISIONS.md ADR-006.
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
                    "Guiones de práctica bilingüe para leer en voz alta y grabarte practicando, como preparación para escuchar mientras cocinas, caminas o te trasladas entre turnos. Sin tecnicismos vacíos.",
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
                Text(
                    "${todaysEpisode.targetMinutes} min sugeridos · ${todaysEpisode.learnerPromptEs}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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

        item { VoiceRecordCard(promptLabel = "Practica y grábate diciendo la frase clave de esta semana.") }

        item {
            Text(
                "Biblioteca de Guiones · 13 Semanas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        items(AudioEpisodeOutlines) { episode -> AudioEpisodeCard(episode) }
    }
}

@Composable
private fun AudioEpisodeCard(episode: AudioEpisodeOutline) {
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
            StatusPill(text = "${episode.targetMinutes} MIN")
        }
    }
}
