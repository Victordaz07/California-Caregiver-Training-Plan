package com.caregiverproca.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A dismiss-free, always-visible legal/safety notice. Two variants:
 * - [emergency] = true: the 911-first override (A-008) — used on any screen with
 *   a timer or a scenario that could be mistaken for a real emergency workflow.
 * - [emergency] = false: the general independent-educational-tool disclaimer
 *   (A-044) — shown on first run and on the internal certificate/export screen.
 *
 * Text is verbatim from content/Disclaimers.kt (itself verbatim from the
 * upgrade pack's DISCLAIMERS_ES_EN.json) — never inline a new disclaimer string
 * in a screen.
 */
@Composable
fun DisclaimerBanner(text: String, modifier: Modifier = Modifier, emergency: Boolean = false) {
    val containerColor = if (emergency) {
        MaterialTheme.colorScheme.errorContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val contentColor = if (emergency) {
        MaterialTheme.colorScheme.onErrorContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = if (emergency) Icons.Outlined.Warning else Icons.Outlined.Info,
            contentDescription = null,
            tint = contentColor,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = contentColor,
        )
    }
}
