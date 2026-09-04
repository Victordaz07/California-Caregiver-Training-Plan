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
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.LabeledProgress
import com.caregiverproca.app.ui.components.NumberedStep
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill
import com.caregiverproca.app.ui.navigation.Screen

private data class Requirement(val title: String, val description: String, val status: String)

private val requirements = listOf(
    Requirement("Portal Guardian (CDSS)", "Registro en línea y arancel oficial ($35 tarifa estatal CA). PIN de agencia opcional o independiente.", "Pendiente"),
    Requirement("Live Scan DOJ / FBI", "Huellas dactilares electrónicas y verificación de antecedentes penales (formulario LIC 9163 HCA).", "Requiere Paso 1"),
    Requirement("Prueba de Tuberculosis (TB)", "Prueba dérmica PPD o QuantiFERON negativa emitida en los últimos 90 días.", "Pendiente"),
    Requirement("Capacitación Acreditada", "Mínimo legal CA: 5h iniciales · Tu Plan Pro: 110h avanzadas (2.5 de 110 horas acumuladas).", "En Progreso 2%"),
    Requirement("CPR / First Aid / AED", "Adulto y pediátrico con práctica presencial (AHA o Cruz Roja).", "Día 30 Límite"),
)

/** Mirrors /screens/requisitos-y-certificacion-ca.html. */
@Composable
fun RequisitosCertificacionScreen(onBack: () -> Unit) {
    DetailScaffold(title = Screen.RequisitosCertificacion.title, onBack = onBack) {
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            "CDSS · CCLD División",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                        )
                        Text(
                            "Home Care Aide (HCA)",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    StatusPill(text = "1/5 COMPLETADO")
                }
                Text(
                    "Seguimiento oficial de cumplimiento para el Home Care Aide Registry de California (Health and Safety Code 1796.12).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LabeledProgress(label = "Pasos Oficiales Obligatorios", progress = 1f / 5f, trailingLabel = "5 requisitos clave")
            }
        }

        item {
            SectionCard {
                requirements.forEachIndexed { index, requirement ->
                    Column {
                        NumberedStep(index + 1, requirement.title, requirement.description)
                        Text(
                            requirement.status.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }

        item {
            SectionCard {
                Text(
                    "Límites Legales de California",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    "Diferencia Crítica: HCA vs HHA — Home Care Aide (No Médico · CDSS). Tu certificación activa está enfocada en la dignidad, seguridad e independencia en el hogar:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                listOf(
                    "Asistencia en Actividades de la Vida Diaria (AVDs: baño, vestido)",
                    "Transferencias seguras y prevención activa de caídas",
                    "Recordatorio verbal de medicamentos autoadministrados",
                    "Preparación de alimentos terapéuticos y compañía",
                ).forEach { line ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(line, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}
