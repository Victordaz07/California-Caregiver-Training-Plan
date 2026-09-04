package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.caregiverproca.app.ui.components.NumberedStep
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

/**
 * Mirrors /screens/escenarios-y-flashcards.html. Corrected per A-014/A-039:
 * this is an internal practice protocol, not a "Protocolo CDSS California",
 * and the scenario case is explicitly labeled fictional.
 */
@Composable
fun EscenariosFlashcardsScreen(onBack: () -> Unit) {
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
                        Text(
                            "Simulaciones Clínicas",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            "Práctica interna · CA Caregiver",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    StatusPill(text = "🔥 RACHA: 5 DÍAS")
                }
                LabeledProgress(
                    label = "Distribución del día (10 actividades)",
                    progress = 0.8f,
                    trailingLabel = "8/10 completadas",
                )
                Text(
                    "60% Límites y Dignidad · 20% Emergencias · 20% Repaso previo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
                        "Flashcard Situacional Activa",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Tarjeta 4 de 10",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Medication, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Text(
                        "Manejo Seguro de Fármacos",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Text(
                    "“Encuentro una pastilla suelta sin identificar en el piso de la habitación del cliente. " +
                        "¿Qué acciones están estrictamente dentro de mi función como cuidador en California?”",
                    style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Tómate 10 segundos para estructurar tus límites regulatorios antes de comprobar la respuesta oficial.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text("Ver Ficha de Aprendizaje")
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Protocolo Interno de Práctica (4 Pasos Clave)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                NumberedStep(1, "Proteger sin suministrar", "Jamás asumir qué pastilla es ni dársela al cliente. No desecharla en la basura común.")
                NumberedStep(2, "Aislar el hallazgo", "Colocarla en un recipiente limpio etiquetado “Encontrada en piso” sin mezclarla con el pastillero oficial.")
                NumberedStep(3, "Notificación expedita", "Contactar al supervisor de agencia, enfermero a cargo o familiar responsable designado de inmediato.")
                NumberedStep(4, "Registro de Turno", "Consignar hora precisa, lugar exacto del hallazgo, aspecto visual y persona notificada.")
            }
        }

        item {
            SectionCard {
                Text(
                    "Autoevaluación de asimilación (Algoritmo SM-2)",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    listOf("Difícil" to "En 1 día", "Bien" to "En 3 días", "Fácil" to "En 7 días").forEach { (label, sub) ->
                        OutlinedButton(onClick = { }, modifier = Modifier.weight(1f)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(label, style = MaterialTheme.typography.labelLarge)
                                Text(sub, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
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
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                        Text(
                            "Simulación Deliberada · Caso Ficticio #42",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Text("03:42", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                }
                StatusPill(text = "DEMENTIA & DIGNITY")
                Text(
                    "El cliente rechaza bañarse y muestra visible frustración.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Column {
                        Text(
                            "Principio de Práctica",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                        Text(
                            "Ningún cuidador debe usar contención física ni coacción verbal. Prioriza la dignidad y el consentimiento de la persona, y sigue su plan de cuidado.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                LabeledProgress(
                    label = "Algoritmo de Acción (Paso a Paso)",
                    progress = 2f / 6f,
                    trailingLabel = "2 de 6 listos",
                )
                Text(
                    "1. Seguridad inmediata verificada",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
