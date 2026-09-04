package com.caregiverproca.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.DownloadForOffline
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * One entry per screen of the Stitch export (see /docs/design/DESIGN.md and
 * the /screens *.html files at the repo root for the source design).
 */
enum class Screen(
    val route: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
) {
    RutinaDiaria(
        route = "rutina_diaria",
        title = "Rutina Diaria (75 min)",
        subtitle = "Sesión guiada del día y progreso de 90 días",
        icon = Icons.Outlined.Timeline,
    ),
    EscenariosFlashcards(
        route = "escenarios_flashcards",
        title = "Escenarios y Flashcards",
        subtitle = "Simulaciones clínicas y repaso espaciado",
        icon = Icons.Outlined.Psychology,
    ),
    SimulacionTurno(
        route = "simulacion_turno",
        title = "Simulación de Turno y Handoff",
        subtitle = "Entrega de turno bajo el estándar DAR",
        icon = Icons.Outlined.Assignment,
    ),
    RolePlayBilingue(
        route = "role_play_bilingue",
        title = "Role Play Bilingüe (Semana 10)",
        subtitle = "Diálogos en español e inglés con evaluación de IA",
        icon = Icons.Outlined.Mic,
    ),
    AudioLecciones(
        route = "audio_lecciones",
        title = "Audio Lecciones (Manos Libres)",
        subtitle = "Micro-podcasts narrados por IA, sin pantalla",
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
        title = "Requisitos y Certificación CA",
        subtitle = "Home Care Aide Registry (CDSS · CCLD)",
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
        subtitle = "Afinidad de carrera y rutas salariales en CA",
        icon = Icons.Outlined.Verified,
    ),
    Plan90Dias(
        route = "plan_90_dias",
        title = "Plan de 90 Días y Evaluaciones",
        subtitle = "Trayectoria completa y regla de oro de seguridad",
        icon = Icons.Outlined.Gavel,
    ),
    ;

    companion object {
        const val HOME_ROUTE = "home"
    }
}
