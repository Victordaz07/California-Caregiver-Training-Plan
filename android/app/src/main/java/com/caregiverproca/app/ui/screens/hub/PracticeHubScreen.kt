package com.caregiverproca.app.ui.screens.hub

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.caregiverproca.app.ui.components.HubScaffold
import com.caregiverproca.app.ui.components.ScreenNavCard
import com.caregiverproca.app.ui.navigation.Screen

/** "Práctica" tab root: escenarios/flashcards, simulación de turno y role-play viven aquí (ver INVENTARIO_PANTALLAS.md). */
@Composable
fun PracticeHubScreen(onOpenScreen: (Screen) -> Unit) {
    HubScaffold(title = "Práctica", subtitle = "Escenarios, turno y role-play") {
        item {
            Text(
                "Practica antes de que ocurra de verdad: responde antes de ver la solución.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            ScreenNavCard(
                title = Screen.EscenariosFlashcards.title,
                subtitle = Screen.EscenariosFlashcards.subtitle,
                icon = Screen.EscenariosFlashcards.icon,
                onClick = { onOpenScreen(Screen.EscenariosFlashcards) },
            )
        }
        item {
            ScreenNavCard(
                title = Screen.SimulacionTurno.title,
                subtitle = Screen.SimulacionTurno.subtitle,
                icon = Screen.SimulacionTurno.icon,
                onClick = { onOpenScreen(Screen.SimulacionTurno) },
            )
        }
        item {
            ScreenNavCard(
                title = Screen.RolePlayBilingue.title,
                subtitle = Screen.RolePlayBilingue.subtitle,
                icon = Screen.RolePlayBilingue.icon,
                onClick = { onOpenScreen(Screen.RolePlayBilingue) },
            )
        }
    }
}
