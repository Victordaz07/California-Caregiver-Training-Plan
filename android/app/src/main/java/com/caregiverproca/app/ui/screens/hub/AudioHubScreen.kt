package com.caregiverproca.app.ui.screens.hub

import androidx.compose.foundation.lazy.item
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.caregiverproca.app.ui.components.HubScaffold
import com.caregiverproca.app.ui.components.ScreenNavCard
import com.caregiverproca.app.ui.navigation.Screen

/** "Audio" tab root: lecciones de audio y biblioteca offline. */
@Composable
fun AudioHubScreen(onOpenScreen: (Screen) -> Unit) {
    HubScaffold(title = "Audio", subtitle = "Lecciones y biblioteca offline") {
        item {
            Text(
                "Escucha mientras cocinas, caminas o te trasladas entre turnos.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        item {
            ScreenNavCard(
                title = Screen.AudioLecciones.title,
                subtitle = Screen.AudioLecciones.subtitle,
                icon = Screen.AudioLecciones.icon,
                onClick = { onOpenScreen(Screen.AudioLecciones) },
            )
        }
        item {
            ScreenNavCard(
                title = Screen.BibliotecaAudios.title,
                subtitle = Screen.BibliotecaAudios.subtitle,
                icon = Screen.BibliotecaAudios.icon,
                onClick = { onOpenScreen(Screen.BibliotecaAudios) },
            )
        }
    }
}
