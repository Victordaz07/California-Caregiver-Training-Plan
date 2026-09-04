package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.item
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

/** Mirrors /screens/rutina-diaria-75-minutos.html: the daily 75-minute guided session. */
@Composable
fun RutinaDiariaScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.RutinaDiaria.title, onBack = onBack) {
        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                StatusPill(
                    text = "FASE 1: FUNDAMENTOS",
                    containerColor = MaterialTheme.colorScheme.primaryFixed,
                    contentColor = MaterialTheme.colorScheme.onPrimaryFixedVariant,
                )
                Text(
                    "Semana 1: Dignidad, Privacidad y Alcance No Médico",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    "Construyendo la competencia y empatía requerida por el Código de Regulaciones de California (CDSS Title 22).",
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
                            "SESIÓN GUIADA",
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
                    label = "0 / 75 min",
                    progress = 0f,
                    trailingLabel = "0% completado",
                )

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
                            "5 bloques estructurados",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Button(
                        onClick = { /* Skeleton: la sesión guiada real se conecta en una próxima iteración. */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                        ),
                    ) {
                        Icon(Icons.Outlined.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Iniciar Sesión")
                    }
                }
            }
        }

        item {
            SectionCard(containerColor = MaterialTheme.colorScheme.secondaryFixed) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        Icons.Outlined.PriorityHigh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Column {
                        Text(
                            "REGLA INVIOLABLE EN CALIFORNIA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryFixedVariant,
                        )
                        Text(
                            "Cero errores críticos: jamás emitir diagnósticos médicos, prescribir ni realizar administración invasiva de fármacos. Tu rol protege la dignidad dentro del marco IHSS / No Médico.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }
    }
}
