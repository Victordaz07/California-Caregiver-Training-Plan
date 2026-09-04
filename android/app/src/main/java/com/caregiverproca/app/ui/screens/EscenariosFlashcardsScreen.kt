package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.content.Flashcard
import com.caregiverproca.app.content.Flashcards
import com.caregiverproca.app.content.Scenario
import com.caregiverproca.app.content.ScenarioOutcome
import com.caregiverproca.app.content.Scenarios
import com.caregiverproca.app.content.curriculumDayFor
import com.caregiverproca.app.content.rubricCriterion
import com.caregiverproca.app.data.LearningRepository
import com.caregiverproca.app.data.UserPreferencesRepository
import com.caregiverproca.app.data.UserPreferencesState
import com.caregiverproca.app.domain.ReviewRating
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen
import kotlinx.coroutines.launch

/**
 * Mirrors /screens/escenarios-y-flashcards.html, rewritten to use the real
 * content bank (39 cards, 13 branching scenarios — content/Flashcards.kt,
 * content/Scenarios.kt) and a real due-queue backed by Room
 * (data/LearningRepository.kt + domain/ReviewScheduler.kt), instead of one
 * hardcoded card and one hardcoded case.
 */
@Composable
fun EscenariosFlashcardsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val learningRepository = remember { LearningRepository(context) }
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val scope = rememberCoroutineScope()

    val prefs by userPreferencesRepository.state.collectAsState(
        initial = UserPreferencesState(pathwayId = null, onboardingCompleted = false, currentPlanDay = 1, completedBlocksToday = 0),
    )
    val currentWeek = curriculumDayFor(prefs.currentPlanDay)?.week ?: 1
    val todaysScenario = Scenarios.firstOrNull { it.week == currentWeek } ?: Scenarios.first()

    var dueQueue by remember { mutableStateOf<List<String>>(emptyList()) }
    var queueIndex by remember { mutableStateOf(0) }
    var revealed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dueQueue = learningRepository.dueCardIds(Flashcards.map { it.id })
    }

    val currentCard: Flashcard? = dueQueue.getOrNull(queueIndex)?.let { id -> Flashcards.firstOrNull { it.id == id } }

    DetailScaffold(title = Screen.EscenariosFlashcards.title, onBack = onBack) {
        item { DisclaimerBanner(text = Disclaimers.FictionalCase) }

        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text("Repaso Activo", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                        Text("Práctica interna · Semana $currentWeek", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    StatusPill(text = "${dueQueue.size} PENDIENTES")
                }
            }
        }

        item {
            if (currentCard == null) {
                SectionCard {
                    Text(
                        "No hay tarjetas pendientes de repaso ahora mismo. Vuelve más tarde o avanza en tu plan diario.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            } else {
                FlashcardReview(
                    card = currentCard,
                    total = dueQueue.size,
                    index = queueIndex,
                    revealed = revealed,
                    onReveal = { revealed = true },
                    onRate = { rating ->
                        scope.launch { learningRepository.rateCard(currentCard.id, rating) }
                        revealed = false
                        queueIndex += 1
                    },
                )
            }
        }

        item { ScenarioPractice(scenario = todaysScenario) }
    }
}

@Composable
private fun FlashcardReview(
    card: Flashcard,
    total: Int,
    index: Int,
    revealed: Boolean,
    onReveal: () -> Unit,
    onRate: (ReviewRating) -> Unit,
) {
    SectionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Flashcard Situacional", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
            Text("Tarjeta ${index + 1} de $total", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
        }
        if (card.critical) {
            StatusPill(
                text = "TEMA CRÍTICO DE SEGURIDAD",
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
        Text(
            card.questionEs,
            style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (!revealed) {
            Text(
                "Respóndete a ti mismo antes de revelar la respuesta.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Button(
                onClick = onReveal,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text("Revelar Respuesta")
            }
        } else {
            Text(
                card.answerEs,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                "¿Qué tan bien la recordaste?",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf(
                    ReviewRating.AGAIN to "Otra vez",
                    ReviewRating.HARD to "Difícil",
                    ReviewRating.GOOD to "Bien",
                    ReviewRating.EASY to "Fácil",
                ).forEach { (rating, label) ->
                    OutlinedButton(onClick = { onRate(rating) }, modifier = Modifier.weight(1f)) {
                        Text(label, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScenarioPractice(scenario: Scenario) {
    var selectedChoiceId by remember(scenario.id) { mutableStateOf<String?>(null) }

    SectionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Escenario de la Semana · ${scenario.titleEs}", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        }
        StatusPill(text = "CASO FICTICIO · SEMANA ${scenario.week}")
        Text(scenario.stemEs, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)

        val selectedChoice = scenario.choices.firstOrNull { it.id == selectedChoiceId }
        if (selectedChoice == null) {
            Text(
                "Elige una respuesta antes de ver la retroalimentación.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            scenario.choices.forEach { choice ->
                OutlinedButton(
                    onClick = { selectedChoiceId = choice.id },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(choice.textEs, style = MaterialTheme.typography.bodyMedium)
                }
            }
        } else {
            val (label, containerColor, contentColor) = when (selectedChoice.outcome) {
                ScenarioOutcome.Safe -> Triple("RESPUESTA SEGURA", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                ScenarioOutcome.CriticalFail -> Triple("FALLO CRÍTICO INTERNO", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
                ScenarioOutcome.NeedsCorrection -> Triple("NECESITA CORRECCIÓN", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
            }
            StatusPill(text = label, containerColor = containerColor, contentColor = contentColor)
            Text(selectedChoice.feedbackEs, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(
                "Criterios observados: " + scenario.rubricIds.mapNotNull { rubricCriterion(it)?.labelEs }.joinToString(", "),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            OutlinedButton(onClick = { selectedChoiceId = null }) {
                Text("Reintentar")
            }
        }
    }
}
