package com.caregiverproca.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.content.LegalRequirement
import com.caregiverproca.app.content.PathwayId
import com.caregiverproca.app.content.RequirementStatus
import com.caregiverproca.app.content.legalRequirementsFor
import com.caregiverproca.app.content.pathwayById
import com.caregiverproca.app.ui.components.DetailScaffold
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill

/**
 * "Carrera > Mi ruta > Requisitos oficiales". Rewritten to be pathway-aware,
 * driven entirely by content/LegalRequirements.kt (itself verbatim from
 * LEGAL_REQUIREMENTS_CA.json) instead of a fixed five-item list — this is the
 * direct fix for A-001/A-002/A-004/A-009/A-010/A-011/A-012: the app now shows
 * only the requirements that apply to the pathway the user actually chose,
 * with correct registration/registry language instead of "certificación".
 */
@Composable
fun RequisitosCertificacionScreen(pathwayId: PathwayId, onBack: () -> Unit) {
    val pathway = pathwayById(pathwayId)
    val requirements = legalRequirementsFor(pathwayId)

    DetailScaffold(title = "Requisitos — ${pathway.titleEs}", onBack = onBack) {
        item { DisclaimerBanner(text = pathway.routeNoticeEs) }
        item { DisclaimerBanner(text = Disclaimers.Sources) }

        if (requirements.isEmpty()) {
            item {
                SectionCard {
                    Text(
                        "Esta ruta no tiene requisitos estatales específicos registrados en este paquete. Consulta Adult Protective Services y las fuentes generales en Recursos.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }

        items(requirements) { requirement -> RequirementCard(requirement) }
    }
}

@Composable
private fun RequirementCard(requirement: LegalRequirement) {
    SectionCard {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                requirement.titleEs,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            StatusPill(text = requirement.status.labelEs())
        }
        Text(
            requirement.summaryEs,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (requirement.feeUsd != null) {
            Text(
                "Tarifa verificada: \$${requirement.feeUsd} (puede cambiar; ver fuente).",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
        if (requirement.effectiveFrom != null) {
            Text(
                "Vigente desde: ${requirement.effectiveFrom}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        requirement.sourceUrls.forEach { url ->
            Text(
                url,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private fun RequirementStatus.labelEs(): String = when (this) {
    RequirementStatus.RequiredForPathway -> "REQUERIDO EN TU RUTA"
    RequirementStatus.ConditionalByPathway -> "CONDICIONAL"
    RequirementStatus.FutureRequirement -> "VIGENCIA FUTURA"
    RequirementStatus.RecommendedOrExternalRequirement -> "SEGÚN EMPLEADOR"
    RequirementStatus.RoleAndFactDependent -> "SEGÚN EL CASO"
    RequirementStatus.InternalOnly -> "SOLO INTERNO"
}
