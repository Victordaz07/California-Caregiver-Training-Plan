package com.caregiverproca.app.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Pathway
import com.caregiverproca.app.content.PathwayId
import com.caregiverproca.app.content.Pathways
import com.caregiverproca.app.ui.components.SectionCard
import com.caregiverproca.app.ui.components.StatusPill

/**
 * Fase 3, paso 3: selección explícita de ruta (A-001). No hay una ruta por
 * defecto — [PATHWAYS_CA.json].defaultPathwayId es null a propósito, así que
 * esta pantalla nunca preselecciona nada; el usuario elige.
 *
 * Test tag sugerido por 03_ANDROID_TECHNICAL/SCREEN_REQUIREMENTS.md:
 * `onboarding_pathway_list` / `onboarding_continue` — pendiente de añadir
 * hasta que exista una suite de pruebas de UI real (ver TEST_REPORT.md).
 */
@Composable
fun PathwaySelectionScreen(onSelect: (PathwayId) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("¿Cuál es tu situación?", style = MaterialTheme.typography.headlineSmall) },
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Text(
                    "HCA, IHSS, HHA y CNA no son la misma credencial. Elige la ruta que describe tu situación real; puedes cambiarla después desde Carrera.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            items(Pathways) { pathway -> PathwayCard(pathway, onSelect) }
        }
    }
}

@Composable
private fun PathwayCard(pathway: Pathway, onSelect: (PathwayId) -> Unit) {
    SectionCard {
        Text(pathway.titleEs, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        Text(pathway.summaryEs, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (!pathway.stateCredentialFromApp) {
            StatusPill(
                text = "ESTA APP NO OTORGA CREDENCIAL ESTATAL",
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            pathway.coreStepsEs.forEach { step ->
                Text("• $step", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            }
        }
        Text(pathway.routeNoticeEs, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Button(
            onClick = { onSelect(pathway.id) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Text("Elegir esta ruta")
        }
    }
}
