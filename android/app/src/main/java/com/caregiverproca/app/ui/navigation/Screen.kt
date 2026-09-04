package com.caregiverproca.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Screens pushed on top of one of the five [MainTab] hubs (see
 * INVENTARIO_PANTALLAS.md for the mapping from the original Stitch screens).
 * "Rutina Diaria" and "Plan 90 Días" are not here anymore — they are the
 * content of the Hoy/Plan hub roots themselves, not screens pushed from them.
 */
enum class Screen(
    val route: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
) {
    EscenariosFlashcards(
        route = "escenarios_flashcards",
        title = "Escenarios y Flashcards",
        subtitle = "Simulaciones y repaso espaciado",
        icon = Icons.Outlined.Psychology,
    ),
    SimulacionTurno(
        route = "simulacion_turno",
        title = "Simulación de Turno y Handoff",
        subtitle = "Práctica de entrega de turno con DAR",
        icon = Icons.Outlined.Assignment,
    ),
    RolePlayBilingue(
        route = "role_play_bilingue",
        title = "Role Play Bilingüe (Semana 10)",
        subtitle = "Diálogos en español e inglés",
        icon = Icons.Outlined.Mic,
    ),
    AudioLecciones(
        route = "audio_lecciones",
        title = "Audio Lecciones (Manos Libres)",
        subtitle = "Orientación guiada en audio",
        icon = Icons.Outlined.AutoAwesome,
    ),
    BibliotecaAudios(
        route = "biblioteca_audios",
        title = "Biblioteca de Audios (13 Semanas)",
        subtitle = "Lecciones descargables para escuchar offline",
        icon = Icons.Outlined.DownloadForOffline,
    ),
    RequisitosCertificacion(
        route = "requisitos_certificacion",
        title = "Requisitos por tu Ruta",
        subtitle = "Registro, inscripción o certificación según tu ruta",
        icon = Icons.Outlined.Policy,
    ),
    RecursosSemana(
        route = "recursos_semana",
        title = "Recursos por Semana",
        subtitle = "Portales oficiales y certificaciones por módulo",
        icon = Icons.Outlined.TravelExplore,
    ),
    AnalizadorVocacional(
        route = "analizador_vocacional",
        title = "Analizador Vocacional",
        subtitle = "Explora rutas dentro de tu ruta elegida",
        icon = Icons.Outlined.Verified,
    ),
}
