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
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.HubScaffold
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill

/**
 * "Hoy" tab root. Mirrors /screens/rutina-diaria-75-minutos.html (the daily
 * 75-minute guided session), corrected per A-008: an emergency always
 * overrides the session timer, shown explicitly rather than implied.
 *
 * The "Iniciar Sesión" button is still a no-op — Fase 4 (motor de aprendizaje,
 * cinco bloques reanudables) is not implemented in this session. See
 * docs/caregiver_upgrade/DECISIONS.md.
 */
@Composable
fun RutinaDiariaScreen() {
    HubScaffold(title = "Hoy", subtitle = "Día 1 · Semana 1") {
        item { DisclaimerBanner(text = Disclaimers.Emergency, emergency = true) }

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
                    "Construyendo competencia y empatía dentro de tu alcance no médico. " + Disclaimers.Scope,
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
                        onClick = { /* Pendiente: Fase 4 (motor de aprendizaje) no implementada en este lote. */ },
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
                            "REGLA INVIOLABLE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryFixedVariant,
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
