package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.content.curriculumDayFor
import com.caregiverproca.app.data.LearningRepository
import com.caregiverproca.app.data.UserPreferencesRepository
import com.caregiverproca.app.data.UserPreferencesState
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.HubScaffold
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class SessionBlock(val titleEs: String, val minutes: Int)

/** The five techniques of Fase 4, 75 minutes total: 10+15+25+10+15. */
private val SessionBlocks = listOf(
    SessionBlock("Recuerdo activo", 10),
    SessionBlock("Estudio enfocado", 15),
    SessionBlock("Práctica deliberada", 25),
    SessionBlock("Explicación Feynman", 10),
    SessionBlock("Repetición espaciada", 15),
)

/**
 * "Hoy" tab root. Mirrors /screens/rutina-diaria-75-minutos.html, now with a
 * real resumable timer (Fase 4) instead of a static "0/75 min" label, and
 * today's real curriculum content from content/Curriculum.kt instead of a
 * fixed "Semana 1" banner.
 *
 * The timer survives rotation (`rememberSaveable`) but NOT a full process
 * death while running mid-session — that would need a foreground service or
 * WorkManager, out of scope for this batch. See docs/caregiver_upgrade/DECISIONS.md.
 * The A-008 emergency override is unconditional: the banner is always shown,
 * and nothing in this screen blocks the user from leaving to call 911.
 */
@Composable
fun RutinaDiariaScreen() {
    val context = LocalContext.current
    val learningRepository = remember { LearningRepository(context) }
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val scope = rememberCoroutineScope()

    val prefs by userPreferencesRepository.state.collectAsState(
        initial = UserPreferencesState(pathwayId = null, onboardingCompleted = false, currentPlanDay = 1, completedBlocksToday = 0),
    )
    val currentDay = prefs.currentPlanDay
    val today = curriculumDayFor(currentDay)

    var blockIndex by rememberSaveable { mutableIntStateOf(0) }
    var secondsLeftInBlock by rememberSaveable { mutableIntStateOf(SessionBlocks[0].minutes * 60) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var sessionCompleted by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isRunning, sessionCompleted) {
        while (isRunning && !sessionCompleted) {
            delay(1000)
            if (secondsLeftInBlock > 1) {
                secondsLeftInBlock -= 1
            } else if (blockIndex < SessionBlocks.lastIndex) {
                blockIndex += 1
                secondsLeftInBlock = SessionBlocks[blockIndex].minutes * 60
            } else {
                isRunning = false
                sessionCompleted = true
                scope.launch {
                    learningRepository.markDayCompleted(currentDay)
                    userPreferencesRepository.setCurrentPlanDay((currentDay + 1).coerceAtMost(90))
                }
            }
        }
    }

    val totalSeconds = SessionBlocks.sumOf { it.minutes * 60 }
    val elapsedBeforeBlock = SessionBlocks.take(blockIndex).sumOf { it.minutes * 60 }
    val elapsedSeconds = if (sessionCompleted) totalSeconds else elapsedBeforeBlock + (SessionBlocks[blockIndex].minutes * 60 - secondsLeftInBlock)

    HubScaffold(title = "Hoy", subtitle = "Día $currentDay · Semana ${today?.week ?: 1}") {
        item { DisclaimerBanner(text = Disclaimers.Emergency, emergency = true) }

        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                StatusPill(
                    text = "MÓDULO: ${today?.moduleId?.uppercase() ?: "FUNDAMENTOS"}",
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    today?.titleEs ?: "Plan completado",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    today?.objectiveEs ?: "Has llegado al final del plan de 90 días registrado en este lote.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        item {
            SectionCard {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Column {
                        Text(
                            "SESIÓN GUIADA · ${SessionBlocks[blockIndex].titleEs}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                        Text(
                            "Rutina de Hoy",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                LabeledProgress(
                    label = if (sessionCompleted) "75 / 75 min" else "${elapsedSeconds / 60} / 75 min",
                    progress = elapsedSeconds.toFloat() / totalSeconds.toFloat(),
                    trailingLabel = "${(elapsedSeconds * 100 / totalSeconds)}% completado",
                )

                if (today?.practiceEs != null && !sessionCompleted) {
                    Text(
                        "Práctica de hoy: ${today.practiceEs}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            if (sessionCompleted) "Día $currentDay completado" else "Bloque ${blockIndex + 1} de 5 · ${secondsLeftInBlock / 60}:${(secondsLeftInBlock % 60).toString().padStart(2, '0')}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (!sessionCompleted) {
                        Button(
                            onClick = { isRunning = !isRunning },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary,
                            ),
                        ) {
                            Icon(if (isRunning) Icons.Outlined.Pause else Icons.Outlined.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text(if (isRunning) "Pausar" else if (elapsedSeconds > 0) "Reanudar" else "Iniciar Sesión")
                        }
                    }
                }
            }
        }

        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        Icons.Outlined.PriorityHigh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Column {
                        Text(
                            "REGLA INVIOLABLE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        Text(
                            "Cero errores críticos: jamás emitir diagnósticos médicos, prescribir ni realizar administración invasiva de fármacos. Tu rol protege la dignidad dentro del marco no médico.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }
    }
}
