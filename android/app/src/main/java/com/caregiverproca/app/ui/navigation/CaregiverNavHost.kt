package com.caregiverproca.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.caregiverproca.app.ui.screens.AnalizadorVocacionalScreen
import com.caregiverproca.app.ui.screens.AudioLeccionesScreen
import com.caregiverproca.app.ui.screens.BibliotecaAudiosScreen
import com.caregiverproca.app.ui.screens.EscenariosFlashcardsScreen
import com.caregiverproca.app.ui.screens.HomeScreen
import com.caregiverproca.app.ui.screens.Plan90DiasScreen
import com.caregiverproca.app.ui.screens.RecursosSemanaScreen
import com.caregiverproca.app.ui.screens.RequisitosCertificacionScreen
import com.caregiverproca.app.ui.screens.RolePlayBilingueScreen
import com.caregiverproca.app.ui.screens.RutinaDiariaScreen
import com.caregiverproca.app.ui.screens.SimulacionTurnoScreen

@Composable
fun CaregiverNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HOME_ROUTE) {
        composable(Screen.HOME_ROUTE) {
            HomeScreen(onOpenScreen = { screen -> navController.navigate(screen.route) })
        }

        val onBack: () -> Unit = { navController.popBackStack() }

        composable(Screen.RutinaDiaria.route) { RutinaDiariaScreen(onBack) }
        composable(Screen.EscenariosFlashcards.route) { EscenariosFlashcardsScreen(onBack) }
        composable(Screen.SimulacionTurno.route) { SimulacionTurnoScreen(onBack) }
        composable(Screen.RolePlayBilingue.route) { RolePlayBilingueScreen(onBack) }
        composable(Screen.AudioLecciones.route) { AudioLeccionesScreen(onBack) }
        composable(Screen.BibliotecaAudios.route) { BibliotecaAudiosScreen(onBack) }
        composable(Screen.RequisitosCertificacion.route) { RequisitosCertificacionScreen(onBack) }
        composable(Screen.RecursosSemana.route) { RecursosSemanaScreen(onBack) }
        composable(Screen.AnalizadorVocacional.route) { AnalizadorVocacionalScreen(onBack) }
        composable(Screen.Plan90Dias.route) { Plan90DiasScreen(onBack) }
    }
}
