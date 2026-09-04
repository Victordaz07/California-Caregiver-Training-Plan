package com.caregiverproca.app.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caregiverproca.app.content.Disclaimers
import com.caregiverproca.app.ui.components.DisclaimerBanner
import com.caregiverproca.app.ui.components.SectionCard

/**
 * Onboarding steps 1 (idioma/accesibilidad — español por defecto en esta
 * versión, ver DECISIONS.md), 2 (objetivo) y 5 (aviso educativo y
 * consentimiento) de Fase 3 del prompt maestro, condensados en una sola
 * pantalla de bienvenida antes de [PathwaySelectionScreen]. El condado
 * opcional (paso 4) y el plan inicial editable (paso 6) quedan pendientes —
 * ver DECISIONS.md.
 */
@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    Scaffold(containerColor = MaterialTheme.colorScheme.surface) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Bienvenido a Caregiver Pro CA",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Antes de empezar, queremos ser claros sobre qué es esta app y qué no es.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            item { DisclaimerBanner(text = Disclaimers.General) }
            item { DisclaimerBanner(text = Disclaimers.Emergency, emergency = true) }
            items(
                listOf(
                    Disclaimers.Scope,
                    Disclaimers.HandsOn,
                    Disclaimers.Certificate,
                    Disclaimers.Sources,
                ),
            ) { text ->
                SectionCard {
                    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
            }
            item {
                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text("Entiendo, elegir mi ruta")
                }
            }
        }
    }
}
