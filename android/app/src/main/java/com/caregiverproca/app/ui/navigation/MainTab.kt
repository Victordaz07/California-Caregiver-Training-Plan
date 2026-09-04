package com.caregiverproca.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * The five stable root destinations required by Fase 3 of the master prompt
 * and 03_ANDROID_TECHNICAL/ANDROID_ARCHITECTURE.md ("Navegación raíz"):
 * Hoy / Plan / Práctica / Audio / Carrera. This fixes A-029 — the previous
 * flat Home gallery is now the "Hoy" root plus four sibling hubs, and the
 * bottom bar never changes shape between screens.
 */
enum class MainTab(val route: String, val label: String, val icon: ImageVector) {
    Today(route = "hub_today", label = "Hoy", icon = Icons.Outlined.Timeline),
    Plan(route = "hub_plan", label = "Plan", icon = Icons.Outlined.CalendarMonth),
    Practice(route = "hub_practice", label = "Práctica", icon = Icons.Outlined.Psychology),
    Audio(route = "hub_audio", label = "Audio", icon = Icons.Outlined.Headphones),
    Career(route = "hub_career", label = "Carrera", icon = Icons.Outlined.Work),
}
