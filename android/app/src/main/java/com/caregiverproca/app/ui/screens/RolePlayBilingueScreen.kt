package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.components.VoiceRecordCard
import com.caregiverproca.app.ui.navigation.Screen

/**
 * Mirrors /screens/role-play-audio-bilingue-semana-10.html. Corrected per
 * A-028: there is no AI backend evaluating pronunciation or tone, so the
 * previous "Evaluación de IA — 94% (C2 Care)" section (fabricated scores) is
 * replaced by a self-check against the model phrases, and the fake "Escuchar
 * Audio"/"regrabar" controls are replaced by a real recording (see
 * VoiceRecordCard — the source pack ships no narrated audio, only this
 * script; see docs/caregiver_upgrade/DECISIONS.md ADR-006).
 */
@Composable
fun RolePlayBilingueScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.RolePlayBilingue.title, onBack = onBack) {
        item { DisclaimerBanner(text = Disclaimers.FictionalCase) }
        item { DisclaimerBanner(text = Disclaimers.Voice) }
        item {
            SectionCard {
                Text(
                    "Semana 10 · Comunicación y Límites",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                LabeledProgress(label = "14 de 20 Role-Plays", progress = 0.7f, trailingLabel = "70% completo")
                Text(
                    "Meta: 10 ES / 10 EN — 8/10 en Español (80%) · 6/10 en English (60%)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Todos (20)", "Español (10)", "English (10)").forEach {
                        StatusPill(text = it)
                    }
                }
            }
        }

        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Caso #15 · English Track",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    StatusPill(text = "DIFICULTAD MEDIA")
                }
                Text(
                    "Setting Boundaries with Overdemanding Family Member",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Margaret & Daughter Evelyn · Palo Alto, CA · IHSS Case (cliente no-médico desde 2023)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "Situación Inesperada: la hija del cliente llega y ordena en tono autoritario que limpies el techo del garaje y cambies la dosis de pastillas de su madre antes de que despierte.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Column {
                        Text(
                            "Límite de tu Función (No Médico)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                        Text(
                            "El cuidador NO puede alterar dosis de fármacos ni realizar tareas domésticas pesadas o peligrosas ajenas al cuidado directo y seguro del beneficiario.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Guion del Escenario (Voz Evelyn)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "“Listen, while mom is asleep, you need to go clean the garage ceiling and double her sleeping medication tonight!”",
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "ES: “Escuche, mientras mamá duerme, usted necesita ir a limpiar el techo del garaje y duplicar su medicina para dormir esta noche.”",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "Lee el guion en voz alta como si Evelyn te lo dijera; no hay audio narrado todavía, solo este texto.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }

        item { VoiceRecordCard(promptLabel = "Graba tu respuesta en inglés usando la frase modelo de abajo.") }

        item {
            SectionCard {
                Text(
                    "Frase Modelo Recomendada (Cheat Sheet)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text("English (Target)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(
                    "“I understand you want the best for your mother. However, as an In-Home Caregiver under California guidelines, I am not authorized to adjust medications or perform heavy maintenance. I will note your concern and inform my supervisor immediately.”",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text("Español (Equivalente)", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(
                    "“Entiendo que quiera lo mejor para su madre. Sin embargo, bajo las normas de California, no estoy autorizado a cambiar medicamentos ni realizar tareas pesadas. Notificaré a mi supervisor.”",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        item {
            SectionCard {
                Text(
                    "Autoevaluación (sin IA conectada)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Escucha tu grabación y compárala con la frase modelo. Esta app no tiene un backend de IA que evalúe tono o pronunciación — la evaluación es tuya.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                listOf(
                    "¿Mantuviste un tono empático y asertivo?",
                    "¿Respetaste tu límite de función sin dar instrucciones clínicas?",
                    "¿Se entendió tu pronunciación en inglés?",
                ).forEach { question ->
                    Text("• $question", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
